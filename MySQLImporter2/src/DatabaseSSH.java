
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by AHawley on 3/15/2017.
 */
public class DatabaseSSH extends Database {

    public boolean isConnected = false;
    private Channel channel;

    private static String SSH_PORT = "22";
    private static int SSH_PORT_NUMBER = 22;
    private static String DATABASE = "databasename";
    private static String DATABASE_USER = "databaseuser";
    private static String DATABASE_PASSWORD = "databasepasswd";
    private static String DATABASE_HOST = "databasehost";
    private static String SSH_HOST = "XX.XX.XX.XX";

    public SourceTargetModel SourceTarget = new SourceTargetModel();
    public DatabaseType databaseType = DatabaseType.SOURCE;

    /**
     * constructor
     */
    public DatabaseSSH(SourceTargetModel GetSourceTarget, DatabaseType getDatabaseType) {
        super(GetSourceTarget, getDatabaseType);

        this.SourceTarget = GetSourceTarget;
        databaseType = getDatabaseType;
    }

    /**
     * Setters and Getters
     */
    public static int getSshPortNumber() {
        return SSH_PORT_NUMBER;
    }
    public static String getSshPort() {
        return SSH_PORT;
    }
    public static void setSshPort(String sshPort) {
        int sshPortNumber = 22;
        if (sshPort == "") {
            sshPort = "22";
            sshPortNumber = 22;
        } else {
            int getPort = Integer.parseInt(sshPort);
            if (getPort < 1) {
                sshPort = "22";
                sshPortNumber = 22;
            }
        }
        SSH_PORT = sshPort;
        SSH_PORT_NUMBER = sshPortNumber;
    }

    public static String getDATABASE() {
        return DATABASE;
    }

    public static void setDATABASE(String DATABASE) {
        DatabaseSSH.DATABASE = DATABASE;
    }

    public static String getDatabaseUser() {
        return DATABASE_USER;
    }

    public static void setDatabaseUser(String databaseUser) {
        DATABASE_USER = databaseUser;
    }

    public static String getDatabasePassword() {
        return DATABASE_PASSWORD;
    }

    public static void setDatabasePassword(String databasePassword) {
        DATABASE_PASSWORD = databasePassword;
    }

    public static String getDatabaseHost() {
        return DATABASE_HOST;
    }

    public static void setDatabaseHost(String databaseHost) {
        DATABASE_HOST = databaseHost;
    }

    public static String getSshHost() {
        return SSH_HOST;
    }

    public static void setSshHost(String sshHost) {
        SSH_HOST = sshHost;
    }

    /**
     * Create SSH Session using JSch Library
     *
     * @return
     * @throws JSchException
     */
    private Session createSshSession() throws JSchException {

        String getSshUsername = "";
        String getSshHost = "";
        int getSshPortNum = 22;
        String getSshPassword = "";

        if (databaseType == DatabaseType.SOURCE) {
            getSshUsername = SourceTarget.getSourceSSHUsername();
            getSshHost = SourceTarget.getSourceSSHHost();
            getSshPortNum = SourceTarget.getSourceSSHPortNum();
            getSshPassword = SourceTarget.getSourceSSHPassword();
        } else {
            getSshUsername = SourceTarget.getTargetSSHUsername();
            getSshHost = SourceTarget.getTargetSSHHost();
            getSshPortNum = SourceTarget.getTargetSSHPortNum();
            getSshPassword = SourceTarget.getTargetSSHPassword();
        }

        Session session = new JSch().getSession(getSshUsername, getSshHost, getSshPortNum);
        session.setPassword(getSshPassword);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();
        return session;
    }

    /**
     * Set Source DatabaseSSH Connection (no port)
     */
    public void setConn() {

        String getSshUsername = "";
        String getSshHost = "";
        int getSshPortNum = 22;
        String getSshPassword = "";

        if (databaseType == DatabaseType.SOURCE) {

            this.DATABASE = SourceTarget.getSourceDBName();
            this.SSH_HOST = SourceTarget.getSourceSSHHost();
            this.setSshPort(SourceTarget.getSourceSSHPort());
            this.DATABASE_USER = SourceTarget.getSourceDBUsername();
            this.DATABASE_PASSWORD = SourceTarget.getSourceDBPassword();
        } else {
            this.DATABASE = SourceTarget.getTargetDBName();
            this.SSH_HOST = SourceTarget.getTargetSSHHost();
            this.setSshPort(SourceTarget.getTargetSSHPort());
            this.DATABASE_USER = SourceTarget.getTargetDBUsername();
            this.DATABASE_PASSWORD = SourceTarget.getTargetDBPassword();
        }
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
     * shortcut function to both set source connection, and attempt to connect to db,
     * if returns "SUCESS" then jsch session was established
     *
     * @return
     */
    public String setConnect() {
        this.setConn();
        return this.connectToDB();
    }

    /**
     * shortcut function to both connect to db, and then request the results
     * of a query
     *
     * @return List<Map<String, String>>
     */
    public List<Map<String, String>> connectAndGetQuery(String query) throws SQLException {

        List<Map<String, String>> resultset = new ArrayList<>();
        this.setConn();
        String retVal = this.connectToDB();
        if (retVal == "SUCCESS") {
            resultset = this.getQuery(query);
        }
        return resultset;
    }

    /**
     * Fhrt eine SQL-Abfrage aus und liefert eine Tabelle der Ergebnisse
     * @param query Der SQL-Code
     * @return Tabelle der Ergebnisse. Erste Dimension = Zeile; zweite Dimension = Spalte
     * @throws SQLException Tritt auf, wenn der SQL-Befehl eine Fehler auslste. (Fehler wird per System.err.println(...) ausgegeben)
     */
    public List<Map<String, String>> getQuery(String query) throws SQLException {

        List<Map<String, String>> resultset = new ArrayList<>();
        try {
            BufferedReader in=new BufferedReader(new InputStreamReader(channel.getInputStream()));

            if (databaseType == DatabaseType.SOURCE) {
                ((ChannelExec) channel).setCommand("mysql -u" + SourceTarget.getSourceDBUsername() + " -p" + SourceTarget.getSourceDBPassword() + " -h" + SourceTarget.getSourceDBHost() + " -e'" + query + "' " + SourceTarget.getSourceDBName());
            } else {
                ((ChannelExec) channel).setCommand("mysql -u" + SourceTarget.getTargetDBUsername() + " -p" + SourceTarget.getTargetDBPassword() + " -h" + SourceTarget.getTargetDBHost() + " -e'" + query + "' " + SourceTarget.getTargetDBName());
            }
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
        } catch (JSchException | IOException ex) {
            System.out.println(ex.getMessage());
        }
        return resultset;
    }

    /**
     * shortcut function to both connect to db, and then run and update/insert/delete query,
     * which returns no results
     */
    public void connectAndExecQuery(String query) throws SQLException {

        this.setConn();
        String retVal = this.connectToDB();
        if (retVal == "SUCCESS") {
            this.execQuery(query);
        }
    }

    /**
     * Fhrt eine SQL-Abfrage aus und liefert eine Tabelle der Ergebnisse
     * @param query Der SQL-Code
     * @return Tabelle der Ergebnisse. Erste Dimension = Zeile; zweite Dimension = Spalte
     * @throws SQLException Tritt auf, wenn der SQL-Befehl eine Fehler auslste. (Fehler wird per System.err.println(...) ausgegeben)
     */
    public void execQuery(String query) throws SQLException {

        try {
            BufferedReader in=new BufferedReader(new InputStreamReader(channel.getInputStream()));
            if (databaseType == DatabaseType.SOURCE) {
                ((ChannelExec) channel).setCommand("mysql -u" + SourceTarget.getSourceDBUsername() + " -p" + SourceTarget.getSourceDBPassword() + " -h" + SourceTarget.getSourceDBHost() + " -e'" + query + "' " + SourceTarget.getSourceDBName());
            } else {
                ((ChannelExec) channel).setCommand("mysql -u" + SourceTarget.getTargetDBUsername() + " -p" + SourceTarget.getTargetDBPassword() + " -h" + SourceTarget.getTargetDBHost() + " -e'" + query + "' " + SourceTarget.getTargetDBName());
            }
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
}
