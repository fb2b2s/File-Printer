package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileReader {
    private final Path filePath;

    public FileReader(String filePathString) {
        // the file path is currently assumed to be correct
        this.filePath = Paths.get(filePathString);
    }

    public String getFileContent() throws IOException {
        return Files.readString(filePath);
    }
}
