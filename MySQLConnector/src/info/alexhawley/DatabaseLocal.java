package info.alexhawley;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import info.alexhawley.types.StringHashMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 *
 */
public class DatabaseLocal extends Database {

    public boolean isConnected = false;
    private String databaseName = "databasename";
    private String databaseUser = "databaseuser";
    private String databasePassword = "databasepasswd";
    private String databaseHost = "databasehost";
    private String connectionName = "";
    public Connection conn = null;

    public DatabaseLocal() {
        super();
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
     * Set Connection
     */
    public Connection setConn() {

        Connection getConn = null;
        try {
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            conn = DriverManager.getConnection("jdbc:mysql://" + this.getDatabaseHost() + ":3306/" + this.getDatabaseName() + "?" + "user=" + this.getDatabaseUser() + "&password=" + this.getDatabasePassword() + "&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&failOverReadOnly=false&maxReconnects=10");
            getConn = conn;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return getConn;
    }

    public int saveConnection(DatabaseLocal getDatabase) {
        Calendar calendar = Calendar.getInstance();
        java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());

        String query = "INSERT INTO connections " +
                        "(connection_name, host, db_name, db_username, db_password, entrydate) VALUES " +
                        "(?,               ?,    ?,       ?,           ?,           ?) ";

        PreparedStatement preparedStmt = null;
        try {
            preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        try {
            preparedStmt.setString(1, getDatabase.getConnectionName());
            preparedStmt.setString(2, getDatabase.getDatabaseHost());
            preparedStmt.setString(3, getDatabase.getDatabaseName());
            preparedStmt.setString(4, getDatabase.getDatabaseUser());
            preparedStmt.setString(5,getDatabase.getDatabasePassword());
            preparedStmt.setDate(6, startDate);
            preparedStmt.execute();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        int last_inserted_id = 0;
        try {
            ResultSet rs = preparedStmt.getGeneratedKeys();
            if (rs.next()) {
                last_inserted_id = rs.getInt(1);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println("last inserted id: " + last_inserted_id);
        return last_inserted_id;
    }


    public int saveConnection(DatabaseSSH getDatabase) {
        Calendar calendar = Calendar.getInstance();
        java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());

        DatabaseLocal ds = new DatabaseLocal();

        String query = "INSERT INTO connections " +
                "(connection_name, host, db_name, db_username, db_password, entrydate, is_ssh_tunnel, ssh_host, ssh_username, ssh_password, ssh_port) VALUES " +
                "(?,               ?,    ?,       ?,           ?,           ?,         1,             ?,        ?,            ?,            ?) ";

        PreparedStatement preparedStmt = null;
        try {
            preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        try {
            preparedStmt.setString(1, getDatabase.getConnectionName());
            preparedStmt.setString(2, getDatabase.getDatabaseHost());
            preparedStmt.setString(3, getDatabase.getDatabaseName());
            preparedStmt.setString(4, getDatabase.getDatabaseUser());
            preparedStmt.setString(5,getDatabase.getDatabasePassword());
            preparedStmt.setDate(6, startDate);
            preparedStmt.setString(7, getDatabase.getSshHost());
            preparedStmt.setString(8, getDatabase.getSshUsername());
            preparedStmt.setString(9, getDatabase.getSshPassword());
            preparedStmt.setInt(10, getDatabase.getSshPort());
            preparedStmt.execute();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        int last_inserted_id = 0;
        try {
            ResultSet rs = preparedStmt.getGeneratedKeys();
            if (rs.next()) {
                last_inserted_id = rs.getInt(1);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println("last inserted id: " + last_inserted_id);
        return last_inserted_id;
    }

    private String handleCommas(String field) {

        return field.replace("'", "''");
    }


    public void setParamsFromHash(StringHashMap fieldsArr) {

        this.setDatabaseName(fieldsArr.get("db_name"));
        this.setDatabaseUser(fieldsArr.get("db_username"));
        this.setDatabasePassword(fieldsArr.get("db_password"));
        this.setDatabaseHost(fieldsArr.get("host"));
    }

    /*/
    public ResultSet getQuery(String query) throws SQLException {
        //Statement st = conn.createStatement();
        //ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            int id = rs.getInt("id");
            String firstName = rs.getString("first_name");
            String lastName = rs.getString("last_name");
            Date dateCreated = rs.getDate("date_created");
            boolean isAdmin = rs.getBoolean("is_admin");
            int numPoints = rs.getInt("num_points");
            System.out.format("%s, %s, %s, %s, %s, %s\n", id, firstName, lastName, dateCreated, isAdmin, numPoints);
        }
        //st.close();
    }
    public void execQuery(String query) throws SQLException {
        Calendar calendar = Calendar.getInstance();
        java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        preparedStmt.setString (1, "Barney");
        preparedStmt.setString (2, "Rubble");
        preparedStmt.setDate   (3, startDate);
        preparedStmt.setBoolean(4, false);
        preparedStmt.setInt    (5, 5000);
        preparedStmt.execute();
    }
    //*/
}