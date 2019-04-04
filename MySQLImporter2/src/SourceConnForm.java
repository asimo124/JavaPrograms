import info.alexhawley.ConfigLoader;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.*;

import java.util.*;

/**
 * Created by AHawley on 3/15/2017.
 */
public class SourceConnForm {
    private JButton btnSourceConnect;
    public JPanel panelMain;
    private JTextField text_SourceHost;
    private JTextField text_SourceUsername;
    private JTextField text_SourcePassword;
    private JTextField text_SourceSSHHost;
    private JTextField text_SourceSSHUsername;
    private JTextField text_SourceSSHPassword;
    private JTextField text_SourceSSHPort;
    private JTextField text_SourceDatabase;

    private JCheckBox chkSSHTunnelCheckBox;
    public JComboBox cboSelSourceDB;
    private JButton btnNext;
    public String source_db = "";
    public boolean isConnected = false;
    public Connection conn = null;

    public SourceTargetModel SourceTarget = new SourceTargetModel();
    public DatabaseSSH database_ssh = new DatabaseSSH(new SourceTargetModel(), DatabaseType.SOURCE);
    public DatabaseLocal database_local = new DatabaseLocal(new SourceTargetModel(), DatabaseType.SOURCE);

    public SourceConnForm() {

        /**
         * Connect Button
         */
        btnSourceConnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String retVal = "";
                // Use Config Loader to bypass test inputting
                int debug = 1;
                if (debug == 1) {
                    loadConfigSettings();
                }
                if (text_SourceDatabase.isEnabled()) {
                    source_db = text_SourceDatabase.getText();
                } else if (cboSelSourceDB.isEnabled()) {
                    source_db = cboSelSourceDB.getSelectedItem().toString();
                }
                SourceTarget.setSourceIsSSHTunnel(chkSSHTunnelCheckBox.isSelected());
                if (chkSSHTunnelCheckBox.isSelected()) {  // if SSH Tunneling
                    if (text_SourceSSHPort.getText().trim() == "") {
                        text_SourceSSHPort.setText("22");
                    }
                    setSourceModel();
                    database_ssh = new DatabaseSSH(SourceTarget, DatabaseType.SOURCE);
                    retVal = database_ssh.setConnect();
                    if (!retVal.equals("SUCCESS")) {
                        JOptionPane.showMessageDialog(null, "Could not connect: " + retVal);
                    } else {  // if connection is success
                        JOptionPane.showMessageDialog(null, "Successfully Connected");
                        fillDatabasesDropdown();
                        btnNext.setEnabled(true);
                    }
                } else {  // if Direct MySQL Connection
                    setSourceModel();
                    text_SourceSSHPort.setText("");
                    database_local = new DatabaseLocal(SourceTarget, DatabaseType.SOURCE);
                    conn = database_local.setConn();
                    isConnected = true;
                    JOptionPane.showMessageDialog(null, "Successfully Connected");
                    fillDatabasesDropdown();
                    btnNext.setEnabled(true);
                    try {
                        conn.close();
                    } catch (SQLException ex4) {
                        System.out.println(ex4.getMessage());
                    }
                }
            }
        });

        /**
         * SSH Tunnel Check Box - Change
         */
        chkSSHTunnelCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AbstractButton absB = (AbstractButton) e.getSource();
                ButtonModel bMod = absB.getModel();
                boolean selected = bMod.isSelected();
                if (!selected) {
                    text_SourceSSHHost.setEnabled(false);
                    text_SourceSSHUsername.setEnabled(false);
                    text_SourceSSHPassword.setEnabled(false);
                    text_SourceSSHPort.setEnabled(false);
                } else {

                    text_SourceSSHHost.setEnabled(true);
                    text_SourceSSHUsername.setEnabled(true);
                    text_SourceSSHPassword.setEnabled(true);
                    text_SourceSSHPort.setEnabled(true);
                }
            }
        });

        /**
         * Source Databases Dropdown - Change
         */
        cboSelSourceDB.addActionListener (new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                source_db = cboSelSourceDB.getSelectedItem().toString();
            }
        });

        /**
         * Move to Next Step (Target Connection)
         */
        btnNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Save Source Settings into Model Class which save source/target settings, and is passed between JFrames
                setSourceModel();
                int dialogButton = JOptionPane.YES_NO_OPTION;
                int dialogResult = JOptionPane.showConfirmDialog(panelMain, "Is the Target Server Connection Info the same as the Source Connect (Excluding Database) ?", "Target Same as Source", dialogButton);
                if(dialogResult == 0) {  // if yes
                    SourceTarget.setTargetSameAsSource();
                    // Pass SourceTarget Model to Target Form
                    SourceTablesForm sourceTables = new SourceTablesForm(SourceTarget);
                    JFrame frame = new JFrame("Set Target Connection");
                    frame.setContentPane(sourceTables.panelMain);
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.pack();
                    frame.setVisible(true);
                    frame.setLocationRelativeTo(null);
                } else { // if no
                    // Pass SourceTarget Model to Target Form
                    TargetConnForm targetConn = new TargetConnForm(SourceTarget);
                    JFrame frame = new JFrame("Set Target Connection");
                    frame.setContentPane(targetConn.panelMain);
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.pack();
                    frame.setVisible(true);
                    frame.setLocationRelativeTo(null);
                }
            }
        });
    }

    /**
     * function to fill source databases list
     * after entering source connection info
     */
    public void fillDatabasesDropdown() {
        List<String> dbList = new ArrayList<>();
        if (chkSSHTunnelCheckBox.isSelected()) {  // if SSH Tunnelling
            List<Map<String, String>> retVal = new ArrayList<>();
            try {
                retVal = database_ssh.getQuery("SHOW DATABASES");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            for (Map<String, String> item : retVal) {
                Iterator it = item.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    dbList.add(pair.getValue().toString());
                }
            }
        } else {  // if local db connection
            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SHOW DATABASES");
                while (rs.next()) {
                    String Database = rs.getString("Database");
                    dbList.add(Database);
                }
                st.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        String[] dbs = dbList.toArray(new String[dbList.size()]);
        cboSelSourceDB.setEnabled(true);
        cboSelSourceDB.setModel(new DefaultComboBoxModel(dbs));
        if (text_SourceDatabase.isEnabled() && text_SourceDatabase.getText() != "") {
            int i = 0;
            for (String db : dbList) {
                if (db.equals(text_SourceDatabase.getText().toString().trim())) {
                    cboSelSourceDB.setSelectedIndex(i);
                }
                i++;
            }
        }
        text_SourceDatabase.setEnabled(false);
    }

    /**
     * function to set SourceTarget object used for connecting to db
     */
    public void setSourceModel() {
        SourceTarget.setSourceIsSSHTunnel(chkSSHTunnelCheckBox.isSelected());
        if (chkSSHTunnelCheckBox.isSelected()) {
            SourceTarget.setSourceSSHHost(text_SourceSSHHost.getText().toString());
            SourceTarget.setSourceSSHUsername(text_SourceSSHUsername.getText().toString());
            SourceTarget.setSourceSSHPassword(text_SourceSSHPassword.getText().toString());
            SourceTarget.setSourceSSHPort(text_SourceSSHPort.getText().toString());
        }
        SourceTarget.setSourceDBHost(text_SourceHost.getText().toString());
        SourceTarget.setSourceDBUsername(text_SourceUsername.getText().toString());
        SourceTarget.setSourceDBPassword(text_SourcePassword.getText().toString());
        SourceTarget.setSourceDBName(text_SourceDatabase.getText().toString());
    }

    /**
     * function to load default settings to avoid test input
     */
    public void loadConfigSettings() {
        ConfigLoader config = new ConfigLoader();
        if (chkSSHTunnelCheckBox.isSelected()) {
            text_SourceSSHHost.setText(config.getSsh_host());
            text_SourceSSHUsername.setText(config.getSsh_username());
            text_SourceSSHPassword.setText(config.getSsh_password());
            text_SourceSSHPort.setText(config.getSsh_port());
        }
        text_SourceHost.setText(config.getDb_host());
        text_SourceUsername.setText(config.getDb_username());
        text_SourcePassword.setText(config.getDb_password());
        text_SourceDatabase.setText(config.getDb_name());
    }
}