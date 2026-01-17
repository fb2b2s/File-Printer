package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class PreprocessFile {
    final String ROW_SEPARATOR_REGEX = "\\n";
    final String ENTRY_SEPARATOR_REGEX = "\\t";

    private final String fileConent;
    private final int columnsCount;
    private final int lineCharLimit;

    public PreprocessFile(String filePath, int columnsCount, int lineCharLimit) throws IOException {
        this.fileConent = Files.readString(Paths.get(filePath));
        this.columnsCount = columnsCount;
        this.lineCharLimit = lineCharLimit;
    }

    public List<List<List<String>>> getRowCellLines() {
        // Get lines in a cell for all cells in a row
        String[] rowLines = fileConent.split(ROW_SEPARATOR_REGEX);
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
        int cellLinesCount = (cell.length() % lineCharLimit == 0) ? cell.length() / lineCharLimit : cell.length() / lineCharLimit + 1;
        List<String> lines = new ArrayList<>();
        int idx = 0;
        for (int i = 0; i < cellLinesCount - 1; i++) {
            lines.add(cell.substring(idx, idx + lineCharLimit));
            idx += lineCharLimit;
        }

        // handle the last separately
        if (idx < cell.length()) {
            lines.add(cell.substring(idx));
        }
        return lines;
    }
}
