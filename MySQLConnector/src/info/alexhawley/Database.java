
package info.alexhawley;

import info.alexhawley.types.StringHashMap;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @class Database
 * @author AMH 2017.50.26
 *
 * class to manage MySQL Database Connections, Queries
 */
public class Database {

    public Database() { }

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

    /**
     * function to convert MySQL resultset
     * assumed to only have 1 row, to a HashMap Array
     *
     * @param rs
     * @return
     */
    public static StringHashMap singleResultToHash(ResultSet rs) {
        StringHashMap eachItem = new StringHashMap();
        try {
            ResultSetMetaData meta = rs.getMetaData();
            final int columnCount = meta.getColumnCount();
            if (rs.next()) {
                for (int column = 1; column <= columnCount; column++) {
                    Object value = rs.getObject(column);
                    eachItem.put(meta.getColumnName(column), String.valueOf(value));
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return eachItem;
    }
}
