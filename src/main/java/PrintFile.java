import utils.PreprocessFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static constants.WhiteSpaces.ENDLINE;
import static constants.WhiteSpaces.TAB;

// complete the basic functionality then think about the enhancements
public class PrintFile {
    final int COLUMNS_COUNT = 4;
    final int LIMIT = 100;
    final int BUFFER_MULTIPLIER = 1;

    // The most important part of a text file
    private final List<List<List<String>>> rowCellLines;
    private final int[] maxWidth;

    public PrintFile(String filePath) throws IOException {
        PreprocessFile preprocessFile = new PreprocessFile(filePath, COLUMNS_COUNT, LIMIT);
        this.rowCellLines = preprocessFile.getRowCellLines();
        this.maxWidth = preprocessFile.getMaxWidthOfColumns(rowCellLines);
    }

    //TODO : Separate main to a different class
    //TODO : Create tests for all the methods
    public static void main(String[] args) throws IOException {
        PrintFile pf = new PrintFile(args[0]);
        // Build rows
        List<String> rows = pf.buildRows();
        // Print the result
        rows.forEach(System.out::print);
    }

    private List<String> buildRows() {
        // Build each row using the lines of cell and max width
        List<String> rows = new ArrayList<>();
        for (List<List<String>> cellLines : this.rowCellLines) {
            StringBuilder row = new StringBuilder();

            // indices for lines of each cell
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
            rows.add(row.toString());
        }
        return rows;
    }

    // while loop condition breaker
    private boolean cellsLeft(List<List<String >> cellsLines, int[] indices) {
        boolean loop = false;
        for (int i = 0; i < cellsLines.size(); i++) {
            loop = (loop ||  indices[i] < cellsLines.get(i).size());
        }
        return loop;
    }
}
