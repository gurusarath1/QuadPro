/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quadpro;

/**
 *
 * @author gsgur
 */
public class FlightAlgorithms implements GLOBAL_CONSTANTS {

    // Calibration values
    public static double Cal_Acc_X = 0;
    public static double Cal_Acc_Y = 0;
    public static double Cal_Acc_Z = 0;

    public static double Cal_Gyro_X = 0;
    public static double Cal_Gyro_Y = 0;
    public static double Cal_Gyro_Z = 0;
    
    public static double Cal_Acc_X_swing = 0;
    public static double Cal_Acc_Y_swing = 0;
    public static double Cal_Acc_Z_swing = 0;

    public static double Cal_Gyro_X_swing = 0;
    public static double Cal_Gyro_Y_swing = 0;
    public static double Cal_Gyro_Z_swing = 0;
    
    //Gains
    float P_Gain = P_GAIN_DEFAULT;
    float I_Gain = I_GAIN_DEFAULT;
    float D_Gain = D_GAIN_DEFAULT;

    // Real time errors PID Accelerometer
    public static double P_error_Ax = 0;
    public static double I_error_Ax = 0;
    public static double D_error_Ax = 0;

    public static double P_error_Ay = 0;
    public static double I_error_Ay = 0;
    public static double D_error_Ay = 0;

    public static double P_error_Az = 0;
    public static double I_error_Az = 0;
    public static double D_error_Az = 0;
    
    // Real time errors PID Gyro
    public static double P_error_Gx = 0;
    public static double I_error_Gx = 0;
    public static double D_error_Gx = 0;

    public static double P_error_Gy = 0;
    public static double I_error_Gy = 0;
    public static double D_error_Gy = 0;

    public static double P_error_Gz = 0;
    public static double I_error_Gz = 0;
    public static double D_error_Gz = 0;

    // Pointers to current error
    public static Integer P_error_AccX_Pointer_CURRENT = -1; 
    public static Integer P_error_AccY_Pointer_CURRENT = -1;
    public static Integer P_error_AccZ_Pointer_CURRENT = -1;
    
    public static Integer P_error_GyroX_Pointer_CURRENT = -1;
    public static Integer P_error_GyroY_Pointer_CURRENT = -1;
    public static Integer P_error_GyroZ_Pointer_CURRENT = -1;

    // Data array
    public static int[] AccX_array = new int[MAX_BUFFER_SIZE];
    public static int[] AccY_array = new int[MAX_BUFFER_SIZE];
    public static int[] AccZ_array = new int[MAX_BUFFER_SIZE];
     
    public static int AccX_array_ptr = -1;
    public static int AccY_array_ptr = -1;
    public static int AccZ_array_ptr = -1;

    public static int[] GyroX_array = new int[MAX_BUFFER_SIZE];
    public static int[] GyroY_array = new int[MAX_BUFFER_SIZE];
    public static int[] GyroZ_array = new int[MAX_BUFFER_SIZE];
    
    public static int GyroX_array_ptr = -1;
    public static int GyroY_array_ptr = -1;
    public static int GyroZ_array_ptr = -1;

    //P error array
    public static double[] P_error_AccX = new double[MAX_ERROR_BUFFER_SIZE];
    public static double[] P_error_AccY = new double[MAX_ERROR_BUFFER_SIZE];
    public static double[] P_error_AccZ = new double[MAX_ERROR_BUFFER_SIZE];
    
    public static double[] P_error_GyroX = new double[MAX_ERROR_BUFFER_SIZE];
    public static double[] P_error_GyroY = new double[MAX_ERROR_BUFFER_SIZE];
    public static double[] P_error_GyroZ = new double[MAX_ERROR_BUFFER_SIZE];
    
    // Filtered P error array
    public static double[] P_error_AccX_filtered = new double[MAX_ERROR_BUFFER_SIZE];
    public static double[] P_error_AccY_filtered = new double[MAX_ERROR_BUFFER_SIZE];
    public static double[] P_error_AccZ_filtered = new double[MAX_ERROR_BUFFER_SIZE];
    
    public static double[] P_error_GyroX_filtered = new double[MAX_ERROR_BUFFER_SIZE];
    public static double[] P_error_GyroY_filtered = new double[MAX_ERROR_BUFFER_SIZE];
    public static double[] P_error_GyroZ_filtered = new double[MAX_ERROR_BUFFER_SIZE];     
    
    // Filtered P error array NORMALIZED 0 - 100
    public static double[] P_error_AccX_filtered_NORMALIZED = new double[MAX_ERROR_BUFFER_SIZE];
    public static double[] P_error_AccY_filtered_NORMALIZED = new double[MAX_ERROR_BUFFER_SIZE];
    public static double[] P_error_AccZ_filtered_NORMALIZED = new double[MAX_ERROR_BUFFER_SIZE];
    
    public static double[] P_error_GyroX_filtered_NORMALIZED = new double[MAX_ERROR_BUFFER_SIZE];
    public static double[] P_error_GyroY_filtered_NORMALIZED = new double[MAX_ERROR_BUFFER_SIZE];
    public static double[] P_error_GyroZ_filtered_NORMALIZED = new double[MAX_ERROR_BUFFER_SIZE];     

    public static int PID_Acc() {


        
        return 0;
    }
    

    


    public static void setSpeeds() {

    }
    
    public static void calibration()
    {
        
    }

}
