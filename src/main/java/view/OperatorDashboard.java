package view;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import dao.BookingDAO;
import dao.FlightDAO;
import dao.OperatorDAO;
import db.DbConnection;
import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import model.Booking;
import model.Flight;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import model.User;
import org.jfree.chart.plot.CategoryPlot;

public class OperatorDashboard extends javax.swing.JFrame {

    ResultSet rs = null;
    private User loggedInUser;
    Connection con = null;
    PreparedStatement pst = null;
     private String flightStatus = ""; 

    public OperatorDashboard() {
        initComponents();
        loadComboBoxData();
        loadCombo();
        showPieChart();
        loadflightData();
        //loading seats data base on selected class
        cbcls.addActionListener(e -> {
            String seatClass = (String) cbcls.getSelectedItem();
            String flightId = fn.getText();
            
            if (flightId != null && !flightId.isEmpty() && seatClass != null) {
                loadCB1(flightId, seatClass);
            }
        });
    }

    public OperatorDashboard(User user) {
        this.loggedInUser = user;
        initComponents();
        usern.setText("Welcome, " + loggedInUser.getUsername() + " || " + " UID : " + loggedInUser.getUserId());
        
        loadCombo();
        showPieChart();
        
        loadflightData();
        loadComboBoxData();

      //loading seats data base on selected class
            cbcls.addActionListener(e -> {
                String seatClass = (String) cbcls.getSelectedItem();
                String flightId = fn.getText();

                if (flightId != null && !flightId.isEmpty() && seatClass != null) {
                    loadCB1(flightId, seatClass);
                }
            });
       
    }


    //Load the Airport Details to comboBox
    private void loadCombo() {
        try {
            Connection con = DbConnection.getConnection();
            airportList.removeAllItems();

            PreparedStatement pst = con.prepareStatement("SELECT airport_id FROM flights");
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String Airport = rs.getString("airport_id");

                airportList.addItem(Airport);

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

   
//load class
private void loadCB(String flightId) {
        try {
            Connection con = DbConnection.getConnection();
            cbcls.removeAllItems();

            PreparedStatement pst = con.prepareStatement("SELECT  DISTINCT class FROM seats WHERE flight_id=?");
            pst.setString(1, flightId);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                String Class = rs.getString("class");
                //String Seat = rs.getString("seat_no");
                cbcls.addItem(Class);
                //cbst.addItem(Seat);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    //load seat
private void loadCB1(String flightId, String seatClass) {
        try {
            Connection con = DbConnection.getConnection();

            cbst.removeAllItems();

            PreparedStatement pst = con.prepareStatement("SELECT seat_no FROM seats WHERE flight_id= ? AND class= ? AND is_booked = 0");
            pst.setString(1, flightId);
            pst.setString(2, seatClass);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                String Seat = rs.getString("seat_no");
                cbst.addItem(Seat);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    //Add the Chart Creation Method
  private JPanel showPieChart() {
        // Create the dataset
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.setValue(dao.OperatorDAO.getScheduledFlightCount(), "Count", "Scheduled Flights");
        dataset.setValue(dao.OperatorDAO.getBookedFlightCount(), "Count", "Booked Flights");
        dataset.setValue(dao.OperatorDAO.getUserCount(), "Count", "Users");
        dataset.setValue(dao.OperatorDAO.getAirplaneCount(), "Count", "Airplanes");
        dataset.setValue(dao.OperatorDAO.getAirportCount(), "Count", "Airports");

        // Create the chart
        JFreeChart barChart = ChartFactory.createBarChart(
                "System Overview", // Chart Title
                "Category", // X-axis Label
                "Count", // Y-axis Label
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false);

        // Customize appearance
        CategoryPlot plot = barChart.getCategoryPlot();
        plot.setRangeGridlinePaint(Color.BLACK);     // Gridline color
        plot.setBackgroundPaint(Color.WHITE);        // Chart background

        // Wrap in ChartPanel
        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(300, 300));

        // Add chart to  JPanel
        GraphP.removeAll();  // Replace with your panel name
        GraphP.add(chartPanel);
        GraphP.validate();

        return new ChartPanel(barChart);
    };

 private void displayFlightPlans(List<Flight> flights) {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"Flight Id", "Plane Id", "Airport Id", "Origin", "Destination", "Transit", "Departure", "Arrival"});

        for (Flight flight : flights) {
            model.addRow(new Object[]{
                flight.getFlightid(),
                flight.getPlaneId(),
                flight.getAirportId(),
                flight.getOrigin(),
                flight.getDestination(),
                flight.getTransit(),
                flight.getDepature().toString(),
                flight.getArrival().toString()

            });
        }
        flightTable.setModel(model);
    }

//load Flght data
    private void loadflightData() {
        try {
            Connection con = DbConnection.getConnection();
            pst = con.prepareStatement("SELECT flight_id, plane_id, origin, destination, transit, departure, arrival, Action FROM flights");
            rs = pst.executeQuery();

            DefaultTableModel dtm = (DefaultTableModel) jt1.getModel();
            dtm.setRowCount(0);

            while (rs.next()) {
                Vector<String> v = new Vector<>();
                v.add(rs.getString("flight_id"));
                v.add(rs.getString("plane_id"));
                v.add(rs.getString("origin"));
                v.add(rs.getString("destination"));
                v.add(rs.getString("transit"));
                v.add(rs.getString("departure"));
                v.add(rs.getString("arrival"));
                v.add(rs.getString("Action"));

                dtm.addRow(v);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    //Load  Origin , destination details
    private void loadComboBoxData() {
        try {
            Connection con = DbConnection.getConnection();
            cbori.removeAllItems();
            cbdes.removeAllItems();

            PreparedStatement pst = con.prepareStatement("SELECT origin , destination FROM flights");
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String Origin = rs.getString("origin");
                String Destination = rs.getString("destination");
                cbori.addItem(Origin);
                cbdes.addItem(Destination);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

   
   
   

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        analyticalPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        flightTable = new javax.swing.JTable();
        GraphP = new javax.swing.JPanel();
        dept = new com.github.lgooddatepicker.components.DateTimePicker();
        art = new com.github.lgooddatepicker.components.DateTimePicker();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        airportList = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        usern = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        availableFlightPanel = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jt1 = new javax.swing.JTable();
        cbori = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        cbdes = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        dtp = new com.github.lgooddatepicker.components.DatePicker();
        jLabel6 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        fn = new javax.swing.JTextField();
        or = new javax.swing.JTextField();
        des = new javax.swing.JTextField();
        dep = new javax.swing.JTextField();
        arr = new javax.swing.JTextField();
        cbcls = new javax.swing.JComboBox<>();
        cbst = new javax.swing.JComboBox<>();
        jButton6 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        un = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        ui = new javax.swing.JTextField();
        pi = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        tr = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        bookForCustomerPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        bookingT = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        cusId = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        pdb = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N

        analyticalPanel.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        flightTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Fligth Id", "Plane Id", "Airport Id", "Origin", "Destination", "Transit", "Departure", "Arrival"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(flightTable);

        GraphP.setLayout(new java.awt.BorderLayout());

        jButton2.setText("Select");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel1.setText("Departure Time");
        jLabel1.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N

        jLabel2.setText("Arrival Time");
        jLabel2.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N

        airportList.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel3.setText("Airport ");
        jLabel3.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N

        usern.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N

        jButton1.setText("Sign Out");
        jButton1.setBackground(new java.awt.Color(0, 204, 204));
        jButton1.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel23.setText("Check the Availability of Flights");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(136, 136, 136)
                                .addComponent(GraphP, javax.swing.GroupLayout.PREFERRED_SIZE, 654, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(38, 38, 38)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(dept, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(55, 55, 55)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(art, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(56, 56, 56)
                                .addComponent(jLabel3)
                                .addGap(35, 35, 35)
                                .addComponent(airportList, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(75, 75, 75)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 236, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(500, 500, 500)
                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(usern, javax.swing.GroupLayout.PREFERRED_SIZE, 643, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(usern, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel23))
                        .addGap(43, 43, 43)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(dept, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel1)
                                .addComponent(art, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel2))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(airportList, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel3)))
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(GraphP, javax.swing.GroupLayout.PREFERRED_SIZE, 434, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(21, 21, 21))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(76, 76, 76))))
        );

        javax.swing.GroupLayout analyticalPanelLayout = new javax.swing.GroupLayout(analyticalPanel);
        analyticalPanel.setLayout(analyticalPanelLayout);
        analyticalPanelLayout.setHorizontalGroup(
            analyticalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        analyticalPanelLayout.setVerticalGroup(
            analyticalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(analyticalPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Dashbaord", analyticalPanel);

        availableFlightPanel.setBackground(new java.awt.Color(255, 255, 255));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jt1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Flight Id", "Plane Id", "Origin", "Destination", "Transit", "Departure", "Arrival", "Action"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jt1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jt1MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jt1);

        cbori.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Origin", " " }));

        jLabel4.setText("Origin");

        cbdes.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Destination", " " }));

        jLabel5.setText("Destination");

        jLabel6.setText("Date");

        jButton4.setText("Search");
        jButton4.setBackground(new java.awt.Color(51, 153, 255));
        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton3.setText("Clear");
        jButton3.setBackground(new java.awt.Color(102, 204, 0));
        jButton3.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jPanel4.setBackground(new java.awt.Color(0, 204, 204));

        cbcls.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select your Class" }));

        cbst.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select your Seat No" }));

        jButton6.setBackground(new java.awt.Color(0, 0, 0));
        jButton6.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setText("Book");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Flight No:");

        jLabel9.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Origin");

        jLabel10.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Destination");

        jLabel11.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Departure");

        jLabel12.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Arrival");

        jLabel13.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Class");

        jLabel14.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Seat No");

        jLabel17.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Name  :");

        jLabel18.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("User Id");

        ui.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uiActionPerformed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Plane ID");

        jLabel20.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Transit");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(140, 140, 140)
                        .addComponent(pi, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel14)
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel18)
                                .addGap(41, 41, 41)))
                        .addGap(27, 27, 27)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cbcls, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbst, 0, 245, Short.MAX_VALUE)
                            .addComponent(arr)
                            .addComponent(dep)
                            .addComponent(des)
                            .addComponent(or)
                            .addComponent(fn)
                            .addComponent(un)
                            .addComponent(ui)
                            .addComponent(tr))))
                .addContainerGap(40, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(102, 102, 102))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(ui, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(un, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fn, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 35, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(pi, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(or, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(42, 42, 42))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(des, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                        .addGap(27, 27, 27)))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel20)
                    .addComponent(tr, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dep, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(arr, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbcls, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbst, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addGap(18, 18, 18)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51))
        );

        jLabel16.setBackground(new java.awt.Color(0, 0, 0));
        jLabel16.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 51, 51));
        jLabel16.setText("Please Select Flight plan you need from the Table,Then come to the booking ");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1025, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbori, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(53, 53, 53)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(cbdes, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(63, 63, 63)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(dtp, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 442, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(40, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addGap(7, 7, 7)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbori)
                    .addComponent(cbdes)
                    .addComponent(dtp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 589, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(113, 113, 113))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout availableFlightPanelLayout = new javax.swing.GroupLayout(availableFlightPanel);
        availableFlightPanel.setLayout(availableFlightPanelLayout);
        availableFlightPanelLayout.setHorizontalGroup(
            availableFlightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1558, Short.MAX_VALUE)
            .addGroup(availableFlightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, availableFlightPanelLayout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        availableFlightPanelLayout.setVerticalGroup(
            availableFlightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 868, Short.MAX_VALUE)
            .addGroup(availableFlightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, availableFlightPanelLayout.createSequentialGroup()
                    .addContainerGap(15, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 812, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(41, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("Book For Customer", availableFlightPanel);

        bookForCustomerPanel.setBackground(new java.awt.Color(255, 255, 255));

        bookingT.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Booking Id", "Flight Id", "User Id", "Name", "Origin", "Destination", "Transit", "Departure", "Arrival", "Class", "Seat No", "Booked Date"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane3.setViewportView(bookingT);

        jLabel8.setText("Select the User Id");
        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jButton5.setText("Search");
        jButton5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel21.setText("Select Flight Plane From Above table");
        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        pdb.setText("Download Customer Manifest");
        pdb.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        pdb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pdbActionPerformed(evt);
            }
        });

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Assets/booking1.png"))); // NOI18N

        jLabel22.setText("My Bookings");
        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N

        javax.swing.GroupLayout bookForCustomerPanelLayout = new javax.swing.GroupLayout(bookForCustomerPanel);
        bookForCustomerPanel.setLayout(bookForCustomerPanelLayout);
        bookForCustomerPanelLayout.setHorizontalGroup(
            bookForCustomerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bookForCustomerPanelLayout.createSequentialGroup()
                .addGap(423, 423, 423)
                .addGroup(bookForCustomerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bookForCustomerPanelLayout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cusId, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(54, 54, 54)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(bookForCustomerPanelLayout.createSequentialGroup()
                        .addGap(210, 210, 210)
                        .addGroup(bookForCustomerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pdb, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(bookForCustomerPanelLayout.createSequentialGroup()
                        .addGap(96, 96, 96)
                        .addComponent(jLabel15)
                        .addGap(48, 48, 48)
                        .addComponent(jLabel22)))
                .addContainerGap(524, Short.MAX_VALUE))
            .addGroup(bookForCustomerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(bookForCustomerPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 1501, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        bookForCustomerPanelLayout.setVerticalGroup(
            bookForCustomerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bookForCustomerPanelLayout.createSequentialGroup()
                .addGroup(bookForCustomerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bookForCustomerPanelLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jLabel15))
                    .addGroup(bookForCustomerPanelLayout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jLabel22)))
                .addGap(18, 18, 18)
                .addGroup(bookForCustomerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cusId, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 447, Short.MAX_VALUE)
                .addComponent(jLabel21)
                .addGap(31, 31, 31)
                .addComponent(pdb, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(155, 155, 155))
            .addGroup(bookForCustomerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(bookForCustomerPanelLayout.createSequentialGroup()
                    .addGap(210, 210, 210)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(312, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("View Booking", bookForCustomerPanel);

        jPanel1.add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1500, 850));

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
 // Select specific details 
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        String Airport = (String) airportList.getSelectedItem();
        LocalDateTime fromTime = dept.getDateTimeStrict();
        LocalDateTime toTime = art.getDateTimeStrict();

        if (fromTime == null || toTime == null || Airport.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fileds");
            return;
        }
        OperatorDAO dao = new OperatorDAO();
        List<Flight> result = dao.getFlightsByAirportAndTime(Airport, fromTime, toTime);

        if (result.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No results found for the selected Airport and time range");
            //clear table if preveoiusly resutl retrieved
            DefaultTableModel model = (DefaultTableModel) flightTable.getModel();
            model.setRowCount(0);
            airportList.setSelectedIndex(-1);
            dept.clear();
            art.clear();
            return;
        }
        displayFlightPlans(result);

    }//GEN-LAST:event_jButton2ActionPerformed

    // get flight data to booking forum
    private void jt1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jt1MouseClicked

           
        DefaultTableModel model = (DefaultTableModel) jt1.getModel();
        int selectedRow = jt1.getSelectedRow();
        if (selectedRow != -1) {
            String flightId = model.getValueAt(jt1.getSelectedRow(), 0).toString();
            String planeId = model.getValueAt(jt1.getSelectedRow(), 1).toString();
            String origin = model.getValueAt(jt1.getSelectedRow(), 2).toString();
            String destination = model.getValueAt(jt1.getSelectedRow(), 3).toString();
            //String transit = model.getValueAt( jt1.getSelectedRow(), 4).toString();
            Object transisObj = jt1.getValueAt(jt1.getSelectedRow(), 4);
            String transit = (transisObj != null) ? transisObj.toString() : "";
            String departure = model.getValueAt(jt1.getSelectedRow(), 5).toString();
            String arrival = model.getValueAt(jt1.getSelectedRow(), 6).toString();
            
                 // Read Action column (assuming it's column 7)
         Object actionObj = model.getValueAt(selectedRow, 7);  // Change index if needed
         flightStatus = (actionObj != null) ? actionObj.toString() : "";

            fn.setText(flightId);
            pi.setText(planeId);
            or.setText(origin);
            des.setText(destination);
            tr.setText(transit);
            dep.setText(departure);
            arr.setText(arrival);

            //load the classess combo box for selected flight
            loadCB(flightId);

            cbst.removeAllItems();
        }
    }//GEN-LAST:event_jt1MouseClicked

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

        String origin = (String) cbori.getSelectedItem();
        String destination = (String) cbdes.getSelectedItem();
        LocalDate date = dtp.getDate();  // LGoodDatePicker

        StringBuilder query = new StringBuilder("SELECT flight_id, plane_id, origin, destination, transit, departure, arrival, Action FROM flights WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (origin != null && !origin.trim().isEmpty()) {
            query.append(" AND origin = ?");
            params.add(origin);
        }
        if (destination != null && !destination.trim().isEmpty()) {
            query.append(" AND destination = ?");
            params.add(destination);
        }
        if (date != null) {
            query.append(" AND DATE(departure) = ?");
            params.add(Date.valueOf(date));
        }

        try {
            Connection con = DbConnection.getConnection();
            PreparedStatement pst = con.prepareStatement(query.toString());

            for (int i = 0; i < params.size(); i++) {
                pst.setObject(i + 1, params.get(i));
            }

            ResultSet rs = pst.executeQuery();
            DefaultTableModel dtm = (DefaultTableModel) jt1.getModel();
            dtm.setRowCount(0);

            while (rs.next()) {
                Vector<String> v = new Vector<>();
                v.add(rs.getString("flight_id"));
                v.add(rs.getString("plane_id"));
                v.add(rs.getString("origin"));
                v.add(rs.getString("destination"));
                v.add(rs.getString("transit"));
                v.add(rs.getString("departure"));
                v.add(rs.getString("arrival"));
                v.add(rs.getString("Action"));
                dtm.addRow(v);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        cbori.setSelectedIndex(-1);
        cbdes.setSelectedIndex(-1);
        dtp.clear();
        loadflightData();
    }//GEN-LAST:event_jButton3ActionPerformed

    //create a downloadble report
    public static void exportSelectedRowAsBill(JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a flight to generate the bill.");
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save PassengerManifest");
        fileChooser.setSelectedFile(new File("PassengerManifest.pdf"));

        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File fileToSave = fileChooser.getSelectedFile();
        String filePath = fileToSave.getAbsolutePath();
        if (!filePath.toLowerCase().endsWith(".pdf")) {
            filePath += ".pdf";
        }

        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Add heading
            document.add(new Paragraph("Cutomer Manifest ATBS AirLine"));
            document.add(new Paragraph("Generated on: " + java.time.LocalDate.now()));
            document.add(new Paragraph("--------------------------------------------------"));
            document.add(new Paragraph(" "));

            TableModel model = table.getModel();

            for (int i = 0; i < model.getColumnCount(); i++) {
                String colName = model.getColumnName(i);
                Object value = model.getValueAt(selectedRow, i);
                String valStr = value != null ? value.toString() : "-";
                document.add(new Paragraph(colName + ": " + valStr));
            }

            document.add(new Paragraph(" "));
            document.add(new Paragraph("--------------------------------------------------"));
            document.add(new Paragraph("Thank you for choosing our airline."));

            document.close();
            JOptionPane.showMessageDialog(null, "Manifest saved as PDF successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error generating PDF: " + e.getMessage());
        }
    }

    //another way to use rs2xml functions
    public static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        String[] columnNames = new String[columnCount];
        for (int i = 1; i <= columnCount; i++) {
            columnNames[i - 1] = metaData.getColumnName(i);
        }

        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        while (rs.next()) {
            Object[] row = new Object[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                row[i - 1] = rs.getObject(i);
            }
            model.addRow(row);
        }

        return model;
    }

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed

        String id = cusId.getText();

        try {
            Connection con = DbConnection.getConnection();
            String SearchQ = "SELECT * FROM booked_flights WHERE user_id = '" + id + "'";
            PreparedStatement pst = con.prepareStatement(SearchQ);

            ResultSet rs = pst.executeQuery();

            bookingT.setModel(buildTableModel(rs));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }//GEN-LAST:event_jButton5ActionPerformed

    private void pdbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pdbActionPerformed

        exportSelectedRowAsBill(bookingT);
    }//GEN-LAST:event_pdbActionPerformed
//Booking button
    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
         if (flightStatus == null || flightStatus.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Flight status is unknown. Cannot proceed with booking.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    if (flightStatus.equalsIgnoreCase("Canceled") || 
        flightStatus.equalsIgnoreCase("Expired") || 
        flightStatus.equalsIgnoreCase("Departed")) {
        JOptionPane.showMessageDialog(this, "Booking is not allowed for this flight. Status: " + flightStatus, "Flight Not Bookable", JOptionPane.WARNING_MESSAGE);
        return;
    }
         
        String flightNo = fn.getText();
        String planeId = pi.getText();
        String userId = ui.getText();  // Flight ID from form
        String userName = un.getText();
        String origin = or.getText();
        String destination = des.getText();
        String transit = tr.getText();
        String departure = dep.getText();
        String arrival = arr.getText();
        String seatClass = (String) cbcls.getSelectedItem(); // First, Business, Economy
        String seatNo = (String) cbst.getSelectedItem();    // Selected seat

        if (flightNo.isEmpty() || seatClass == null || seatNo == null || userName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if seat is already booked (extra safety)
        Set<String> bookedSeats = BookingDAO.getBookedSeats(flightNo, seatClass);
        if (bookedSeats.contains(seatNo)) {
            JOptionPane.showMessageDialog(this, "Selected seat is already booked. Please select another seat.", "Seat Taken", JOptionPane.WARNING_MESSAGE);
            // Refresh seat list to remove booked seat
            return;
        }

        Booking booking = new Booking(flightNo, planeId, userId, userName, origin, destination, transit, departure, arrival, seatClass, seatNo);
        BookingDAO bookingDao = new BookingDAO();
        boolean success = bookingDao.saveBooking(booking);

        if (success) {
            JOptionPane.showMessageDialog(this, "Booking successful!");

            // Clear or reset form if needed
        } else {
            JOptionPane.showMessageDialog(this, "Booking failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        
    }//GEN-LAST:event_jButton6ActionPerformed

    private void uiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_uiActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        this.dispose();
        LoginPage login = new LoginPage();
        login.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(OperatorDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(OperatorDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(OperatorDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(OperatorDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new OperatorDashboard().setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel GraphP;
    private javax.swing.JComboBox<String> airportList;
    private javax.swing.JPanel analyticalPanel;
    private javax.swing.JTextField arr;
    private com.github.lgooddatepicker.components.DateTimePicker art;
    private javax.swing.JPanel availableFlightPanel;
    private javax.swing.JPanel bookForCustomerPanel;
    private javax.swing.JTable bookingT;
    private javax.swing.JComboBox<String> cbcls;
    private javax.swing.JComboBox<String> cbdes;
    private javax.swing.JComboBox<String> cbori;
    private javax.swing.JComboBox<String> cbst;
    private javax.swing.JTextField cusId;
    private javax.swing.JTextField dep;
    private com.github.lgooddatepicker.components.DateTimePicker dept;
    private javax.swing.JTextField des;
    private com.github.lgooddatepicker.components.DatePicker dtp;
    private javax.swing.JTable flightTable;
    private javax.swing.JTextField fn;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jt1;
    private javax.swing.JTextField or;
    private javax.swing.JButton pdb;
    private javax.swing.JTextField pi;
    private javax.swing.JTextField tr;
    private javax.swing.JTextField ui;
    private javax.swing.JTextField un;
    private javax.swing.JLabel usern;
    // End of variables declaration//GEN-END:variables
}
