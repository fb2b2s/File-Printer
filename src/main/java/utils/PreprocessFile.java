package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class PreprocessFile {
    final String ROW_SEPARATOR_REGEX = "\\n";
    final String ENTRY_SEPARATOR_REGEX = "\\t";

    private final String fileContent;
    private final int columnsCount;
    private final int lineCharLimit;

    public PreprocessFile(String filePath, int columnsCount, int lineCharLimit) throws IOException {
        this.fileContent = Files.readString(Paths.get(filePath));
        this.columnsCount = columnsCount;
        this.lineCharLimit = lineCharLimit;
    }

    public List<List<List<String>>> getRowCellLines() {
        // Get lines in a cell for all cells in a row
        String[] rowLines = fileContent.split(ROW_SEPARATOR_REGEX);
        List<List<List<String>>> rowCellLines = new ArrayList<>();
        for (String line : rowLines) {
            String[] cells = line.split(ENTRY_SEPARATOR_REGEX);
            List<List<String>> cellLines = new ArrayList<>(); // this variable has lines for all cells in a row
            for (int i = 0; i < this.columnsCount; i++) {
                List<String> linesCell = splitCellIntoLines(cells[i]); // get all lines in a cell
                cellLines.add(linesCell);
            }
            rowCellLines.add(cellLines);
        }
        return rowCellLines;
    }

    // make lines out of all the text in a cell
    private List<String> splitCellIntoLines(String cell) {
        List<String> lines = new ArrayList<>();
        for (int start = 0; start < cell.length(); start += lineCharLimit) {
            int end = Math.min(start + lineCharLimit, cell.length());
            lines.add(cell.substring(start, end));
        }
        return lines;
    }

    public int[] getMaxWidthOfColumns(List<List<List<String>>> rowCellLines) {
        int[] maxWidth = new int[columnsCount];
        for (List<List<String>> cellLines : rowCellLines) {
            for (int i = 0; i < cellLines.size(); i++) {
                // if a cell has multiple lines, then the width is LIMIT
                if (cellLines.get(i).size() > 1) {
                    maxWidth[i] = lineCharLimit;
                } else {
                    // if only 1 line then compare with the line's length
                    maxWidth[i] = Math.max(maxWidth[i], cellLines.get(i).get(0).length());
                }
            }
        }
        return maxWidth;
    }
}
