package utils;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileWriterUtility {


    public static boolean isFileEmpty(String path) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(path, "r");
        return raf.length() == 0;
    }

    public static boolean doesFileNotExist(String path) throws IOException {
        return Files.notExists(Path.of(path));
    }


    public static boolean hasFileLastEmptyLine(String path) {
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(path, "r");
            long pos = raf.length() - 2;
            if (pos < 0) return false;

            // too short
            raf.seek(pos);
            return raf.read() == '\r' && raf.read() == '\n';
        } catch (IOException e) {
            return false;
        } finally {
            if (raf != null) try {
                raf.close();
            } catch (IOException ignored) {
            }
        }
    }

    public static void writeLineToFile (String line, String pathToFile) throws IOException {
        if (FileWriterUtility.doesFileNotExist(pathToFile)) {
            Files.createFile(Path.of(pathToFile));
        } else if (FileWriterUtility.isFileEmpty(pathToFile)) {
            Files.write(Path.of(pathToFile), line.getBytes(), StandardOpenOption.APPEND);
        } else if (FileWriterUtility.hasFileLastEmptyLine(pathToFile)) {
            Files.write(Path.of(pathToFile), line.getBytes(), StandardOpenOption.APPEND);
        } else {
            Files.write(Path.of(pathToFile), "\r\n".getBytes(), StandardOpenOption.APPEND);
            Files.write(Path.of(pathToFile), line.getBytes(), StandardOpenOption.APPEND);
        }
    }


    public static boolean isNumber(String s) throws NumberFormatException {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }



}
