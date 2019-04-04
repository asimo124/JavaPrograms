package info.alexhawley;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {

        File[] files = new File(args[0]).listFiles();
        showFiles(files, args[1]);
    }

    public static void showFiles(File[] files, String outputPath) {
        for (File file : files) {
            if (file.isDirectory()) {

                System.out.println("Directory: " + file.getName());
                showFiles(file.listFiles(), outputPath); // Calls same method again.

            } else {

                System.out.println("File: " + file.getAbsoluteFile());
                String pathDest = outputPath + "/" + file.getName();
                Path pathDest2 = Paths.get(pathDest);
                try {
                    Files.copy(file.getAbsoluteFile().toPath(), pathDest2);
                } catch (IOException e) {
                    System.out.println(e);
                }

            }
        }
    }
}
