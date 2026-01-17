import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static constants.WhiteSpaces.ENDLINE;
import static constants.WhiteSpaces.TAB;

// complete the basic functionality then think about the enhancements
public class PrintFile {
    final int COLUMNS_COUNT = 4;
    final int LIMIT = 100;
    final int BUFFER_MULTIPLIER = 1;
    final String ROW_SEPARATOR_REGEX = "\\n";
    final String ENTRY_SEPARATOR_REGEX = "\\t";

    // The most important part of a text file
    private List<List<List<String>>> rowCellLines;

    public PrintFile() {
        this.rowCellLines = new ArrayList<>();
    }

    public static void main(String[] args) throws IOException {
        String filePath = args[0];
        String fileContent = Files.readString(Paths.get(filePath));

        PrintFile pf = new PrintFile();

        // Get lines
        String[] lines = pf.getLines(fileContent); // get each line

        // Build rows
        List<String> rows = pf.buildRows(lines);

        // Print the result
        rows.forEach(System.out::print);
    }

    private String[] getLines(String content) {
        return content.split(ROW_SEPARATOR_REGEX); // get each line
    }

    private List<String> buildRows(String[] rowLines) {
        // Get lines in a cell for all cells in a row
        for (String line : rowLines) {
            String[] cells = line.split(ENTRY_SEPARATOR_REGEX);
            List<List<String>> cellLines = new ArrayList<>(); // this variable has lines for all cells in a row
            for (int i = 0; i < COLUMNS_COUNT; i++) {
                List<String> linesCell = handleCell(cells[i]); // get all lines in a cell
                cellLines.add(linesCell);
            }
            this.rowCellLines.add(cellLines);
        }

        // get a max width for each column by going through every cell of a column
        int[] maxWidth = new int[COLUMNS_COUNT];
        for (List<List<String>> cellLines : this.rowCellLines) {
            for (int i = 0; i < cellLines.size(); i++) {
                // if a cell has multiple lines then the width is LIMIT
                if (cellLines.get(i).size() > 1) {
                    maxWidth[i] = LIMIT;
                } else {
                    // if only 1 line then compare with the line's length
                    maxWidth[i] = Math.max(maxWidth[i], cellLines.get(i).get(0).length());
                }
            }
        }

        // Build each row using the lines of cell and max width
        List<String> rows = new ArrayList<>();
        for (List<List<String>> cellLines : this.rowCellLines) {
            rows.add(buildRowFromCells(cellLines, maxWidth));
        }
        return rows;
    }

    // make lines out of all the text in a cell
    private List<String> handleCell(String cell) {
        int cellLinesCount = (cell.length() % LIMIT == 0) ? cell.length() / LIMIT : cell.length() / LIMIT + 1;
        List<String> lines = new ArrayList<>();
        int idx = 0;
        for (int i = 0; i < cellLinesCount - 1; i++) {
            lines.add(cell.substring(idx, idx + LIMIT));
            idx += LIMIT;
        }

        // handle the last separately
        if (idx < cell.length()) {
            lines.add(cell.substring(idx));
        }
        return lines;
    }

    private String buildRowFromCells(List<List<String>> cellLines, int[] maxWidth) {
        StringBuilder row = new StringBuilder();
        // get max width of each cell

        int[] indices = new int[cellLines.size()];
        while (cellsLeft(cellLines, indices)) {
            for (int i = 0; i < indices.length; i++) {
                if (indices[i] < cellLines.get(i).size()) {
                    String text = cellLines.get(i).get(indices[i]);
                    row.append(text) // add the text
                        .append(" ".repeat(maxWidth[i] - text.length())) // add remaining space with space
                        .append(TAB.repeat(BUFFER_MULTIPLIER)); // add buffer
                } else {
                    row.append(" ".repeat(maxWidth[i]))
                        .append(TAB.repeat(BUFFER_MULTIPLIER));
                }
                indices[i]++;
            }
            row.append(ENDLINE);
        }
        return row.toString();
    }

    boolean cellsLeft(List<List<String >> cellsLines, int[] indices) {
        boolean loop = false;
        for (int i = 0; i < cellsLines.size(); i++) {
            loop = (loop ||  indices[i] < cellsLines.get(i).size());
        }

        return loop;
    }
}
