/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quadpro;

import java.awt.Font;

/**
 *
 * @author gsgur
 */
public interface GLOBAL_CONSTANTS {
    String VERSION = "1.0";
    String AUTHORS = "Guru Sarath & Priyanka ";
    String MAIN_WINDOW_TITLE = "Quad Pro " + VERSION + " " + AUTHORS ;
    
    String TESTING_TAB_TITLE = "Test Quad";
    String QUAD_CONTROLS_TAB_TITLE = "Control Quad";
    String SEND_CUSTOM_DATA_TAB_TITLE = "Send Data";
    String CALIBRATION_TAB_TITLE = "Calibration";
    
    Font TAB_TITLE_FONT = new Font(Font.DIALOG, Font.BOLD, 20);
    Font GENERAL_FONT = new Font(Font.DIALOG, Font.BOLD, 15);
    Font BORDER_TITLE_FONT = new Font(Font.SERIF, Font.BOLD, 20);
    
    String TEXT_PANE_DEFAULT_TEXT = "Quad Pro " + VERSION;
    Font TEXT_PANE_FONT = new Font(Font.MONOSPACED, Font.BOLD, 15);
    
    String ENTER_TEST_MODE = "TEST_MODE";
    String GET_ONE_SR_DATA = "SR";
    String EXIT_TEST_MODE = "EXT";
    String ENTER_APP_MODE = "APP";
    
    int MAX_BUFFER_SIZE = 50;
    int MAX_ERROR_BUFFER_SIZE = 25;
    int MAX_CAL_BUFFER_SIZE = 200;
    
    float P_GAIN_DEFAULT = 1;
    float I_GAIN_DEFAULT = 5;
    float D_GAIN_DEFAULT = 2;
    
    String TABLE_COLUMNS[] = {"#","GyroX_array","GyroY_array","GyroZ_array","AccX_array","AccY_array","AccZ_array","---","P_error_AccX", "P_error_AccY", "P_error_AccZ","---","CAL_GyroX_array","CAL_GyroY_array","CAL_GyroZ_array", "CAL_AccX_array","CAL_AccY_array","CAL_AccZ_array"};
    int GYRO_X_TABLE_POS = 1;
    int GYRO_Y_TABLE_POS = 2;
    int GYRO_Z_TABLE_POS = 3;
    int ACC_X_TABLE_POS = 4;
    int ACC_Y_TABLE_POS = 5;
    int ACC_Z_TABLE_POS = 6;
    
    int ERROR_ACC_X_TABLE_POS = 8;
    int ERROR_ACC_Y_TABLE_POS = 9;
    int ERROR_ACC_Z_TABLE_POS = 10;
    
    int CAL_GYRO_X_TABLE_POS = 12;
    int CAL_GYRO_Y_TABLE_POS = 13;
    int CAL_GYRO_Z_TABLE_POS = 14;
    
    int CAL_ACC_X_TABLE_POS = 15;
    int CAL_ACC_Y_TABLE_POS = 16;
    int CAL_ACC_Z_TABLE_POS = 17;
    
    
}
