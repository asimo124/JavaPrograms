package info.alexhawley;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import info.alexhawley.types.StringHashMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.NoRouteToHostException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * Created by AHawley on 3/15/2017.
 */
public class DatabaseSSH extends Database {

    public boolean isConnected = false;
    private Channel channel;

    private String sshHost = "sshhost";
    private int sshPort = 22;
    private String sshUsername = "sshusername";
    private String sshPassword = "sshpassword";

    private String databaseName = "databasename";
    private String databaseUser = "databaseuser";
    private String databasePassword = "databasepasswd";
    private String databaseHost = "databasehost";
    private String connectionName = "";

    public DatabaseSSH() {
        super();
    }

    public String getSshHost() {
        return sshHost;
    }
    public void setSshHost(String sshHost) {
        this.sshHost = sshHost;
    }
    public void setSshPort(int sshPort) {
        this.sshPort = sshPort;
    }
    public int getSshPort() { return this.sshPort; }
    public String getSshUsername() {
        return sshUsername;
    }
    public void setSshUsername(String sshUsername) {
        this.sshUsername = sshUsername;
    }
    public String getSshPassword() {
        return sshPassword;
    }
    public void setSshPassword(String sshPassword) {
        this.sshPassword = sshPassword;
    }

    public String getDatabaseName() {
        return databaseName;
    }
    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }
    public String getDatabaseUser() {
        return databaseUser;
    }
    public void setDatabaseUser(String databaseUser) {
        this.databaseUser = databaseUser;
    }
    public String getDatabasePassword() {
        return databasePassword;
    }
    public void setDatabasePassword(String databasePassword) {
        this.databasePassword = databasePassword;
    }
    public String getDatabaseHost() {
        return databaseHost;
    }
    public void setDatabaseHost(String databaseHost) {
        this.databaseHost = databaseHost;
    }
    public String getConnectionName() {
        return connectionName;
    }
    public void setConnectionName(String connectionName) {
        this.connectionName = connectionName;
    }

    /**
     * Create SSH Session using JSch Library
     *
     * @return
     * @throws JSchException
     */
    private Session createSshSession() throws JSchException {

        System.out.println("ssh username: " + this.getSshUsername() + " SshHost: " + this.getSshHost() + " port: " + this.getSshPort() + " password: " + this.getSshPassword());

        Session session = null;


        session = new JSch().getSession(this.getSshUsername(), this.getSshHost(), this.getSshPort());
        session.setPassword(this.getSshPassword());
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();

        System.out.println("is connected: " + session.isConnected());


        return session;
    }

    /**
     * create ssh session, to prepare for db connection
     *
     * @return
     */
    public String connectToDB() {

        try {
            Session sshSession = createSshSession();
            this.channel = sshSession.openChannel("exec");
            ((ChannelExec) channel).setErrStream(System.err);
            isConnected = true;
            return "SUCCESS";
        } catch (JSchException ex) {
            return ex.getMessage();
        }
    }



    /**
     * shortcut function to both connect to db, and then request the results
     * of a query
     *
     * @return List<Map<String, String>>
     */
    public List<StringHashMap> connectAndGetQuery(String query) throws SQLException {

        List<StringHashMap> resultset = new ArrayList<>();
        String retVal = this.connectToDB();
        if (retVal == "SUCCESS") {
            resultset = this.getQuery(query);
        }
        return resultset;
    }



    public boolean checkConnectDB() throws SQLException {

        String query = "SHOW TABLES;";
        List<Map<String, String>> resultset = new ArrayList<>();

        System.out.println("mysql -u" + this.getDatabaseUser() + " -p" + this.getDatabasePassword() + " -h" + this.getDatabaseHost()+ " -e'" + query + "' " + this.getDatabaseName());

        try {
            BufferedReader in=new BufferedReader(new InputStreamReader(channel.getInputStream()));

            ((ChannelExec) channel).setCommand("mysql -u" + this.getDatabaseUser() + " -p" + this.getDatabasePassword() + " -h" + this.getDatabaseHost()+ " -e'" + query + "' " + this.getDatabaseName());

            channel.connect();
            int i = 0;
            String msg=null;
            int t = 0;
            List<String> colsArr = new ArrayList<>();
            while((msg=in.readLine())!=null){
                List<String> fieldsArr = Arrays.asList(msg.split("\t"));
                if (i == 0) {
                    for (String item: fieldsArr) {
                        colsArr.add(item);
                    }
                } else {
                    int j = 0;
                    Map<String, String> fields = new LinkedHashMap<String, String>();
                    for (String item: fieldsArr) {

                        fields.put(colsArr.get(j), item);
                        j++;
                    }
                    resultset.add(fields);
                    t++;
                }
                i++;
            }
            channel.disconnect();
            channel.disconnect();
            return true;
        } catch (JSchException | IOException ex) {
            System.out.println(ex.getMessage());
            return false;
        }

    }


    /**
     *
     * @param query
     * @return
     * @throws SQLException
     */
    public List<StringHashMap> getQuery(String query) throws SQLException {

        System.out.println("mysql -u" + this.getDatabaseUser() + " -p" + this.getDatabasePassword() + " -h" + this.getDatabaseHost()+ " -e'" + query + "' " + this.getDatabaseName());

        List<StringHashMap> resultset = new ArrayList<>();
        try {
            BufferedReader in=new BufferedReader(new InputStreamReader(channel.getInputStream()));

            ((ChannelExec) channel).setCommand("mysql -u" + this.getDatabaseUser() + " -p" + this.getDatabasePassword() + " -h" + this.getDatabaseHost()+ " -e'" + query + "' " + this.getDatabaseName());

            channel.connect();
            int i = 0;
            String msg=null;
            int t = 0;
            List<String> colsArr = new ArrayList<>();
            while((msg=in.readLine())!=null){
                List<String> fieldsArr = Arrays.asList(msg.split("\t"));
                if (i == 0) {
                    for (String item: fieldsArr) {
                        colsArr.add(item);
                    }
                } else {
                    int j = 0;
                    StringHashMap fields = new StringHashMap();
                    for (String item: fieldsArr) {

                        fields.put(colsArr.get(j), item);
                        j++;
                    }
                    resultset.add(fields);
                    t++;
                }
                i++;
            }
            channel.disconnect();
            channel.disconnect();
        } catch (JSchException | IOException ex) {
            System.out.println(ex.getMessage());
        }
        return resultset;
    }

    public void setParamsFromHash(StringHashMap fieldsArr) {

        this.setDatabaseName(fieldsArr.get("db_name"));
        this.setDatabaseUser(fieldsArr.get("db_username"));
        this.setDatabasePassword(fieldsArr.get("db_password"));
        this.setDatabaseHost(fieldsArr.get("host"));
        this.setSshHost(fieldsArr.get("ssh_host"));
        this.setSshUsername(fieldsArr.get("ssh_username"));
        this.setSshPassword(fieldsArr.get("ssh_password"));
        this.setSshPort(Integer.parseInt(fieldsArr.get("ssh_port")));
    }

    private String handleCommas(String field) {

        return field.replace("'", "''");
    }


    /**
     * shortcut function to both connect to db, and then run and update/insert/delete query,
     * which returns no results
     */
    public void connectAndExecQuery(String query) throws SQLException {

        String retVal = this.connectToDB();
        if (retVal == "SUCCESS") {
            this.execQuery(query);
        }
    }

    /**
     *
     * @param query
     * @throws SQLException
     */
    public void execQuery(String query) throws SQLException {

        try {
            BufferedReader in=new BufferedReader(new InputStreamReader(channel.getInputStream()));

            ((ChannelExec) channel).setCommand("mysql -u" + this.getDatabaseUser() + " -p" + this.getDatabasePassword() + " -h" + this.getDatabaseHost()+ " -e'" + query + "' " + this.getDatabaseName());

            channel.connect();
            String msg=null;
            while((msg=in.readLine())!=null){
                System.out.println(msg);
            }
            channel.disconnect();
            channel.disconnect();
        } catch (JSchException | IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void printProps() {

        /*/
        private String sshHost = "sshhost";
        private int sshPort = 22;
        private String sshUsername = "sshusername";
        private String sshPassword = "sshpassword";

        private String databaseName = "databasename";
        private String databaseUser = "databaseuser";
        private String databasePassword = "databasepasswd";
        private String databaseHost = "databasehost";
        private String connectionName = "";
        //*/

        System.out.println("Connection Name: " + this.getConnectionName());
        System.out.println("SSH Host: " + this.getSshHost());
        System.out.println("SSH Username: " + this.getSshUsername());
        System.out.println("SSH Password: " + this.getSshPassword());
        System.out.println("SSH Port: " + this.getSshPort());
        System.out.println("DatabaseName: " + this.getDatabaseName());
        System.out.println("DatabaseUser: " + this.getDatabaseUser());
        System.out.println("DatabasePassword: " + this.getDatabasePassword());
        System.out.println();
    }

}
