package info.alexhawley;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        putContent("rec_rap.php", "rap_content", line_str);
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
        putContent("rec_indie.php", "indie_content", line_str2);
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
        putContent("rec_metal.php", "metal_content", line_str3);
        System.out.println("Music updated.");
    }

    static void putContent(String action, String contentKey, String lineContent) {

        URL url = null;
        try {
            url = new URL("http://alexhawley.info/" + action);
        } catch (MalformedURLException e) {
            System.out.println(e.getMessage());
        }
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        try {
            con.setRequestMethod("POST");
        } catch (ProtocolException e) {
            System.out.println(e.getMessage());
        }
        Map<String, String> parameters = new HashMap<>();
        parameters.put(contentKey, lineContent);
        con.setDoOutput(true);
        DataOutputStream out = null;
        try {
            out = new DataOutputStream(con.getOutputStream());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        try {
            try {
                out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
            } catch (UnsupportedEncodingException e) {
                System.out.println(e.getMessage());
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        try {
            out.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        try {
            out.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        try {
            int status = con.getResponseCode();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        String inputLine;
        StringBuffer content = new StringBuffer();
        try {
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        try {
            in.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        con.disconnect();
    }
}
