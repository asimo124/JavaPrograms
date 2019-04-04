package net.alexhawley;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

/**
 * Created by dev5 on 7/14/2016.
 */
public class SQLLiteCompany {

    public void SQLLiteCompany() {

    }

    public void insertRecordPrompt() {

        System.out.print("Enter ID > ");
        Scanner scan = new Scanner(System.in);
        String id = scan.nextLine();
    }

    public void showRecords() {

        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:test.db");

            c.setAutoCommit(false);
            /*/stmt = c.createStatement();
            String sql = "CREATE TABLE COMPANY " +
                    "(ID INT PRIMARY KEY     NOT NULL," +
                    " NAME           TEXT    NOT NULL, " +
                    " AGE            INT     NOT NULL, " +
                    " ADDRESS        CHAR(50), " +
                    " SALARY         REAL)";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
            stmt = c.createStatement();
            String sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) " +
                    "VALUES (2, 'Steve', 38, 'Texas', 32000.00 );";
            stmt.executeUpdate(sql);//*/

            System.out.println("/-----------------------------------------------------------------------\\");
            System.out.format("| %4s | %20s | %20s | %4s | %9s |", "ID", "Name", "Address", "Age", "Salary");
            System.out.println();
            System.out.println("|-----------------------------------------------------------------------|");
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM COMPANY;" );
            while ( rs.next() ) {
                int id = rs.getInt("id");
                String  name = rs.getString("name");
                int age  = rs.getInt("age");
                String  address = rs.getString("address");
                float salary = rs.getFloat("salary");
                BigDecimal salary2 = BigDecimal.valueOf(salary).setScale(2,BigDecimal.ROUND_HALF_UP);
                String salary3 = salary2.toString();
                System.out.format("| %4d | %20s | %20s | %4d | %9s |", id, name, address, age, salary3);
                System.out.println();
                System.out.println("|-----------------------------------------------------------------------|");
            }
            rs.close();
            stmt.close();
            c.commit();
            c.close();
        } catch ( Exception e ) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
}
