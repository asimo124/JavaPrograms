import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * Created by AHawley on 3/28/2017.
 */
public class DatabaseLocal extends Database {

    public boolean isConnected = false;
    private static String DATABASE = "databasename";
    private static String DATABASE_USER = "databaseuser";
    private static String DATABASE_PASSWORD = "databasepasswd";
    private static String DATABASE_HOST = "databasehost";

    public SourceTargetModel SourceTarget = new SourceTargetModel();
    public DatabaseType databaseType = DatabaseType.SOURCE;

    public Connection conn = null;

    /**
     * constructor
     */
    public DatabaseLocal(SourceTargetModel GetSourceTarget, DatabaseType getDatabaseType) {
        super(GetSourceTarget, getDatabaseType);
        this.SourceTarget = GetSourceTarget;
        databaseType = getDatabaseType;
    }

    /**
     * Set Source DatabaseSSH Connection (no port)
     */
    public Connection setConn() {

        Connection getConn = null;
        try {
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            conn = DriverManager.getConnection("jdbc:mysql://" + SourceTarget.getSourceDBHost() + ":3306/" + SourceTarget.getSourceDBName() + "?" + "user=" + SourceTarget.getSourceDBUsername() + "&password=" + SourceTarget.getSourceDBPassword() + "&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&failOverReadOnly=false&maxReconnects=10");
            getConn = conn;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return getConn;
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
