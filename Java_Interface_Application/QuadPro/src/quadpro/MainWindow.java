/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quadpro;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import com.fazecast.jSerialComm.SerialPort;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import static quadpro.GLOBAL_CONSTANTS.ENTER_APP_MODE;
import static quadpro.GLOBAL_CONSTANTS.ENTER_TEST_MODE;
import static quadpro.GLOBAL_CONSTANTS.MAX_CAL_BUFFER_SIZE;

/**
 *
 * @author gsgur
 */
public class MainWindow implements GLOBAL_CONSTANTS {

    private JFrame mainWindow;
    private JTextArea mainOutput;
    private JScrollPane scrollTestArea; // Scrollarea for mainOutput
    private JTable reviewDataTable;
    private JScrollPane scrollTableArea; // Scrollarea for reviewDataTable
    private String Table_data[][];
    private Toolkit tlk_kit = Toolkit.getDefaultToolkit();

    SerialPort[] allPorts = SerialPort.getCommPorts();
    JComboBox<String> portList = new JComboBox<>();
    public static SerialPort chosenPort;
    boolean ScanForSerialData = false;
    Thread SerialDataScan;
    String LineReceivedFromDrone;

    //UI Components in status bar
    JLabel COM_selected = new JLabel("PORT: None");
    JLabel AccX = new JLabel(" Acc X: 0000");
    JLabel AccY = new JLabel(" Acc Y: 0000");
    JLabel AccZ = new JLabel(" Acc Z: 0000");
    JLabel GyroX = new JLabel(" Gyro X: 0000");
    JLabel GyroY = new JLabel(" Gyro Y: 0000");
    JLabel GyroZ = new JLabel(" Gyro Z: 0000");
    JCheckBox FreezeData = new JCheckBox("Freeze data");

    // Actual sensor data
    int Ax_;
    int Ay_;
    int Az_;
    int Gx_;
    int Gy_;
    int Gz_;

    //Custom data panel text field
    JTextField customDataTosend = new JTextField();

    // FOR CALIBRATION -----------------++
    public Integer[] CAL_AccX_array = new Integer[MAX_CAL_BUFFER_SIZE];
    public Integer[] CAL_AccY_array = new Integer[MAX_CAL_BUFFER_SIZE];
    public Integer[] CAL_AccZ_array = new Integer[MAX_CAL_BUFFER_SIZE];

    public Integer[] CAL_GyroX_array = new Integer[MAX_CAL_BUFFER_SIZE];
    public Integer[] CAL_GyroY_array = new Integer[MAX_CAL_BUFFER_SIZE];
    public Integer[] CAL_GyroZ_array = new Integer[MAX_CAL_BUFFER_SIZE];

    int CAL_array_indexer_Gx, CAL_array_indexer_Gy, CAL_array_indexer_Gz = 0;
    int CAL_array_indexer_Ax, CAL_array_indexer_Ay, CAL_array_indexer_Az = 0;

    // FOR CALIBRATION TAB (UI Components) -----------------++

    JTextField TextField_CAL_GyroX = new JTextField("Gx");
    JTextField TextField_CAL_GyroY = new JTextField("Gy");
    JTextField TextField_CAL_GyroZ = new JTextField("Gz");

    JTextField TextField_CAL_AccX = new JTextField("Accx");
    JTextField TextField_CAL_AccY = new JTextField("Accy");
    JTextField TextField_CAL_AccZ = new JTextField("Accz");
    
    JTextField TextField_CAL_GyroX_swing = new JTextField("Gx_swing");
    JTextField TextField_CAL_GyroY_swing = new JTextField("Gy_swing");
    JTextField TextField_CAL_GyroZ_swing = new JTextField("Gz_swing");

    JTextField TextField_CAL_AccX_swing = new JTextField("Accx_swing");
    JTextField TextField_CAL_AccY_swing = new JTextField("Accy_swing");
    JTextField TextField_CAL_AccZ_swing = new JTextField("Accz_swing");
    
    JLabel TotalDataForCal = new JLabel("Total Count:");
    
    SimpleMessage CAL_Progress_MsgBox;

    // ------------------------------------------
    MainWindow() {

        mainWindow = new JFrame();
        mainOutput = new JTextArea();

        //mainOutput.setText(TEXT_PANE_DEFAULT_TEXT);
        mainOutput.setFont(TEXT_PANE_FONT);

        CraftMainWindow();

        mainWindow.setSize(tlk_kit.getScreenSize().width / 2, tlk_kit.getScreenSize().height / 2);
        mainWindow.setVisible(true);
    }

    /* Design the UI FUNCTION */
 /*
        -> Design the Main Window
     */
    private final void CraftMainWindow() {

        mainWindow.setTitle(MAIN_WINDOW_TITLE);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setLayout(new BorderLayout());

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(TAB_TITLE_FONT);
        tabs.addTab(TESTING_TAB_TITLE, FlightTestControls());
        tabs.addTab(QUAD_CONTROLS_TAB_TITLE, ControlQuard());
        tabs.addTab(SEND_CUSTOM_DATA_TAB_TITLE, SendCustomDataPanel());
        tabs.addTab(CALIBRATION_TAB_TITLE, CalibrationTab());

        mainWindow.add(tabs, BorderLayout.NORTH);
        
        JPanel MainPanel = new JPanel();
        MainPanel.setLayout(new GridLayout(1,2));
        
        scrollTestArea = new JScrollPane(mainOutput);

        String[] colums = TABLE_COLUMNS;
        Table_data = new String[5*MAX_CAL_BUFFER_SIZE][TABLE_COLUMNS.length];
        
        reviewDataTable = new JTable(Table_data,colums);
        scrollTableArea = new JScrollPane(reviewDataTable);
        reviewDataTable.setAutoscrolls(true);
        
        MainPanel.add(scrollTestArea);
        
        JFrame tableWindow = new JFrame("Table");
        tableWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tableWindow.setSize(tlk_kit.getScreenSize().width / 2, tlk_kit.getScreenSize().height / 2);
        tableWindow.add(scrollTableArea);
        tableWindow.setVisible(true);

        
        mainWindow.add(MainPanel, BorderLayout.CENTER);
        mainWindow.add(StatusPanel(), BorderLayout.SOUTH);
    }

    /* For Debugging */
 /*
        -> Send any data panel
     */
    private final JPanel SendCustomDataPanel() {

        JPanel customDataPanel = new JPanel();
        JButton sendCustomData = new JButton("SEND NOW");

        customDataTosend.setColumns(30);
        customDataTosend.setFont(TEXT_PANE_FONT);

        sendCustomData.addActionListener(new Send_Now());

        customDataPanel.add(customDataTosend);
        customDataPanel.add(sendCustomData);

        return customDataPanel;

    }

    private final JPanel CalibrationTab() {
        JPanel CAL = new JPanel();

        TextField_CAL_GyroX.setColumns(5);
        TextField_CAL_GyroY.setColumns(5);
        TextField_CAL_GyroZ.setColumns(5);
        TextField_CAL_AccX.setColumns(5);
        TextField_CAL_AccY.setColumns(5);
        TextField_CAL_AccZ.setColumns(5);

        CAL.add(new JLabel(" CAL Gyro X:"));
        CAL.add(TextField_CAL_GyroX);
        CAL.add(new JLabel(" CAL Gyro Y:"));
        CAL.add(TextField_CAL_GyroY);
        CAL.add(new JLabel(" CAL Gyro Z:"));
        CAL.add(TextField_CAL_GyroZ);

        CAL.add(new JLabel(" CAL Acc X:"));
        CAL.add(TextField_CAL_AccX);
        CAL.add(new JLabel(" CAL Acc Y:"));
        CAL.add(TextField_CAL_AccY);
        CAL.add(new JLabel(" CAL Acc Z:"));
        CAL.add(TextField_CAL_AccZ);
        
        
        CAL.add(new JLabel(" CAL Acc X Swing:"));
        CAL.add(TextField_CAL_AccX_swing);
        CAL.add(new JLabel(" CAL Acc Y Swing:"));
        CAL.add(TextField_CAL_AccY_swing);
        CAL.add(new JLabel(" CAL Acc Z Swing:"));
        CAL.add(TextField_CAL_AccZ_swing);
        
        
        CAL.add(new JLabel(" CAL Gyro X Swing:"));
        CAL.add(TextField_CAL_GyroX_swing);
        CAL.add(new JLabel(" CAL Gyro Y Swing:"));
        CAL.add(TextField_CAL_GyroY_swing);
        CAL.add(new JLabel(" CAL Gyro Z Swing:"));
        CAL.add(TextField_CAL_GyroZ_swing);
        
        
        
        
        
        CAL.add(TotalDataForCal);

        return CAL;
    }

    /* DESIGN TEST QUAD TAB FUNCTION */
 /*
        -> Create all buttons and UI elements for test quad tab
     */
    private final JPanel FlightTestControls() {

        JPanel flightTestControls = new JPanel();
        JPanel rotarTestGroup = new JPanel();
        JPanel ConnectionStart = new JPanel();

        // Check and list all available COM ports
        for (SerialPort port : allPorts) {
            portList.addItem(port.getSystemPortName());
        }

        JButton RefreshPortList = new JButton("Refresh COM");
        JButton ConnectToCOMPort = new JButton("Connect !");
        JButton Enter_TestMode = new JButton("Test Mode");

        ConnectionStart.add(portList);
        ConnectionStart.add(RefreshPortList);
        ConnectionStart.add(ConnectToCOMPort);
        ConnectionStart.add(Enter_TestMode);

        TitledBorder title1 = new TitledBorder("Test Quad");
        title1.setTitleFont(BORDER_TITLE_FONT);
        rotarTestGroup.setBorder(title1);

        TitledBorder title2 = new TitledBorder("Get Started");
        title1.setTitleFont(BORDER_TITLE_FONT);
        ConnectionStart.setBorder(title2);

        ButtonGroup rotars_RadioGroup = new ButtonGroup();
        JRadioButton rotar_1 = new JRadioButton("Rotar 1");
        rotar_1.setSelected(true);
        rotar_1.setFont(GENERAL_FONT);
        JRadioButton rotar_2 = new JRadioButton("Rotar 2");
        rotar_2.setFont(GENERAL_FONT);
        JRadioButton rotar_3 = new JRadioButton("Rotar 3");
        rotar_3.setFont(GENERAL_FONT);
        JRadioButton rotar_4 = new JRadioButton("Rotar 4");
        rotar_4.setFont(GENERAL_FONT);

        rotars_RadioGroup.add(rotar_1);
        rotars_RadioGroup.add(rotar_2);
        rotars_RadioGroup.add(rotar_3);
        rotars_RadioGroup.add(rotar_4);

        JButton testRotar = new JButton("Check Rotar");
        JButton GetSensorData = new JButton("SR");

        rotarTestGroup.add(rotar_1);
        rotarTestGroup.add(rotar_2);
        rotarTestGroup.add(rotar_3);
        rotarTestGroup.add(rotar_4);
        rotarTestGroup.add(testRotar);
        rotarTestGroup.add(GetSensorData);

        JButton exitTest = new JButton("EXT");

        ConnectToCOMPort.addActionListener(new Connect());
        RefreshPortList.addActionListener(new PortRefresh());
        Enter_TestMode.addActionListener(new Enter_TEST_MODE());
        exitTest.addActionListener(new Exit_TEST_MODE());
        GetSensorData.addActionListener(new GetOneSensorDataInTestMode());

        flightTestControls.add(ConnectionStart);
        flightTestControls.add(rotarTestGroup);
        flightTestControls.add(exitTest);

        return flightTestControls;
    }

    /* MAIN CONTROLS UI FUNCTION */
 /*
        -> Create all buttons and UI elements to control the drone
     */
    private final JPanel ControlQuard() {
        JPanel controlQuad = new JPanel();

        JButton checkConnection = new JButton("Connect !");
        controlQuad.add(checkConnection);

        JButton left_button = new JButton("Slide Left >>>");
        JButton right_button = new JButton("Slide Right <<<");
        JButton forward_button = new JButton("Move Forward ^");

        controlQuad.add(left_button);
        controlQuad.add(right_button);
        controlQuad.add(forward_button);

        checkConnection.addActionListener(new Enter_APP_MODE());

        return controlQuad;
    }

    /* Status Panel UI FUNCTION */
 /*
        -> Create all buttons and UI elements for status panel
     */
    private final JPanel StatusPanel() throws NullPointerException {

        JPanel status_Panel = new JPanel();

        TitledBorder title1 = new TitledBorder("QUAD STATUS");
        title1.setTitleFont(BORDER_TITLE_FONT);
        status_Panel.setBorder(title1);

        JPanel Sensordata = new JPanel();
        TitledBorder title2 = new TitledBorder("Sensor Data");
        title2.setTitleFont(BORDER_TITLE_FONT);
        Sensordata.setBorder(title2);

        FreezeData.setSelected(false);
        
        JButton Populate_table = new JButton("Populate table");
        Populate_table.addActionListener(new Populate_table_class());

        Sensordata.add(AccX);
        Sensordata.add(AccY);
        Sensordata.add(AccZ);
        Sensordata.add(GyroX);
        Sensordata.add(GyroY);
        Sensordata.add(GyroZ);
        Sensordata.add(FreezeData);

        status_Panel.add(COM_selected);
        status_Panel.add(Sensordata);
        status_Panel.add(Populate_table);

        return status_Panel;
    }

    /* CONNECT FUNCTION */
 /*
        -> Connect to the selected port
     */
    class Connect implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            if (portList.getSelectedItem() == null) {
                mainOutput.append("\nNo Port selected");
            } else {
                chosenPort = SerialPort.getCommPort(portList.getSelectedItem().toString());
                chosenPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 10, 10);

                if (chosenPort.openPort()) {

                    // Display the selected COM port
                    COM_selected.setText("PORT: " + portList.getSelectedItem().toString());

                }

                SerialDataScan = new Thread(new ListenForIncommingSerialData());
                SerialDataScan.start();

            }
        }

    }

    /* REFRESH FUNCTION */
 /*
        -> Re Create the port list
     */
    class PortRefresh implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            // Remove all the items
            portList.removeAllItems();

            allPorts = SerialPort.getCommPorts();

            // Create a new item list
            for (SerialPort port : allPorts) {
                portList.addItem(port.getSystemPortName());
            }
        }

    }

    /* TEST MODE enter FUNCTION */
 /*
        -> sends the command to enter test mode
     */
    class Enter_TEST_MODE implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (chosenPort != null) {
                chosenPort.writeBytes(ENTER_TEST_MODE.getBytes(), ENTER_TEST_MODE.getBytes().length);
            }
        }

    }

    /* Listen on Port FUNCTION */
 /*
        -> Read serial data from serial buffer
     */
    class ListenForIncommingSerialData implements Runnable {

        @Override
        public void run() {
            Scanner read_from_comm = new Scanner(chosenPort.getInputStream());

            ScanForSerialData = true;

            while (read_from_comm.hasNextLine() && ScanForSerialData) {

                StringBuffer textFromComm = new StringBuffer();
                LineReceivedFromDrone = read_from_comm.nextLine();
                textFromComm.append(LineReceivedFromDrone + "\n");

                mainOutput.append(textFromComm.toString());

                DecodeIncommingMessage(LineReceivedFromDrone);

                mainOutput.setCaretPosition(mainOutput.getText().length());

            }

            mainOutput.append("Closed Thread");

            read_from_comm.close();
        }

    }

    /* Exit test mode FUNCTION */
 /*
        -> Send SR
     */
    class GetOneSensorDataInTestMode implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (chosenPort != null) {
                chosenPort.writeBytes(GET_ONE_SR_DATA.getBytes(), GET_ONE_SR_DATA.getBytes().length);
            }
        }

    }

    /* Exit test mode FUNCTION */
 /*
        -> Send the command to exit test mode
     */
    class Exit_TEST_MODE implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (chosenPort != null) {
                chosenPort.writeBytes(EXIT_TEST_MODE.getBytes(), EXIT_TEST_MODE.getBytes().length);
            }
        }

    }

    /* Enter application FUNCTION */
 /*
        -> Send the command to enter app mode
     */
    class Enter_APP_MODE implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (chosenPort != null) {
                chosenPort.writeBytes(ENTER_APP_MODE.getBytes(), ENTER_APP_MODE.getBytes().length);
            }
        }

    }

    /* Send your custom data to drone (Developer option) */
 /*
        -> read the incomming message and extract valid data
     */
    class Send_Now implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (chosenPort != null) {
                chosenPort.writeBytes(customDataTosend.getText().getBytes(), customDataTosend.getText().getBytes().length);
            }
        }

    }
    
    class Populate_table_class implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e) {
            for(int i=0; i<5*MAX_CAL_BUFFER_SIZE; i++)
                reviewDataTable.setValueAt(String.valueOf(i), i, 0);

        }
        
    }

    /* Decode and perform actions based on message received */
 /*
        -> read the incomming message and extract valid data
     */
    private void DecodeIncommingMessage(String message) {
        try {
            
            if(message.substring(0, 1).equals("W"))
            {
                
            }
            
            if (message.substring(0, 2).equals("Ax")) {
                Ax_ = Integer.parseInt(message.substring(2));

                if (!FreezeData.isSelected()) {
                    AccX.setText(" Acc X: " + Ax_);
                }
                
                FlightAlgorithms.AccX_array_ptr += 1;
                if(FlightAlgorithms.AccX_array_ptr >= MAX_BUFFER_SIZE) FlightAlgorithms.AccX_array_ptr = 0;
                FlightAlgorithms.AccX_array[FlightAlgorithms.AccX_array_ptr] = Ax_;
                reviewDataTable.setValueAt(String.valueOf(Ax_), FlightAlgorithms.AccX_array_ptr, ACC_X_TABLE_POS);
                
                
                FlightAlgorithms.P_error_AccX_Pointer_CURRENT += 1;
                if(FlightAlgorithms.P_error_AccX_Pointer_CURRENT >= MAX_ERROR_BUFFER_SIZE) FlightAlgorithms.P_error_AccX_Pointer_CURRENT = 0;
                FlightAlgorithms.P_error_AccX[FlightAlgorithms.P_error_AccX_Pointer_CURRENT] = Ax_ - FlightAlgorithms.Cal_Acc_X;
                
                if(Math.abs(FlightAlgorithms.P_error_AccX[FlightAlgorithms.P_error_AccX_Pointer_CURRENT]) <= 3 * FlightAlgorithms.Cal_Acc_X_swing)
                {
                    FlightAlgorithms.P_error_AccX_filtered[FlightAlgorithms.P_error_AccX_Pointer_CURRENT] = 0;
                } else {
                    
                    if(FlightAlgorithms.P_error_AccX[FlightAlgorithms.P_error_AccX_Pointer_CURRENT] < 0)
                    {
                        FlightAlgorithms.P_error_AccX_filtered[FlightAlgorithms.P_error_AccX_Pointer_CURRENT] = FlightAlgorithms.P_error_AccX[FlightAlgorithms.P_error_AccX_Pointer_CURRENT] + (3 * FlightAlgorithms.Cal_Acc_X_swing);
                    } else {
                        FlightAlgorithms.P_error_AccX_filtered[FlightAlgorithms.P_error_AccX_Pointer_CURRENT] = FlightAlgorithms.P_error_AccX[FlightAlgorithms.P_error_AccX_Pointer_CURRENT] - (3 * FlightAlgorithms.Cal_Acc_X_swing);
                    }

                }

                reviewDataTable.setValueAt(String.valueOf(FlightAlgorithms.P_error_AccX_filtered[FlightAlgorithms.P_error_AccX_Pointer_CURRENT]), FlightAlgorithms.P_error_AccX_Pointer_CURRENT, ERROR_ACC_X_TABLE_POS);
                //reviewDataTable.setValueAt(String.valueOf(FlightAlgorithms.P_error_AccX[FlightAlgorithms.P_error_AccX_Pointer_CURRENT]), FlightAlgorithms.P_error_AccX_Pointer_CURRENT, ERROR_ACC_Y_TABLE_POS);
                
            }

            if (message.substring(0, 2).equals("Ay")) {
                Ay_ = Integer.parseInt(message.substring(2));
                if (!FreezeData.isSelected()) {
                    AccY.setText(" Acc Y: " + Ay_);
                }
                
                FlightAlgorithms.AccY_array_ptr += 1;
                if(FlightAlgorithms.AccY_array_ptr >= MAX_BUFFER_SIZE) FlightAlgorithms.AccY_array_ptr = 0;
                FlightAlgorithms.AccY_array[FlightAlgorithms.AccY_array_ptr] = Ay_;
                reviewDataTable.setValueAt(String.valueOf(Ay_), FlightAlgorithms.AccY_array_ptr, ACC_Y_TABLE_POS);
                
                FlightAlgorithms.P_error_AccY_Pointer_CURRENT += 1;
                if(FlightAlgorithms.P_error_AccY_Pointer_CURRENT >= MAX_ERROR_BUFFER_SIZE) FlightAlgorithms.P_error_AccY_Pointer_CURRENT = 0;
                FlightAlgorithms.P_error_AccY[FlightAlgorithms.P_error_AccY_Pointer_CURRENT] = Ay_ - FlightAlgorithms.Cal_Acc_Y;
                                
                if(Math.abs(FlightAlgorithms.P_error_AccY[FlightAlgorithms.P_error_AccY_Pointer_CURRENT]) <= 3 * FlightAlgorithms.Cal_Acc_Y_swing)
                {
                    FlightAlgorithms.P_error_AccY_filtered[FlightAlgorithms.P_error_AccY_Pointer_CURRENT] = 0;
                } else {
                    
                    if(FlightAlgorithms.P_error_AccY[FlightAlgorithms.P_error_AccY_Pointer_CURRENT] < 0)
                    {
                        FlightAlgorithms.P_error_AccY_filtered[FlightAlgorithms.P_error_AccY_Pointer_CURRENT] = FlightAlgorithms.P_error_AccY[FlightAlgorithms.P_error_AccY_Pointer_CURRENT] + (3 * FlightAlgorithms.Cal_Acc_Y_swing);
                    } else {
                        FlightAlgorithms.P_error_AccY_filtered[FlightAlgorithms.P_error_AccY_Pointer_CURRENT] = FlightAlgorithms.P_error_AccY[FlightAlgorithms.P_error_AccY_Pointer_CURRENT] - (3 * FlightAlgorithms.Cal_Acc_Y_swing);
                    }

                }

               reviewDataTable.setValueAt(String.valueOf(FlightAlgorithms.P_error_AccY_filtered[FlightAlgorithms.P_error_AccY_Pointer_CURRENT]), FlightAlgorithms.P_error_AccY_Pointer_CURRENT, ERROR_ACC_Y_TABLE_POS);
                                
            }

            if (message.substring(0, 2).equals("Az")) {
                Az_ = Integer.parseInt(message.substring(2));
                if (!FreezeData.isSelected()) {
                    AccZ.setText(" Acc Z: " + Az_);
                }
                
                FlightAlgorithms.AccZ_array_ptr += 1;
                if(FlightAlgorithms.AccZ_array_ptr >= MAX_BUFFER_SIZE) FlightAlgorithms.AccZ_array_ptr = 0;
                FlightAlgorithms.AccZ_array[FlightAlgorithms.AccZ_array_ptr] = Az_;
                reviewDataTable.setValueAt(String.valueOf(Az_), FlightAlgorithms.AccZ_array_ptr, ACC_Z_TABLE_POS);

                FlightAlgorithms.P_error_AccZ_Pointer_CURRENT += 1;
                if(FlightAlgorithms.P_error_AccZ_Pointer_CURRENT >= MAX_ERROR_BUFFER_SIZE) FlightAlgorithms.P_error_AccZ_Pointer_CURRENT = 0;
                FlightAlgorithms.P_error_AccZ[FlightAlgorithms.P_error_AccZ_Pointer_CURRENT] = Az_ - FlightAlgorithms.Cal_Acc_Z;
                
                if(Math.abs(FlightAlgorithms.P_error_AccZ[FlightAlgorithms.P_error_AccZ_Pointer_CURRENT]) <= 3 * FlightAlgorithms.Cal_Acc_Z_swing)
                {
                    FlightAlgorithms.P_error_AccZ_filtered[FlightAlgorithms.P_error_AccZ_Pointer_CURRENT] = 0;
                } else {
                    
                    if(FlightAlgorithms.P_error_AccZ[FlightAlgorithms.P_error_AccZ_Pointer_CURRENT] < 0)
                    {
                        FlightAlgorithms.P_error_AccZ_filtered[FlightAlgorithms.P_error_AccZ_Pointer_CURRENT] = FlightAlgorithms.P_error_AccZ[FlightAlgorithms.P_error_AccZ_Pointer_CURRENT] + (3 * FlightAlgorithms.Cal_Acc_Z_swing);
                    } else {
                        FlightAlgorithms.P_error_AccZ_filtered[FlightAlgorithms.P_error_AccZ_Pointer_CURRENT] = FlightAlgorithms.P_error_AccZ[FlightAlgorithms.P_error_AccZ_Pointer_CURRENT] - (3 * FlightAlgorithms.Cal_Acc_Z_swing);
                    }

                }
                                
                reviewDataTable.setValueAt(String.valueOf(FlightAlgorithms.P_error_AccZ_filtered[FlightAlgorithms.P_error_AccZ_Pointer_CURRENT]), FlightAlgorithms.P_error_AccZ_Pointer_CURRENT, ERROR_ACC_Z_TABLE_POS);
                                
            }

            if (message.substring(0, 2).equals("Gx")) {
                Gx_ = Integer.parseInt(message.substring(2));
                if (!FreezeData.isSelected()) {
                    GyroX.setText(" Gyro X: " + Gx_);
                }
                
                FlightAlgorithms.GyroX_array_ptr += 1;
                if(FlightAlgorithms.GyroX_array_ptr >= MAX_BUFFER_SIZE) FlightAlgorithms.GyroX_array_ptr = 0;
                FlightAlgorithms.GyroX_array[FlightAlgorithms.GyroX_array_ptr] = Gx_;
                reviewDataTable.setValueAt(String.valueOf(Gx_), FlightAlgorithms.GyroX_array_ptr, GYRO_X_TABLE_POS);
                
            }

            if (message.substring(0, 2).equals("Gy")) {
                Gy_ = Integer.parseInt(message.substring(2));
                if (!FreezeData.isSelected()) {
                    GyroY.setText(" Gyro y: " + Gy_);
                }
                
                FlightAlgorithms.GyroY_array_ptr += 1;
                if(FlightAlgorithms.GyroY_array_ptr >= MAX_BUFFER_SIZE) FlightAlgorithms.GyroY_array_ptr = 0;
                FlightAlgorithms.GyroY_array[FlightAlgorithms.GyroY_array_ptr] = Gy_;
                reviewDataTable.setValueAt(String.valueOf(Gy_), FlightAlgorithms.GyroY_array_ptr, GYRO_Y_TABLE_POS);
                
            }

            if (message.substring(0, 2).equals("Gz")) {
                Gz_ = Integer.parseInt(message.substring(2));
                if (!FreezeData.isSelected()) {
                    GyroZ.setText(" Gyro Z: " + Gz_);
                }
                
                FlightAlgorithms.GyroZ_array_ptr += 1;
                if(FlightAlgorithms.GyroZ_array_ptr >= MAX_BUFFER_SIZE) FlightAlgorithms.GyroZ_array_ptr = 0;
                FlightAlgorithms.GyroZ_array[FlightAlgorithms.GyroZ_array_ptr] = Gz_;
                reviewDataTable.setValueAt(String.valueOf(Gz_), FlightAlgorithms.GyroZ_array_ptr, GYRO_Z_TABLE_POS);
                
            }

            // CALIBRATION Data Receive ----------------------------------------------------------------------
            
            if (message.length() >= 9)
            {
                if (message.substring(0, 9).equals("START_CAL")) {
                    CAL_Progress_MsgBox = new SimpleMessage("Calibration in Progress ", "CALIBRATION COMPLETE !!", true, 0, MAX_CAL_BUFFER_SIZE);
                }
            }
            
            
            if (message.length() >= 6) {
                
                // Gyro cal
                
                if (message.substring(0, 6).equals("CAL_Gx") && CAL_array_indexer_Gx < MAX_CAL_BUFFER_SIZE) {
                    CAL_GyroX_array[CAL_array_indexer_Gx] = Integer.parseInt(message.substring(6));
                    reviewDataTable.setValueAt(String.valueOf(CAL_GyroX_array[CAL_array_indexer_Gx].intValue()), CAL_array_indexer_Gx, CAL_GYRO_X_TABLE_POS);
                    CAL_array_indexer_Gx++;
                    
                    TotalDataForCal.setText("Count"+CAL_array_indexer_Gx);
                    CAL_Progress_MsgBox.CAL_Progress.setValue(CAL_array_indexer_Gx);
                }

                if (message.substring(0, 6).equals("CAL_Gy") && CAL_array_indexer_Gy < MAX_CAL_BUFFER_SIZE) {
                    CAL_GyroY_array[CAL_array_indexer_Gy] = Integer.parseInt(message.substring(6));
                    reviewDataTable.setValueAt(String.valueOf(CAL_GyroY_array[CAL_array_indexer_Gy].intValue()), CAL_array_indexer_Gy, CAL_GYRO_Y_TABLE_POS);
                    CAL_array_indexer_Gy++;
                    
                    TotalDataForCal.setText("Count"+CAL_array_indexer_Gy);
                    CAL_Progress_MsgBox.CAL_Progress.setValue(CAL_array_indexer_Gy);
                }

                if (message.substring(0, 6).equals("CAL_Gz") && CAL_array_indexer_Gz < MAX_CAL_BUFFER_SIZE) {
                    CAL_GyroZ_array[CAL_array_indexer_Gz] = Integer.parseInt(message.substring(6));
                    reviewDataTable.setValueAt(String.valueOf(CAL_GyroZ_array[CAL_array_indexer_Gz].intValue()), CAL_array_indexer_Gz, CAL_GYRO_Z_TABLE_POS);
                    CAL_array_indexer_Gz++;
                    
                    TotalDataForCal.setText("Count"+CAL_array_indexer_Gz);
                    CAL_Progress_MsgBox.CAL_Progress.setValue(CAL_array_indexer_Gz);
                }
                
                // Acc Cal
                
                if (message.substring(0, 6).equals("CAL_Ax") && CAL_array_indexer_Ax < MAX_CAL_BUFFER_SIZE) {
                    CAL_AccX_array[CAL_array_indexer_Ax] = Integer.parseInt(message.substring(6));
                    reviewDataTable.setValueAt(String.valueOf(CAL_AccX_array[CAL_array_indexer_Ax].intValue()), CAL_array_indexer_Ax, CAL_ACC_X_TABLE_POS);
                    CAL_array_indexer_Ax++;
                    
                    TotalDataForCal.setText("Count"+CAL_array_indexer_Ax);
                    CAL_Progress_MsgBox.CAL_Progress.setValue(CAL_array_indexer_Ax);
                }

                if (message.substring(0, 6).equals("CAL_Ay") && CAL_array_indexer_Ay < MAX_CAL_BUFFER_SIZE) {
                    CAL_AccY_array[CAL_array_indexer_Ay] = Integer.parseInt(message.substring(6));
                    reviewDataTable.setValueAt(String.valueOf(CAL_AccY_array[CAL_array_indexer_Ay].intValue()), CAL_array_indexer_Ay, CAL_ACC_Y_TABLE_POS);
                    CAL_array_indexer_Ay++;
                    
                    TotalDataForCal.setText("Count"+CAL_array_indexer_Ay);
                    CAL_Progress_MsgBox.CAL_Progress.setValue(CAL_array_indexer_Ay);
                }

                if (message.substring(0, 6).equals("CAL_Az") && CAL_array_indexer_Az < MAX_CAL_BUFFER_SIZE) {
                    CAL_AccZ_array[CAL_array_indexer_Az] = Integer.parseInt(message.substring(6));
                    reviewDataTable.setValueAt(String.valueOf(CAL_AccZ_array[CAL_array_indexer_Az].intValue()), CAL_array_indexer_Az, CAL_ACC_Z_TABLE_POS);
                    CAL_array_indexer_Az++;
                    
                    TotalDataForCal.setText("Count"+CAL_array_indexer_Az);
                    CAL_Progress_MsgBox.CAL_Progress.setValue(CAL_array_indexer_Az);
                }
            }

            if (message.length() >= 8) {
                if (message.substring(0, 8).equals("CALC_CAL")) {

                    FlightAlgorithms.Cal_Gyro_X = Stats.mean(CAL_GyroX_array);
                    FlightAlgorithms.Cal_Gyro_Y  = Stats.mean(CAL_GyroY_array);
                    FlightAlgorithms.Cal_Gyro_Z  = Stats.mean(CAL_GyroZ_array);
                    
                    FlightAlgorithms.Cal_Acc_X = Stats.mean(CAL_AccX_array);
                    FlightAlgorithms.Cal_Acc_Y = Stats.mean(CAL_AccY_array);
                    FlightAlgorithms.Cal_Acc_Z = Stats.mean(CAL_AccZ_array);

                    FlightAlgorithms.Cal_Gyro_X_swing = Stats.StandardDeviation(CAL_GyroX_array)[0];
                    FlightAlgorithms.Cal_Gyro_Y_swing = Stats.StandardDeviation(CAL_GyroY_array)[0];
                    FlightAlgorithms.Cal_Gyro_Z_swing = Stats.StandardDeviation(CAL_GyroZ_array)[0];
                    
                    FlightAlgorithms.Cal_Acc_X_swing = Stats.StandardDeviation(CAL_AccX_array)[0];
                    FlightAlgorithms.Cal_Acc_Y_swing = Stats.StandardDeviation(CAL_AccY_array)[0];
                    FlightAlgorithms.Cal_Acc_Z_swing = Stats.StandardDeviation(CAL_AccZ_array)[0];

                    // Update the UI elements
                    TextField_CAL_GyroX.setText("" + FlightAlgorithms.Cal_Gyro_X);
                    TextField_CAL_GyroY.setText("" + FlightAlgorithms.Cal_Gyro_Y);
                    TextField_CAL_GyroZ.setText("" + FlightAlgorithms.Cal_Gyro_Z);
                    
                    TextField_CAL_AccX.setText("" + FlightAlgorithms.Cal_Acc_X);
                    TextField_CAL_AccY.setText("" + FlightAlgorithms.Cal_Acc_Y);
                    TextField_CAL_AccZ.setText("" + FlightAlgorithms.Cal_Acc_Z);
                    
                    TextField_CAL_GyroX_swing.setText("" + FlightAlgorithms.Cal_Gyro_X_swing);
                    TextField_CAL_GyroY_swing.setText("" + FlightAlgorithms.Cal_Gyro_Y_swing);
                    TextField_CAL_GyroZ_swing.setText("" + FlightAlgorithms.Cal_Gyro_Z_swing);
                    
                    TextField_CAL_AccX_swing.setText("" + FlightAlgorithms.Cal_Acc_X_swing);
                    TextField_CAL_AccY_swing.setText("" + FlightAlgorithms.Cal_Acc_Y_swing);
                    TextField_CAL_AccZ_swing.setText("" + FlightAlgorithms.Cal_Acc_Z_swing);

                  //Show the message by hiding the progress bar
                    CAL_Progress_MsgBox.msgBox.setTitle("COMPLETED");

                }
            }

        } catch (StringIndexOutOfBoundsException ex) {
            // If you receive a message smaller than the max
            mainOutput.append("Exception caught :( \n");
        }

    }

}
