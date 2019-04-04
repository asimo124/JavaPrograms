package info.alexhawley;

import com.kodehelp.sftp.ListRecursiveFolderSFTP;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

    String host = "";
    String Username = "";
    String Password = "";
    String RemoteDir = "";
    String LocalDir = "";

    public static boolean showFiles = false;
    public static List<String> uniqueFolderPaths = new ArrayList<String>();

    int port = 22;

    public static void main(String[] args) {

        Main main = new Main();
        main.loadConfig();

        ListRecursiveFolderSFTP Files = new ListRecursiveFolderSFTP(main.host, main.Username, main.Password, main.RemoteDir, main.LocalDir, main.port);

        if (args.length > 0) {
            showFiles = true;
        }

        Collections.sort(uniqueFolderPaths);

        for (String filePath: uniqueFolderPaths) {
            System.out.println(filePath);
        }

        /*/
        String server = "172.24.16.23";
        int port = 21;
        String user = "claimaticui";
        String pass = "redpanther45!@";
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalActiveMode();
            System.out.println("Connected");
            String saveDirPath = "C:/Users/AHawley/Documents/Claimatic-Iteration-Group";

            String remoteDirPath = "/app";
            DownloadFTP.downloadDirectory(ftpClient, remoteDirPath, "", saveDirPath);

            remoteDirPath = "/src";
            DownloadFTP.downloadDirectory(ftpClient, remoteDirPath, "", saveDirPath);

            remoteDirPath = "/web";
            DownloadFTP.downloadDirectory(ftpClient, remoteDirPath, "", saveDirPath);

            ftpClient.logout();
            ftpClient.disconnect();
            System.out.println("Disconnected");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        //*/
    }

    public void loadConfig() {

        /*/
        this.host = "192.168.40.23";
        this.Username = "xfloyd";
        this.Password = "fish90!";
        this.RemoteDir = "/var/www/sites/dev.claimatic.com/httpdocs";
        this.LocalDir = "";
        this.port = 22;

        /*/
        String file = "C:\\Temp\\fine_unique_paths_config.txt";
        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            for(String line; (line = br.readLine()) != null; ) {
                String[] lineArr = line.split("\t");

                if (lineArr.length > 1) {
                    switch (lineArr[0])
                    {
                        case "host":
                            this.host = lineArr[1];
                            break;
                        case "username":
                            this.Username = lineArr[1];
                            break;
                        case "password":
                            this.Password = lineArr[1];
                            break;
                        case "remote_directory":
                            this.RemoteDir = lineArr[1];
                            break;
                        case "local_directory":
                            this.LocalDir = lineArr[1];
                            break;
                        case "port":
                            this.port = Integer.parseInt(lineArr[1]);
                            break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        //*/
    }
}
