package view;

import dao.AirportDAO;
import dao.FlightDAO;
import dao.PlainDAO;
import dao.UserDAO;
import db.DbConnection;
import static java.awt.AWTEventMulticaster.add;
import java.awt.Color;
import java.awt.Component;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import model.Airport;
import model.Flight;
import model.Plane;
import model.User;

/**
 *
 * @author ASUS
 */
public class AdminDashboard extends javax.swing.JFrame {

    ResultSet rs = null;
    private User loggedInUser;
    Connection con = null;
    PreparedStatement pst = null;

    public AdminDashboard() {
        initComponents();
        setupTabChangeListener();
    }
      
        public void setupTabChangeListener(){
        jTabbedPane1.addChangeListener(new ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
               Component selectedPanel = jTabbedPane1.getSelectedComponent();
               
               if(selectedPanel == userManaging){
                   System.out.println("User Panel selected");
                   loadUserData();
               }else if (selectedPanel == flightScheduling){
                   System.out.println("Flight Scheduling Panel selected");
                   loadComboBoxData();
                   loadflightData();
               }else if (selectedPanel ==AirplaneAndAirport){
                   System.out.println("Airplane / Airport Panel selected");
                   loadplaneData();
                   loadairportData();
               }else if (selectedPanel ==FM){
                   System.out.println("Flight Management Panel selected");
                   loadflightData1();
               }
            }
        });

        
       loadUserData();
 
    }


    private void loadUserData() {
        try {
            Connection con = DbConnection.getConnection();
            pst = con.prepareStatement("Select user_id,username,email,role,status from users");
            rs = pst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int n = rsmd.getColumnCount();
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);
            while (rs.next()) {
                Vector v = new Vector();
                for (int i = 1; i < n; i++) {
                    v.add(rs.getString("user_id"));
                    v.add(rs.getString("username"));
                    v.add(rs.getString("email"));
                    v.add(rs.getString("role"));
                    v.add(rs.getString("status"));

                }
                dtm.addRow(v);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    //load plane data
    private void loadplaneData() {
        try {
            Connection con = DbConnection.getConnection();
            pst = con.prepareStatement("Select plane_id,model_name,capacity_class from airplane");
            rs = pst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int n = rsmd.getColumnCount();
            DefaultTableModel dtm = (DefaultTableModel) jTable2.getModel();
            dtm.setRowCount(0);
            while (rs.next()) {
                Vector v = new Vector();
                for (int i = 1; i < n; i++) {
                    v.add(rs.getString("plane_id"));
                    v.add(rs.getString("model_name"));
                    v.add(rs.getString("capacity_class"));

                }
                dtm.addRow(v);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void loadairportData() {
        try {
            Connection con = DbConnection.getConnection();
            pst = con.prepareStatement("Select airport_id,name,city,country from airport");
            rs = pst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int n = rsmd.getColumnCount();
            DefaultTableModel dtm = (DefaultTableModel) jTable3.getModel();
            dtm.setRowCount(0);
            while (rs.next()) {
                Vector v = new Vector();
                for (int i = 1; i < n; i++) {
                    v.add(rs.getString("airport_id"));
                    v.add(rs.getString("name"));
                    v.add(rs.getString("city"));
                    v.add(rs.getString("country"));

                }
                dtm.addRow(v);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    private void  loadflightData(){
         try {
            Connection con = DbConnection.getConnection();
            pst = con.prepareStatement("Select flight_id,plane_id,airport_id,origin,destination,transit,departure,arrival from flights");
            rs = pst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int n = rsmd.getColumnCount();
            DefaultTableModel dtm = (DefaultTableModel) jTable4.getModel();
            dtm.setRowCount(0);
            while (rs.next()) {
                Vector v = new Vector();
                for (int i = 1; i < n; i++) {
                    v.add(rs.getString("flight_id"));
                    v.add(rs.getString("plane_id"));
                    v.add(rs.getString("airport_id"));
                    v.add(rs.getString("origin"));
                    v.add(rs.getString("destination"));
                    v.add(rs.getString("transit"));
                    v.add(rs.getString("departure"));
                    v.add(rs.getString("arrival"));
                    

                }
                dtm.addRow(v);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    private void  loadflightData1(){
         try {
            Connection con = DbConnection.getConnection();
            pst = con.prepareStatement("Select flight_id,plane_id,airport_id,origin,destination,transit,departure,arrival,Action from flights");
            rs = pst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int n = rsmd.getColumnCount();
            DefaultTableModel dtm = (DefaultTableModel) fmT.getModel();
            dtm.setRowCount(0);
            while (rs.next()) {
                Vector v = new Vector();
                for (int i = 1; i < n; i++) {
                    v.add(rs.getString("flight_id"));
                    v.add(rs.getString("plane_id"));
                    v.add(rs.getString("airport_id"));
                    v.add(rs.getString("origin"));
                    v.add(rs.getString("destination"));
                    v.add(rs.getString("transit"));
                    v.add(rs.getString("departure"));
                    v.add(rs.getString("arrival"));
                    v.add(rs.getString("Action"));

                }
                dtm.addRow(v);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    private final Map<String, String> planeCapacityMap = new HashMap<>();
    
  private void loadComboBoxData() {
    try {
        Connection con = DbConnection.getConnection();
        
        cbplaneid.removeAllItems();
        planeCapacityMap.clear();
        
        // Load plane IDs into comboBoxPlaneId
        PreparedStatement pst = con.prepareStatement("SELECT plane_id, capacity_class FROM airplane");
        ResultSet rs = pst.executeQuery();
        
       

        while (rs.next()) {
            String planeId = rs.getString("plane_id");
            String capacity = rs.getString("capacity_class");
            cbplaneid.addItem(planeId);
            planeCapacityMap.put(planeId, capacity);
        }
        
        if(cbplaneid.getActionListeners().length == 0){
            cbplaneid.addActionListener(e -> handlePlaneSelection());
        }

      /* cbplaneid.addActionListener(e -> {
            String selectedPlane = (String) cbplaneid.getSelectedItem();
            if (selectedPlane != null && planeCapacityMap.containsKey(selectedPlane)) {
                String capacity = planeCapacityMap.get(selectedPlane);
                cbtransit.setEnabled("Large".equalsIgnoreCase(capacity));
                if (! cbtransit.isEnabled())  cbtransit.setSelectedItem("");
            }
        });*/

        // Load airport IDs, origins, and destinations
        PreparedStatement pst2 = con.prepareStatement("SELECT airport_id, name,city,country FROM airport");
        ResultSet rs2 = pst2.executeQuery();
        cbairportid.removeAllItems();
        cborigin.removeAllItems();
        cbdestination.removeAllItems();
        cbtransit.removeAllItems();
        while (rs2.next()) {
            String name = rs2.getString("name");
            cbairportid.addItem(rs2.getString("airport_id"));
            cborigin.addItem(name);
            cbdestination.addItem(name);
            cbtransit.addItem(name);
        }
        if(cbplaneid.getItemCount()> 0){
            cbplaneid.setSelectedIndex(0);
            handlePlaneSelection();
        }
        
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    
  }
   private void handlePlaneSelection (){
      String selectedPlane = (String) cbplaneid.getSelectedItem();
      if(selectedPlane != null && planeCapacityMap.containsKey(selectedPlane)){
          String capacity = planeCapacityMap.get(selectedPlane);
          boolean isLarge = "large".equalsIgnoreCase(capacity);
          cbtransit.setEnabled(isLarge);
         // System.out.println("Selected Plane "+selectedPlane + "Capacity " + capacity);
          if(!isLarge){
              cbtransit.setSelectedItem("");
          }
      }
}

    AdminDashboard(User user) {
        this.loggedInUser = user;
        initComponents();
        setupTabChangeListener();
        name.setText("Welcome, " + loggedInUser.getUsername());
        loadUserData();
        loadflightData1();
    }

   

   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        userManaging = new javax.swing.JPanel();
        name = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        em = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        rl = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        st = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        emailField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        passwordField = new javax.swing.JPasswordField();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        cb = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        uid = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        uidfield = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        flightScheduling = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        cbplaneid = new javax.swing.JComboBox<>();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        cbairportid = new javax.swing.JComboBox<>();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        cborigin = new javax.swing.JComboBox<>();
        cbdestination = new javax.swing.JComboBox<>();
        cbtransit = new javax.swing.JComboBox<>();
        dtdeparture = new com.github.lgooddatepicker.components.DateTimePicker();
        dtarrival = new com.github.lgooddatepicker.components.DateTimePicker();
        jButton10 = new javax.swing.JButton();
        AirplaneAndAirport = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        mn = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        cc = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txtPlaneId = new javax.swing.JTextField();
        jButton6 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        apName = new javax.swing.JTextField();
        apCity = new javax.swing.JTextField();
        apCountry = new javax.swing.JTextField();
        jButton8 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        apId = new javax.swing.JTextField();
        jButton9 = new javax.swing.JButton();
        FM = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        fmT = new javax.swing.JTable();
        ac = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        jButton11 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));

        userManaging.setBackground(new java.awt.Color(255, 255, 255));

        name.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "UserId", "username", "email", "role", "status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable1);

        jLabel1.setText("Email :");
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jLabel2.setText("Role:");
        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jLabel3.setText("Status:");
        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jLabel4.setText("Create a New User");
        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jLabel5.setText("Name*");
        jLabel5.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N

        nameField.setText("Enter your Name");
        nameField.setForeground(new java.awt.Color(51, 51, 51));
        nameField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                nameFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                nameFieldFocusLost(evt);
            }
        });

        jLabel6.setText("Email*");
        jLabel6.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N

        emailField.setText("Enter your Email");
        emailField.setForeground(new java.awt.Color(51, 51, 51));
        emailField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                emailFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                emailFieldFocusLost(evt);
            }
        });

        jLabel7.setText("Password*");
        jLabel7.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N

        passwordField.setText("Enter your Password");
        passwordField.setEchoChar('\u0000');
        passwordField.setForeground(new java.awt.Color(51, 51, 51));
        passwordField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                passwordFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                passwordFieldFocusLost(evt);
            }
        });

        jButton2.setText("Register New User");
        jButton2.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton1.setText("Update");
        jButton1.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        cb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Role", "Admin", "Operator", "Customer" }));

        jLabel8.setText("Role*");
        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jButton3.setText("Sign Out");
        jButton3.setBackground(new java.awt.Color(0, 255, 204));
        jButton3.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel22.setText("User ID:");
        jLabel22.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N

        uidfield.setText("Enter user id");
        uidfield.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                uidfieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                uidfieldFocusLost(evt);
            }
        });

        jLabel23.setText("User Id");
        jLabel23.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N

        javax.swing.GroupLayout userManagingLayout = new javax.swing.GroupLayout(userManaging);
        userManaging.setLayout(userManagingLayout);
        userManagingLayout.setHorizontalGroup(
            userManagingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userManagingLayout.createSequentialGroup()
                .addGroup(userManagingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(userManagingLayout.createSequentialGroup()
                        .addGap(72, 72, 72)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(userManagingLayout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addGroup(userManagingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(name, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(userManagingLayout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addGroup(userManagingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel8)
                            .addComponent(jButton2)
                            .addComponent(nameField)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(emailField)
                            .addComponent(jLabel7)
                            .addComponent(passwordField, javax.swing.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)
                            .addComponent(cb, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(uidfield)
                            .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 196, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 969, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, userManagingLayout.createSequentialGroup()
                .addContainerGap(710, Short.MAX_VALUE)
                .addGroup(userManagingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, userManagingLayout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(432, 432, 432))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, userManagingLayout.createSequentialGroup()
                        .addGroup(userManagingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(userManagingLayout.createSequentialGroup()
                                .addGroup(userManagingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18))
                            .addGroup(userManagingLayout.createSequentialGroup()
                                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(32, 32, 32)))
                        .addGroup(userManagingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(uid)
                            .addComponent(em)
                            .addComponent(rl)
                            .addComponent(st, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE))
                        .addGap(388, 388, 388))))
        );
        userManagingLayout.setVerticalGroup(
            userManagingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userManagingLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(name, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(uidfield, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(emailField, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cb, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52))
            .addGroup(userManagingLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 411, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addGroup(userManagingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(uid, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(userManagingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(em, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(userManagingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rl, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(userManagingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(userManagingLayout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(jLabel3))
                    .addGroup(userManagingLayout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(st, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 8, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("User Managing", userManaging);

        flightScheduling.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel24.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel24.setText("Schedule a Flight");
        jPanel2.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, 160, 40));

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Flight Id", "Plane Id", "Airport Id", "Origin", "Destination", "Transit", "Depature", "Arrival"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane4.setViewportView(jTable4);

        jPanel2.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 0, 1050, 540));

        cbplaneid.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select a Plane ID" }));
        cbplaneid.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        cbplaneid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbplaneidActionPerformed(evt);
            }
        });
        jPanel2.add(cbplaneid, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 90, 160, -1));

        jLabel25.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel25.setText("Plane ID:");
        jPanel2.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 70, -1));

        jLabel26.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel26.setText("Airport Id");
        jPanel2.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 80, -1));

        cbairportid.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select a Airport ID" }));
        cbairportid.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jPanel2.add(cbairportid, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 140, 160, -1));

        jLabel27.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel27.setText("Origin");
        jPanel2.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, 70, -1));

        jLabel28.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel28.setText("Destination");
        jPanel2.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, 80, -1));

        jLabel29.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel29.setText("Transit");
        jPanel2.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 280, 60, 20));

        jLabel30.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel30.setText("Departure");
        jPanel2.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 320, 70, -1));

        jLabel31.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel31.setText("Arrival");
        jPanel2.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 360, 50, -1));

        cborigin.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        cborigin.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select an Origin" }));
        jPanel2.add(cborigin, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 190, 160, -1));

        cbdestination.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        cbdestination.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select a Destination" }));
        jPanel2.add(cbdestination, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 240, 160, -1));

        cbtransit.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select a Transit" }));
        cbtransit.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        cbtransit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbtransitActionPerformed(evt);
            }
        });
        jPanel2.add(cbtransit, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 280, 160, -1));
        jPanel2.add(dtdeparture, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 320, -1, -1));
        jPanel2.add(dtarrival, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 360, -1, -1));

        jButton10.setText("Schedule");
        jButton10.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton10, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 410, -1, -1));

        javax.swing.GroupLayout flightSchedulingLayout = new javax.swing.GroupLayout(flightScheduling);
        flightScheduling.setLayout(flightSchedulingLayout);
        flightSchedulingLayout.setHorizontalGroup(
            flightSchedulingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        flightSchedulingLayout.setVerticalGroup(
            flightSchedulingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Flight Scheduling", flightScheduling);

        AirplaneAndAirport.setBackground(new java.awt.Color(255, 255, 255));

        jPanel5.setBackground(new java.awt.Color(0, 204, 204));

        jLabel9.setText("Airplane Management");
        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "plane-Id", "ModelName", "CapacityClass"
            }
        ));
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable2);

        jPanel8.setBackground(new java.awt.Color(255, 255, 204));

        jLabel11.setText("Manage New AirPlane");
        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jLabel12.setText("Model Name");
        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jLabel13.setText("Capacity Class");
        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        cc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ccActionPerformed(evt);
            }
        });

        jButton4.setText("Insert");
        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Update");
        jButton5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(mn)
                            .addComponent(cc, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addComponent(jButton4)
                        .addGap(51, 51, 51)
                        .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)))
                .addContainerGap(57, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel11)
                .addGap(30, 30, 30)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(mn, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(cc, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(200, Short.MAX_VALUE))
        );

        jLabel14.setText("Delete Airplane");
        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jLabel15.setText("Plane - Id");
        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jButton6.setText("Delete");
        jButton6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(116, 116, 116)
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPlaneId, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(146, 146, 146)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel14)
                        .addGap(30, 30, 30)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPlaneId, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(30, 30, 30)
                        .addComponent(jButton6)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 702, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(148, 148, 148)
                .addComponent(jLabel9)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel6.setBackground(new java.awt.Color(153, 153, 255));

        jLabel10.setText("Airport Management");
        jLabel10.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Airport-id", "name", "city", "country"
            }
        ));
        jTable3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable3MouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTable3);

        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel10.setBackground(new java.awt.Color(255, 255, 204));

        jLabel16.setText("Manage Airport Details");
        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jLabel17.setText("Name");
        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jLabel18.setText("City");
        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jLabel19.setText("Country");
        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jButton8.setText("Update");
        jButton8.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton7.setText("Insert");
        jButton7.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(84, 84, 84)
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(jLabel19)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(apCountry, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE))
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel17)
                                    .addComponent(jLabel18))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(apName)
                                    .addComponent(apCity, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)))))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(66, 66, 66)
                        .addComponent(jButton7)
                        .addGap(44, 44, 44)
                        .addComponent(jButton8)))
                .addContainerGap(144, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel16)
                .addGap(26, 26, 26)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel17)
                    .addComponent(apName, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(apCity, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(apCountry, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(43, 43, 43)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton8)
                    .addComponent(jButton7))
                .addContainerGap(141, Short.MAX_VALUE))
        );

        jPanel9.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 420, 440));

        jLabel20.setText("Delete Airport");
        jPanel9.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 20, -1, -1));

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel21.setText("Airport -Id");
        jPanel9.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 80, -1, -1));
        jPanel9.add(apId, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 70, 150, 40));

        jButton9.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jButton9.setText("Delete");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jPanel9.add(jButton9, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 140, -1, -1));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(234, 234, 234)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE))
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout AirplaneAndAirportLayout = new javax.swing.GroupLayout(AirplaneAndAirport);
        AirplaneAndAirport.setLayout(AirplaneAndAirportLayout);
        AirplaneAndAirportLayout.setHorizontalGroup(
            AirplaneAndAirportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AirplaneAndAirportLayout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        AirplaneAndAirportLayout.setVerticalGroup(
            AirplaneAndAirportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Airplane and Airport Managing", AirplaneAndAirport);

        FM.setBackground(new java.awt.Color(255, 255, 255));

        fmT.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Flight Id", "Plane Id", "Airport Id", "Origin", "Destination", "Transit", "Departure", "Arrival", "Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        fmT.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fmTMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(fmT);

        jLabel32.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel32.setText("Status");

        jButton11.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jButton11.setText("Update");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout FMLayout = new javax.swing.GroupLayout(FM);
        FM.setLayout(FMLayout);
        FMLayout.setHorizontalGroup(
            FMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FMLayout.createSequentialGroup()
                .addGroup(FMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(FMLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane5))
                    .addGroup(FMLayout.createSequentialGroup()
                        .addGap(404, 404, 404)
                        .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(ac, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 727, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(FMLayout.createSequentialGroup()
                .addGap(538, 538, 538)
                .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        FMLayout.setVerticalGroup(
            FMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FMLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 484, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addGroup(FMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ac, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32))
                .addGap(18, 18, 18)
                .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(136, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Flight Managing", FM);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTabbedPane1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTabbedPane1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void nameFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nameFieldFocusGained
        // TODO add your handling code here:
        if (nameField.getText().equals("Enter your Name")) {
            nameField.setText("");
            nameField.setForeground((new Color(51, 51, 51)));

        }
    }//GEN-LAST:event_nameFieldFocusGained

    private void nameFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nameFieldFocusLost
        // TODO add your handling code here:
        if (nameField.getText().equals("")) {
            nameField.setText("Enter your Name");
            nameField.setForeground((new Color(153, 153, 153)));

        }
    }//GEN-LAST:event_nameFieldFocusLost

    private void emailFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_emailFieldFocusGained
        // TODO add your handling code here:
        if (emailField.getText().equals("Enter your Email")) {
            emailField.setText("");
            emailField.setForeground((new Color(51, 51, 51)));

        }
    }//GEN-LAST:event_emailFieldFocusGained

    private void emailFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_emailFieldFocusLost
        // TODO add your handling code here:
        if (emailField.getText().equals("")) {
            emailField.setText("Enter your Email");
            emailField.setForeground((new Color(51, 51, 51)));

        }
    }//GEN-LAST:event_emailFieldFocusLost

    private void passwordFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_passwordFieldFocusGained
        // TODO add your handling code here:
        if (passwordField.getText().equals("Enter your Password")) {
            passwordField.setText("");
            passwordField.setForeground((new Color(51, 51, 51)));

        }
    }//GEN-LAST:event_passwordFieldFocusGained

    private void passwordFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_passwordFieldFocusLost
        // TODO add your handling code here:
        if (passwordField.getText().equals("")) {
            passwordField.setText("Enter your Password");
            passwordField.setForeground((new Color(153, 153, 153)));

        }
    }//GEN-LAST:event_passwordFieldFocusLost

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        String userid = uidfield.getText();
        String name = nameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String role = cb.getSelectedItem().toString();

        User user1 = new User(userid, name, email, password, role);
        UserDAO userDao = new UserDAO();
        boolean success = userDao.registerUser1(user1);
        if (success) {
            JOptionPane.showMessageDialog(null, "Registration success");

            loadUserData();
            uidfield.setText("");
            nameField.setText("");
            emailField.setText("");
            passwordField.setText("");
            cb.setSelectedIndex(0);

        } else {
            JOptionPane.showMessageDialog(null, "Registration Failed");
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();

        String userId = model.getValueAt(jTable1.getSelectedRow(), 0).toString();
        String email = model.getValueAt(jTable1.getSelectedRow(), 2).toString();
        String role = model.getValueAt(jTable1.getSelectedRow(), 3).toString();
        String status = model.getValueAt(jTable1.getSelectedRow(), 4).toString();

        uid.setText(userId);
        em.setText(email);
        rl.setText(role);
        st.setText(status);
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
        if (jTable1.getSelectedColumnCount() == 1) {
            String userId = uid.getText();
            String email = em.getText();
            String role = rl.getText();
            String status = st.getText();

            try {
                Connection con = DbConnection.getConnection();
                int row = jTable1.getSelectedRow();
                String value = (jTable1.getModel().getValueAt(row, 0).toString()); //original id

                dtm.setValueAt(userId, jTable1.getSelectedRow(), 0);
                dtm.setValueAt(email, jTable1.getSelectedRow(), 2);
                dtm.setValueAt(role, jTable1.getSelectedRow(), 3);
                dtm.setValueAt(status, jTable1.getSelectedRow(), 4);

                String q = "UPDATE users SET user_id=?, email=? , role =? , status=? WHERE user_id=?";
                PreparedStatement pt = con.prepareStatement(q);
                pt.setString(1, uid.getText());
                pt.setString(2, em.getText());
                pt.setString(3, rl.getText());
                pt.setString(4, st.getText());
                pt.setString(5, value);
                pt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Updated success");

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        this.dispose();
        LoginPage login = new LoginPage();
        login.setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void uidfieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_uidfieldFocusGained
        // TODO add your handling code here:
        if (uidfield.getText().equals("Enter user id")) {
            uidfield.setText("");
            uidfield.setForeground((new Color(51, 51, 51)));

        }
    }//GEN-LAST:event_uidfieldFocusGained

    private void uidfieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_uidfieldFocusLost
        // TODO add your handling code here:

        if (uidfield.getText().equals("")) {
            uidfield.setText("Enter user id");
            uidfield.setForeground((new Color(51, 51, 51)));

        }
    }//GEN-LAST:event_uidfieldFocusLost

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:

        String modelName = mn.getText();
        String capacity = cc.getText();

        Plane plane = new Plane(modelName, capacity);
        PlainDAO plainDao = new PlainDAO();
        boolean success = plainDao.registerPlain(plane);
        if (success) {
            JOptionPane.showMessageDialog(null, "Registration success");
            loadplaneData();
            mn.setText("");
            cc.setText("");

        } else {
            JOptionPane.showMessageDialog(null, "Registration Failed");
        }

    }//GEN-LAST:event_jButton4ActionPerformed

    private void ccActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ccActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ccActionPerformed
// jtable2 mouse clicked
    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        // jtable2 mouse clicked

        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();

        String Planeid = model.getValueAt(jTable2.getSelectedRow(), 0).toString();
        String modelname = model.getValueAt(jTable2.getSelectedRow(), 1).toString();
        String capa = model.getValueAt(jTable2.getSelectedRow(), 2).toString();

        txtPlaneId.setText(Planeid);
        mn.setText(modelname);
        cc.setText(capa);


    }//GEN-LAST:event_jTable2MouseClicked
// Update airplane details
    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        

        DefaultTableModel dtm = (DefaultTableModel) jTable2.getModel();
        if (jTable2.getSelectedColumnCount() == 1) {

            String model = mn.getText();
            String capa = cc.getText();

            try {
                Connection con = DbConnection.getConnection();
                int row = jTable2.getSelectedRow();
                String value = (jTable2.getModel().getValueAt(row, 0).toString()); //original id

                dtm.setValueAt(model, jTable2.getSelectedRow(), 1);
                dtm.setValueAt(capa, jTable2.getSelectedRow(), 2);

                String q = "UPDATE airplane SET  model_name=? , capacity_class =?  WHERE plane_id=?";
                PreparedStatement pt = con.prepareStatement(q);

                pt.setString(1, mn.getText());
                pt.setString(2, cc.getText());
                pt.setString(3, value);
                pt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Updated success");

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_jButton5ActionPerformed
 // delete plane based on user id
    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // delete plane based on user id
        String planeId = txtPlaneId.getText();
        if (planeId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a plane to delete");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delet this plane?", "Cofirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean deleted = new PlainDAO().DeletePlane(planeId);

            if (deleted) {
                JOptionPane.showMessageDialog(this, "Plane deleted successfully");
                txtPlaneId.setText("");
                //Refresh table
                loadplaneData();
            } else {
                JOptionPane.showMessageDialog(this, "Deletion Failed.Please try again");
            }
        }
    }//GEN-LAST:event_jButton6ActionPerformed
//Airport table
    private void jTable3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseClicked
        //Airport table
        DefaultTableModel model = (DefaultTableModel) jTable3.getModel();

        String Airportid = model.getValueAt(jTable3.getSelectedRow(), 0).toString();
        String Name = model.getValueAt(jTable3.getSelectedRow(), 1).toString();
        String City = model.getValueAt(jTable3.getSelectedRow(), 2).toString();
        String Country = model.getValueAt(jTable3.getSelectedRow(), 3).toString();

        apId.setText(Airportid);
        apName.setText(Name);
        apCity.setText(City);
        apCountry.setText(Country);
    }//GEN-LAST:event_jTable3MouseClicked
// insert airport details
    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        
         // TODO add your handling code here:

        String Name =  apName.getText();
        String City = apCity.getText();
        String Country = apCountry.getText();

        Airport airport = new  Airport( Name,City,Country);
        AirportDAO airportDao = new AirportDAO();
        boolean success = airportDao.registerAirport(airport);
        if (success) {
            JOptionPane.showMessageDialog(null, "Registration success");
           loadairportData();
            apName.setText("");
            apCity.setText("");
            apCountry.setText("");
            

        } else {
            JOptionPane.showMessageDialog(null, "Registration Failed");
        }
    }//GEN-LAST:event_jButton7ActionPerformed
// Delete AirPort
         // delete plane based on user id
    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // Delete AirPort
         // delete plane based on user id
        String airportId = apId.getText();
        if (airportId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a Airport to delete");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delet this Airport?", "Cofirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean deleted = new AirportDAO().DeleteAirport(airportId);

            if (deleted) {
                JOptionPane.showMessageDialog(this, "Plane deleted successfully");
                apId.setText("");
                //Refresh table
                loadairportData();
            } else {
                JOptionPane.showMessageDialog(this, "Deletion Failed.Please try again");
            }
        }
    }//GEN-LAST:event_jButton9ActionPerformed
 //Update airport details:
    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
       
         DefaultTableModel dtm = (DefaultTableModel) jTable3.getModel();
        if (jTable3.getSelectedColumnCount() == 1) {

             String Name =  apName.getText();
             String City = apCity.getText();
             String Country = apCountry.getText();


            try {
                Connection con = DbConnection.getConnection();
                int row = jTable3.getSelectedRow();
                String value = (jTable3.getModel().getValueAt(row, 0).toString()); //original id

                dtm.setValueAt(Name, jTable3.getSelectedRow(), 1);
                dtm.setValueAt(City, jTable3.getSelectedRow(), 2);
                dtm.setValueAt(Country, jTable3.getSelectedRow(), 2);

                String q = "UPDATE airport SET  name=? , city =? , country = ?  WHERE airport_id=?";
                PreparedStatement pt = con.prepareStatement(q);

                pt.setString(1, apName.getText());
                pt.setString(2, apCity.getText());
                pt.setString(3, apCountry.getText());
                pt.setString(4, value);
                pt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Updated success");
                apName.setText("");
                apCity.setText("");
                apCountry.setText("");
                

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_jButton8ActionPerformed
  //Schedule Flights
    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        //Schedule Flights
        try{
            //Get Combo box selected Items as strings
            String planeId = (String) cbplaneid.getSelectedItem();
            String airportId = (String) cbairportid.getSelectedItem();
            String origin = (String) cborigin.getSelectedItem();
            String destination = (String) cbdestination.getSelectedItem();
            String transit = cbtransit.isEnabled() ? (String) cbtransit.getSelectedItem():null;
            
           //Get date and time
           LocalDateTime departure = dtdeparture.getDateTimeStrict();
           LocalDateTime arrival   = dtarrival.getDateTimeStrict();
           
           String departureStr =departure.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
           String arrivalStr =arrival.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
           
          
           //Validation for dat and time
           if(departure == null || arrival == null){
               JOptionPane.showMessageDialog(this,"Please select both daparture and arrival date/time");
               return;
           }
           if(!arrival.isAfter(departure)){
               JOptionPane.showMessageDialog(this,"Arrival time must be after depature time");
               return;
           }
           
           //validation for combo boxes
           if(planeId == null || origin == null || destination == null || origin.equals(destination)){
               JOptionPane.showMessageDialog(this,"Please select valid PlaneId , Origin and Destination(different airports");
               return;
           }
           if( cbtransit.isEnabled() && (transit == null || transit.equals(origin) || transit.equals(destination))){
               JOptionPane.showMessageDialog(this,"Please select a valid Transit airport different from Origin and Destination");
               return;
           }
           
           Flight flight = new  Flight(planeId,airportId,origin,destination,transit,departure,arrival);
           FlightDAO flightDao = new FlightDAO();
           boolean success = flightDao.scheduleFlight(flight);
        if (success) {
            JOptionPane.showMessageDialog(null, "Scheduling success");
            loadflightData();
           
            
            

        } else {
            JOptionPane.showMessageDialog(null, "Scheduling  Failed");
        }
           
        } catch(Exception ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,"Error while processing Flight Schedule");
        }
        
        
    }//GEN-LAST:event_jButton10ActionPerformed

    private void cbtransitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbtransitActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbtransitActionPerformed

    private void cbplaneidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbplaneidActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbplaneidActionPerformed

    private void fmTMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fmTMouseClicked
        // TODO add your handling code here:
         DefaultTableModel model = (DefaultTableModel) fmT.getModel();
         int selectedRow =fmT.getSelectedRow();
         if(selectedRow >=0){
             Object Status = model.getValueAt(selectedRow,8);
             
             if(Status != null){
                 ac.setText(Status.toString());
             }else{
                 ac.setText(""); 
             }
         }
       
        
    }//GEN-LAST:event_fmTMouseClicked
//upadate status of the flight plan
    //scheduled, present ,caneled
    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
        
        DefaultTableModel dtm = (DefaultTableModel) fmT.getModel();
        if (fmT.getSelectedColumnCount() == 1) {

            String Status = ac.getText();
           

            try {
                Connection con = DbConnection.getConnection();
                int row = fmT.getSelectedRow();
                String value = (fmT.getModel().getValueAt(row, 0).toString()); //original id

                dtm.setValueAt(Status, fmT.getSelectedRow(), 1);
               

                String q = "UPDATE flights SET  Action=? WHERE flight_id=?";
                PreparedStatement pt = con.prepareStatement(q);

                pt.setString(1, ac.getText());
               
                pt.setString(2, value);
                pt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Updated success");
                loadflightData1();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        
    }//GEN-LAST:event_jButton11ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AdminDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AdminDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AdminDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AdminDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AdminDashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel AirplaneAndAirport;
    private javax.swing.JPanel FM;
    private javax.swing.JTextField ac;
    private javax.swing.JTextField apCity;
    private javax.swing.JTextField apCountry;
    private javax.swing.JTextField apId;
    private javax.swing.JTextField apName;
    private javax.swing.JComboBox<String> cb;
    private javax.swing.JComboBox<String> cbairportid;
    private javax.swing.JComboBox<String> cbdestination;
    private javax.swing.JComboBox<String> cborigin;
    private javax.swing.JComboBox<String> cbplaneid;
    private javax.swing.JComboBox<String> cbtransit;
    private javax.swing.JTextField cc;
    private com.github.lgooddatepicker.components.DateTimePicker dtarrival;
    private com.github.lgooddatepicker.components.DateTimePicker dtdeparture;
    private javax.swing.JTextField em;
    private javax.swing.JTextField emailField;
    private javax.swing.JPanel flightScheduling;
    private javax.swing.JTable fmT;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTextField mn;
    private javax.swing.JLabel name;
    private javax.swing.JTextField nameField;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JTextField rl;
    private javax.swing.JTextField st;
    private javax.swing.JTextField txtPlaneId;
    private javax.swing.JTextField uid;
    private javax.swing.JTextField uidfield;
    private javax.swing.JPanel userManaging;
    // End of variables declaration//GEN-END:variables

  
}
