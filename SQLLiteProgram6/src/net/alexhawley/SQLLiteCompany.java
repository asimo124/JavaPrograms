package net.alexhawley;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

/**
 * Created by dev5 on 7/20/2016.
 */
public class SQLLiteCompany {

    public void SQLLiteCompany() {

    }
    public void insertRecordPrompt() {
        Scanner scan = new Scanner(System.in);
        String id = "";
        int id2 = 0;
        System.out.print("<Enter ID:># " );
        while (true) {
            id = scan.nextLine();
            try {
                id2 = Integer.parseInt(id);
            } catch (NumberFormatException ex) {
                System.out.print("(Error: That is not a number. Please re-enter id:) ");
                continue;
            }
            break;
        }
        System.out.print("<Enter Name:># ");
        String name = scan.nextLine();
        System.out.print("<Enter State: (Default is [TX]):># ");
        String state = scan.nextLine();
        if (state.equals("")) {
            state = "TX";
        }
        String age = "";
        int age2 = 0;
        System.out.print("<Enter Age:># " );
        while (true) {
            age = scan.nextLine();
            try {
                age2 = Integer.parseInt(age);
            } catch (NumberFormatException ex) {
                System.out.print("(Error: That is not a number. Please re-enter age:) ");
                continue;
            }
            break;
        }
        String salary = "";
        System.out.print("<Enter Salary:># ");
        float salary2 = 0;
        while (true) {
            salary = scan.nextLine();
            try {
                salary2 = Float.parseFloat(salary);
            } catch (NumberFormatException ex) {
                System.out.print("(Error: That is not a number. Please re-enter Salary without commas, dollar signs:) ");
            }
            break;
        }
        BigDecimal salary3 = BigDecimal.valueOf(salary2).setScale(2,BigDecimal.ROUND_HALF_UP);
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:test.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");
            stmt = c.createStatement();
            String sql = String.format("INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) " +
                    "VALUES (%d, '%s', %d, '%s', %s);", id2, name, age2, state, salary3.toString());
            stmt.executeUpdate(sql);
            stmt.close();
            c.commit();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Record inserted");
    }
    public void deleteRecord(int id) {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:test.db");
            c.setAutoCommit(false);
            stmt = c.createStatement();
            String sql = String.format("DELETE from COMPANY where ID=%d;", id);
            stmt.executeUpdate(sql);
            c.commit();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        showRecords();
    }
    public void showRecords() {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:test.db");
            c.setAutoCommit(false);
            System.out.println("+-----------------------------------------------------------------------+");
            System.out.format("| %4s | %20s | %20s | %4s | %9s |", "ID", "Name", "Address", "Age", "Salary");
            System.out.println();
            System.out.println("+-----------------------------------------------------------------------+");
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
                System.out.println("+-----------------------------------------------------------------------+");
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    }
}
