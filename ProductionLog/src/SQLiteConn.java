import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sqlitetutorial.net
 */
public class SQLiteConn {

    public static Connection conn = null;

    /**
     * Connect to a sample database
     */
    public static void connect() {
        try {
            // create a connection to the database
            String url = "jdbc:sqlite:C:/Temp/production_logs/sqlite_dbs/production_log11.db";
            conn = DriverManager.getConnection(url);
            conn.setAutoCommit(true);
        } catch(SQLException e){
            System.out.println(e.getMessage());
        } finally {
            //System.out.println("Connection to SQLite has been established.");
        }
    }

    /**
     * Run Import SQL if DB was just created
     */
    public static void importSQL() {
        // Check if database create script has been run
        Statement stmt = null;
        ResultSet rs = null;
                String sql = "SELECT * FROM prod_logs";
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {   // if create script not run yet
            // run create scripts
            String sql2 = "CREATE TABLE IF NOT EXISTS prod_logs (\n"
                    + "	id integer PRIMARY KEY,\n"
                    + "	site text NOT NULL,\n"
                    + "	change_desc text NOT NULL,\n"
                    + "	project text NULL,\n"
                    + "	date text NULL\n"
                    + ");";
            try {
                stmt = conn.createStatement();
                stmt.execute(sql2);
            } catch (SQLException e2) {
                System.out.println(e2.getMessage());
            }
            sql2 = "CREATE TABLE IF NOT EXISTS prod_log_files (\n"
                    + "	id integer PRIMARY KEY,\n"
                    + "	log_id integer NOT NULL,\n"
                    + "	file_path NOT NULL);";
            try {
                stmt = conn.createStatement();
                stmt.execute(sql2);
            } catch (SQLException e2) {
                System.out.println(e2.getMessage());
            }
        }
    }

    /**
     * function to convert MySQL resultset
     * to a List of HashMap arrays
     *
     * @param rs
     * @return
     */
    public static List<StringHashMap> resultsToList(ResultSet rs) {
        List<StringHashMap> retArr = new ArrayList<>();
        try {
            ResultSetMetaData meta = rs.getMetaData();
            final int columnCount = meta.getColumnCount();
            while (rs.next()) {
                StringHashMap eachItem = new StringHashMap();
                for (int column = 1; column <= columnCount; column++) {
                    Object value = rs.getObject(column);
                    eachItem.put(meta.getColumnName(column), String.valueOf(value));
                }
                retArr.add(eachItem);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return retArr;
    }
}