
package info.alexhawley;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 *
 */
public class ConfigLoader {

    public String ssh_host = "";
    public String ssh_username = "";
    public String ssh_password = "";
    public String ssh_port = "22";
    public String db_host = "";
    public String db_username = "";
    public String db_password = "";
    public String db_name = "";

    public String getSsh_host() {
        return ssh_host;
    }

    public void setSsh_host(String ssh_host) {
        this.ssh_host = ssh_host;
    }

    public String getSsh_username() {
        return ssh_username;
    }

    public void setSsh_username(String ssh_username) {
        this.ssh_username = ssh_username;
    }

    public String getSsh_password() {
        return ssh_password;
    }

    public void setSsh_password(String ssh_password) {
        this.ssh_password = ssh_password;
    }

    public String getSsh_port() {
        return ssh_port;
    }

    public void setSsh_port(String ssh_port) {
        this.ssh_port = ssh_port;
    }

    public String getDb_host() {
        return db_host;
    }

    public void setDb_host(String db_host) {
        this.db_host = db_host;
    }

    public String getDb_username() {
        return db_username;
    }

    public void setDb_username(String db_username) {
        this.db_username = db_username;
    }

    public String getDb_password() {
        return db_password;
    }

    public void setDb_password(String db_password) {
        this.db_password = db_password;
    }

    public String getDb_name() {
        return db_name;
    }

    public void setDb_name(String db_name) {
        this.db_name = db_name;
    }

    public ConfigLoader() {

        File file = new File("C:\\Temp\\java_mysqlconnector_config.txt");
        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            for (String line; (line = br.readLine()) != null; ) {

                String[] lineArr = line.split("\\t");

                if (lineArr.length > 1) {
                    switch (lineArr[0]) {
                        case "ssh_host":
                            this.setSsh_host(lineArr[1]);
                            break;
                        case "ssh_username":
                            this.setSsh_username(lineArr[1]);
                            break;
                        case "ssh_password":
                            this.setSsh_password(lineArr[1]);
                            break;
                        case "ssh_port":
                            this.setSsh_port(lineArr[1]);
                            break;
                        case "db_host":
                            this.setDb_host(lineArr[1]);
                            break;
                        case "db_username":
                            this.setDb_username(lineArr[1]);
                            break;
                        case "db_password":
                            this.setDb_password(lineArr[1]);
                            break;
                        case "db_name":
                            this.setDb_name(lineArr[1]);
                            break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public ConfigLoader(String filePath) {

        File file = new File(filePath);
        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            for (String line; (line = br.readLine()) != null; ) {

                String[] lineArr = line.split("\\t");
                if (lineArr.length > 1) {
                    switch (lineArr[0]) {
                        case "ssh_host":
                            this.setSsh_host(lineArr[1]);
                            break;
                        case "ssh_username":
                            this.setSsh_username(lineArr[1]);
                            break;
                        case "ssh_password":
                            this.setSsh_password(lineArr[1]);
                            break;
                        case "ssh_port":
                            this.setSsh_port(lineArr[1]);
                            break;
                        case "db_host":
                            this.setDb_host(lineArr[1]);
                            break;
                        case "db_username":
                            this.setDb_username(lineArr[1]);
                            break;
                        case "db_password":
                            this.setDb_password(lineArr[1]);
                            break;
                        case "db_name":
                            this.setDb_name(lineArr[1]);
                            break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void printProps() {

        System.out.println("ssh_host: " + this.getSsh_host());
        System.out.println("ssh_username: " + this.getSsh_username());
        System.out.println("ssh_password: " + this.getSsh_password());
        System.out.println("ssh_port: " + this.getSsh_port());
        System.out.println("db_host: " + this.getDb_host());
        System.out.println("db_username: " + this.getDb_username());
        System.out.println("db_password: " + this.getDb_password());
        System.out.println("db_name: " + this.getDb_name());
    }
}
