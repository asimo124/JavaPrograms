package info.alexhawley;

import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;

import java.io.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        Path wiki_path = Paths.get("C:/Users/AHawley/Music", "good_rap_bands_tracks.txt");

        String line_str = "";
        Charset charset = Charset.forName("ISO-8859-1");
        try {
            List<String> lines = Files.readAllLines(wiki_path, charset);
            for (String line : lines) {
                line_str += line + "\n";
            }

        } catch (IOException e) {
            System.out.println(e);
        }

        System.out.println(line_str);

        try {
            try {
                URL url = new URL("http://alexhawley.info/rec_rap.php?rap_content=" + URLEncoder.encode(line_str, "UTF-8"));

                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
                    for (String line; (line = reader.readLine()) != null; ) {
                        System.out.println(line);
                    }
                } catch (IOException e) {
                    System.out.println("error 1:" + e);
                }
            } catch (UnsupportedEncodingException e) {
                System.out.println("error 2: " + e);
            }



        } catch (MalformedURLException e) {
            System.out.println("error 3: " + e);
        }

        Path wiki_path2 = Paths.get("C:/Users/AHawley/Music", "good_indie_bands_tracks.txt");

        String line_str2 = "";
        try {
            List<String> lines = Files.readAllLines(wiki_path2, charset);
            for (String line : lines) {
                line_str2 += line + "\n";
            }

        } catch (IOException e) {
            System.out.println(e);
        }

        try {
            try {
                URL url = new URL("http://alexhawley.info/rec_indie.php?indie_content=" + URLEncoder.encode(line_str2, "UTF-8"));

                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
                    for (String line; (line = reader.readLine()) != null; ) {
                        System.out.println(line);
                    }
                } catch (IOException e) {
                    System.out.println("error 1:" + e);
                }
            } catch (UnsupportedEncodingException e) {
                System.out.println("error 2: " + e);
            }



        } catch (MalformedURLException e) {
            System.out.println("error 3: " + e);
        }



        Path wiki_path3 = Paths.get("C:/Users/AHawley/Music", "good_metal_bands_tracks.txt");

        String line_str3 = "";
        try {
            List<String> lines = Files.readAllLines(wiki_path3, charset);
            for (String line : lines) {
                line_str3 += line + "\n";
            }

        } catch (IOException e) {
            System.out.println(e);
        }

        try {
            try {
                URL url = new URL("http://alexhawley.info/rec_metal.php?metal_content=" + URLEncoder.encode(line_str3, "UTF-8"));

                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
                    for (String line; (line = reader.readLine()) != null; ) {
                        System.out.println(line);
                    }
                } catch (IOException e) {
                    System.out.println("error 1:" + e);
                }
            } catch (UnsupportedEncodingException e) {
                System.out.println("error 2: " + e);
            }



        } catch (MalformedURLException e) {
            System.out.println("error 3: " + e);
        }





    }
}
