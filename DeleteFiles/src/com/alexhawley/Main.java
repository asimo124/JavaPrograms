package com.alexhawley;

import java.io.*;
import java.nio.file.Files;

public class Main {

    public static void main(String[] args) {
	    // write your code here

        File[] files = new File("H:/Repos/OwnCloud_20160719/or-jsrc").listFiles();
        showFiles(files);
        //removeFilesFromTextfile();
    }

    public static void removeFilesFromTextfile() {

        String file = "C:/temp/Java/DeleteFiles/or_jsrc_same_paths.txt";
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            for(String line; (line = br.readLine()) != null; ) {
                String[] lineArr = line.split(" ");
                for (String getLine: lineArr) {
                    try {
                        File myFile = new File(getLine);
                        myFile.delete();
                    } catch (Exception e) {
                        continue;
                    }
                }
                break;
            }
            br.close();
        } catch (Exception e) {

        }
    }

    public static void showFiles(File[] files) {
        for (File file : files) {
            if (file.isDirectory()) {
                //System.out.println("Directory: " + file.getName());
                showFiles(file.listFiles()); // Calls same method again.
            } else {
                System.out.println("File: " + file.getName());

                String use_filename = file.getName();

                if (use_filename.indexOf(".swp") > -1 || (use_filename.indexOf(".php") == -1 && use_filename.indexOf(".htm") == -1
                        && use_filename.indexOf(".css") == -1 && use_filename.indexOf("js") == -1))
                {
                    file.delete();
                }


            }
        }
    }
}
