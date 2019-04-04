package info.alexhawley;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static String path = "";
    private static List<String> FilesList = new ArrayList<String>();

    public static void main(String[] args) {
	    // write your code here
        if (args.length > 0) {
            path = args[0];
            File file = new File(path + "/list_files_output3.txt");
            boolean result = false;
            try {
                result = Files.deleteIfExists(file.toPath());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

            File[] files = new File(args[0]).listFiles();
            showFiles(files);

            for (String eachFile: FilesList) {
                try(FileWriter fw = new FileWriter(path + "/list_files_output3.txt", true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    PrintWriter out = new PrintWriter(bw))
                {
                    out.println(eachFile);
                } catch (IOException e) {
                    //exception handling left as an exercise for the reader
                }
            }

        } else {
            System.out.println("Please supply path to list files for.");
        }
    }

    public static void showFiles(File[] files) {
        for (File file : files) {
            if (file.isDirectory()) {
                //System.out.println("Directory: " + file.getName());
                showFiles(file.listFiles()); // Calls same method again.
            } else {
                FilesList.add(file.getAbsolutePath().replace("C:\\Users\\AHawley\\Documents", ""));
            }
        }
    }
}
