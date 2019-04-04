package info.alexhawley;


import info.alexhawley.types.StringHashMap;

import java.sql.*;
import java.util.*;

/**
 * @class Main
 * @author AMH 2017.05.26
 *
 * console application that allows user to
 * export tables to text file, and keep a database
 * manager of database connections, stored in
 * local mysql database
 */
public class Main {

    public static DatabaseLocal databaseTest = new DatabaseLocal();
    public static DatabaseSSH databaseTestSsh = new DatabaseSSH();
    public static DatabaseLocal databaseConnections = new DatabaseLocal();
    public static int connectionId = 0;

    public static void main(String[] args) {

        // Connect to Connections Manager Database
        connectToMain();
        Scanner reader = new Scanner(System.in);
        boolean usesSSH = false;
        while (true) {

            // Request Action from User, until they type quit
            System.out.println("What do you wish to do?");
            System.out.println("(L)ist Existing Connections, (C)reate A New Connection, or (Q)uit?");
            String action = reader.next();

            if (action.equals("l") || action.equals("L")) {  // if select "List" connections

                // Select Connections from Connections Manager
                List<StringHashMap> resultsArray = new ArrayList<>();
                String query = "SELECT id, connection_name, ssh_host, ssh_username, ssh_password, ssh_port, is_ssh_tunnel as is_ssh, host, db_name, " +
                               "db_username, db_password, entrydate " +
                               "FROM connections " +
                               "ORDER BY entrydate ";
                try {
                    int i = 0;
                    Statement st = databaseConnections.conn.createStatement();
                    ResultSet rs = st.executeQuery(query);
                    List<StringHashMap> Connections = Database.resultsToList(rs);
                    PrintUtilities.printTable(Connections);
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
                break;

            } else if (action.equals("c") || action.equals("C")) {  // if select "Create" Connection

                // Continually ask if connection requires SSH tunneling, until they type "Y" or "N"
                while (true) {
                    System.out.println("Does this connection use SSH Tunneling? (Y)es/(N)o");
                    String isSshTunnel = reader.next();
                    if (isSshTunnel.equals("Y") || isSshTunnel.equals("y")) {  // if yes
                        usesSSH = true;
                        break;
                    } else if (isSshTunnel.equals("N") || isSshTunnel.equals("n"))  {  // if no
                        break;
                    }
                }


                if (usesSSH == true) {  // if Connection requires SSH Tunneling

                    while (true) {

                        // Ask for connection information until they enter valid connection parameters
                        System.out.println("What is the SSH Host?");
                        String sshHost = reader.next();
                        System.out.println("What is the SSH Port?");
                        int sshPort = reader.nextInt();
                        System.out.println("What is the SSH Username?");
                        String sshUsername = reader.next();
                        System.out.println("What is the SSH Password?");
                        String sshPassword = reader.next();

                        databaseTestSsh.setSshHost(sshHost);
                        databaseTestSsh.setSshPort(sshPort);
                        databaseTestSsh.setSshUsername(sshUsername);
                        databaseTestSsh.setSshPassword(sshPassword);

                        System.out.println("What is the Database Host?");
                        String databaseHost = reader.next();
                        System.out.println("What is the Database Name?");
                        String databaseName = reader.next();
                        System.out.println("What is the Database Username?");
                        String databaseUsername = reader.next();
                        System.out.println("What is the Database Password?");
                        String databasePassword = reader.next();

                        databaseTestSsh.setDatabaseHost(databaseHost);
                        databaseTestSsh.setDatabaseName(databaseName);
                        databaseTestSsh.setDatabaseUser(databaseUsername);
                        databaseTestSsh.setDatabasePassword(databasePassword);

                        boolean didConnect = false;
                        String retVal = databaseTestSsh.connectToDB();
                        List<StringHashMap> results = new ArrayList<>();
                        if (retVal == "SUCCESS") {
                            didConnect = true;
                            try {
                                results = databaseTestSsh.getQuery("SHOW TABLES;");
                            } catch (SQLException ex) {
                                System.out.println(ex.getMessage());
                            }
                            if (results.size() == 0) {
                                System.out.println("No Tables Found");
                            }
                        }

                        if (didConnect) {

                            // output success and go back to original step 1 - Ask what Action to take
                            System.out.println("----------------------");
                            System.out.println("Connection successful.");
                            break;
                        } else {

                            // continue asking for valid connection parameters
                            System.out.println("Could not Connect");
                        }
                    }

                    System.out.println("Please give this connection a name.");
                    String connectionName = reader.next();
                    databaseTestSsh.setConnectionName(connectionName);

                    databaseConnections.saveConnection(databaseTestSsh);

                } else {   // if does not require SSH tunneling

                    while (true) {


                        // Ask for connection information until they enter valid connection parameters
                        System.out.println("What is the Database Host?");
                        String databaseHost = reader.next();
                        System.out.println("What is the Database Name?");
                        String databaseName = reader.next();
                        System.out.println("What is the Database Username?");
                        String databaseUsername = reader.next();
                        System.out.println("What is the Database Password?");
                        String databasePassword = reader.next();

                        databaseTest.setDatabaseHost(databaseHost);
                        databaseTest.setDatabaseName(databaseName);
                        databaseTest.setDatabaseUser(databaseUsername);
                        databaseTest.setDatabasePassword(databasePassword);

                        // Test for valid connection
                        Connection getConn = databaseTest.setConn();

                        if (getConn != null) {  // if valid connection

                            // output success and go back to original step 1 - Ask what Action to take
                            System.out.println("----------------------");
                            System.out.println("Connection successful.");
                            break;

                        } else {  // if invalid connection

                            // continue asking for valid connection parameters
                            System.out.println("Could not Connect");
                        }
                    }

                    // Request name for Connection
                    System.out.println("Please give this connection a name.");
                    String connectionName = reader.next();
                    databaseTest.setConnectionName(connectionName);

                    // Insert into Connections Manager Database
                    databaseConnections.saveConnection(databaseTest);
                    System.out.println("Connection Saved.");
                }

            } else {  // if type "Q" for quit

                // exit console application
                System.exit(1);
            }
        }

        // Continue after selecting "List" Connections
        StringHashMap EachConnection = new StringHashMap();

        // Ask them to enter an ID of the connection for exporting tables
        while (true) {
            System.out.println("Enter the ID of the Connection you wish to export tables for");
            int getId = reader.nextInt();

            // Select that Connection
            String query = "SELECT id, connection_name, ssh_host, ssh_username, ssh_password, ssh_port, is_ssh_tunnel, host, db_name, " +
                    "db_username, db_password, entrydate " +
                    "FROM connections " +
                    "WHERE id = ? LIMIT 1 ";
            PreparedStatement stmt = null;
            try {
                int i = 0;
                stmt = databaseConnections.conn.prepareStatement(query);
                stmt.setString(1, Integer.toString(getId));
                ResultSet rs = stmt.executeQuery();
                EachConnection = Database.singleResultToHash(rs);

                if (!EachConnection.isEmpty()) {  // if select valid ID

                    // move on to next step
                    break;
                }
                // otherwise, continue asking for ID of connection

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }

        if (!EachConnection.get("is_ssh_tunnel").equals("1")) {   // if NOT ssh tunnel connection

            // Show List of Tables for them to choose from
            DatabaseLocal database1 = new DatabaseLocal();
            database1.setParamsFromHash(EachConnection);
            database1.setConn();
            String query = "SHOW TABLES; ";
            try {
                Statement st = database1.conn.createStatement();
                ResultSet rs = st.executeQuery(query);
                List<StringHashMap> Tables = Database.resultsToList(rs);

                // Add an ID to Hash Table
                int i = 1;
                List<StringHashMap> Tables2 = new ArrayList<>();
                for (StringHashMap getTable : Tables) {
                    StringHashMap eachTable = new StringHashMap();
                    eachTable.put("ID", Integer.toString(i));
                    eachTable.put("TABLE_NAME", getTable.get("TABLE_NAME"));
                    Tables2.add(eachTable);
                    i++;
                }

                // Show Grid of Tables for this Database
                PrintUtilities.printTable(Tables2);

            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }

        } else {  // if using SSH

            DatabaseSSH database2 = new DatabaseSSH();
            database2.setParamsFromHash(EachConnection);

            database2.printProps();

            boolean didConnect = false;
            String retVal = database2.connectToDB();
            List<StringHashMap> results = new ArrayList<>();
            if (retVal == "SUCCESS") {
                didConnect = true;
                try {
                    results = database2.getQuery("SHOW TABLES;");
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
                if (results.size() == 0) {
                    System.out.println("No Tables Found");
                }
            }

            if (didConnect) {
                PrintUtilities.printTable(results);
            } else {

            }
        }
    }

    /**
     * Connect to Connections Manager DB
     */
    public static void connectToMain() {
        ConfigLoader config = new ConfigLoader();


        if (config.getSsh_password() == "") {
            databaseConnections.setDatabaseHost(config.getDb_host());
            databaseConnections.setDatabaseName(config.getDb_name());
            databaseConnections.setDatabaseUser(config.getDb_username());
            databaseConnections.setDatabasePassword(config.getDb_password());
            Connection getConnection = databaseConnections.setConn();
            if (getConnection == null) {
                System.out.println("Could not connect to main database from Config Loader.");
                System.exit(0);
            }
        }
    }
}