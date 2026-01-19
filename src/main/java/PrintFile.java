import utils.FileReader;
import utils.PreprocessFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static constants.WhiteSpaces.ENDLINE;
import static constants.WhiteSpaces.TAB;

// complete the basic functionality then think about the enhancements
public class PrintFile {
    // The most important part of a text file
    private final List<List<List<String>>> rowCellLines;
    private final int[] maxWidth;
    private final int columnsCount;
    private final int lineCharLimit;
    private final int bufferMultiplier;

    public PrintFile(PreprocessFile preprocessFile, int columnsCount, int lineCharLimit, int bufferMultiplier) {
        this.rowCellLines = preprocessFile.getRowCellLines();
        this.maxWidth = preprocessFile.getMaxWidthOfColumns(rowCellLines);
        this.columnsCount = columnsCount;
        this.lineCharLimit = lineCharLimit;
        this.bufferMultiplier = bufferMultiplier;
    }

    @Override
    public String toString() {
        return "File metadata : " + "{ no. of rows : " + this.rowCellLines.size() + ", columnsCount : " + this.columnsCount
                + ", lineCharLimit : " + this.lineCharLimit + ", bufferMultiplier : " + this.bufferMultiplier + " }";
    }

    //TODO : Separate main to a different class
    //TODO : Create tests for all the methods
    public static void main(String[] args) throws IOException {
        // Read the file
        FileReader fr = new FileReader(args[0]);
        String fileContent = fr.getFileContent();

        // Pre-process the file
        PreprocessFile preprocessFile = new PreprocessFile(fileContent, Integer.parseInt(args[1]), Integer.parseInt(args[2]));

        // Print file
        PrintFile pf = new PrintFile(preprocessFile,
                Integer.parseInt(args[1]),
                Integer.parseInt(args[2]),
                Integer.parseInt(args[3]));

        // Check if the file is valid
        if (preprocessFile.isFileValid()) {
            System.out.println("The file is valid, printing the file ");
            // Print metadata
            System.out.println("The file metadata : " + pf + "\n");
            // Build rows
            List<String> rows = pf.buildRows();
            // Print the result
            rows.forEach(System.out::print);
        } else {
            System.out.println("The file is in invalid format, can not print!!!!!!");
        }
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
                                .append(TAB.repeat(bufferMultiplier)); // add buffer
                    } else {
                        row.append(" ".repeat(maxWidth[i]))
                                .append(TAB.repeat(bufferMultiplier));
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
