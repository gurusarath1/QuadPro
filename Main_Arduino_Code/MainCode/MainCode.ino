#include "G:\Guru_Sarath\Study\K2\Main_Arduino_Code\MainCode\Define_Constants.ino"
#include <Servo.h>

// Declarations of this file
Servo esc_signal_motor_1;
Servo esc_signal_motor_2;
Servo esc_signal_motor_3;
Servo esc_signal_motor_4;
void SeriallySendMPUData();
void initDrone();
void toggleHeartBeat();
void flightControl();
void CheckFaultCode();


extern char RX_buffer[SOFTWARE_RX_BUFFER_SIZE];

extern void testMode();
extern int serialWaitStr(int length_of_string);
extern bool checkIfReceived(char c[], int length);

extern int16_t AccX,AccY,AccZ,Temp,GyroX,GyroY,GyroZ;
extern void setUpI2C_MPU();
extern void MPU_ReadAll();


char TEST_MODE_ENTER_9[] = "TEST_MODE";
char APP_MODE_3[] = "APP";
char ROTOR_CONTROL_WORD[] = "R1234R";

#define TEST_MODE_PIN 13
#define HEART_BEAT_PIN 3

#define ESC_MOTOR_1 8
#define ESC_MOTOR_2 9
#define ESC_MOTOR_3 10
#define ESC_MOTOR_4 11



bool testModeEntered = false;
bool Inflight = false;
int STABLE_X =0;
int STABLE_Y =0;
int STABLE_Z =0;


int FaultIndicated = 0;
int NoRotorControlAge = 0;
int SerialWaitAge = 0;


void setup()
{
  Serial.begin(9600);

    // Initialize the pins
  pinMode(TEST_MODE_PIN, OUTPUT);
  pinMode(HEART_BEAT_PIN, OUTPUT);  

  delay(1000); 

  // ----------------------------------------------------------------------------------------------------------------------------

  SerialSeparator;
  // First data to be sent
  Serial.println(HARDWARE_PART_NUMBER);
  Serial.println(SOFTWARE_PART_NUMBER);
  Serial.println(DRONE_NAME);
  SerialSeparator;

  // Wait to receive a Drone testing mode request
  serialWaitStr(9);

  // Check if you received the testing mode request
  if(checkIfReceived(TEST_MODE_ENTER_9, 9))
  {
      Serial.println("TEST DRONE ------- \n");
      digitalWrite(TEST_MODE_PIN, HIGH);
      testModeEntered = true;

      // Initialise drone for flight
      initDrone();

      // Enter into testing mode
      testMode();
  }

  // Drone is out of testing mode
  digitalWrite(TEST_MODE_PIN, LOW);

  // Wait for launch request
  serialWaitStr(3);

  if(checkIfReceived(APP_MODE_3, 3))
  {
     Serial.println("Initiating launch ... ");

    // Initialize drone only if test mode wasnt entered
     if(testModeEntered == false)
     {
        initDrone();
     }
     
  } else {

    Serial.println("NO VALID KEY RECEIVED (RESET REQUIRED)");

    while(1);
    
  }
  
}

void loop()
{
  // Main application code
  Serial.println("Be ready for lift off ... ");

  toggleHeartBeat();

  for(int i=0; i<50; i++)
  {
    MPU_ReadAll();
    SeriallySendMPUData();
  }

  Serial.println("CONTROL ... !!");
  
  Inflight = true;

  // Drone Control Loop
  for(;;)
  {
    toggleHeartBeat();

    MPU_ReadAll();
    SeriallySendMPUData();
    
    Serial.println("W"); //This the wait command sent by drone to indicate waiting for response
    
    // Wait for command from Application software
    serialWaitStr(9);
    flightControl();

    // If any fault is detected, initiate landing
    if(FaultIndicated) break;
    
  }

  Serial.println("SYSTEM FAIL ... !!");

  // Check the reason for failure
  CheckFaultCode();

  //Do nothing // Fail safe mode
  while(1);
  
  
}

void SeriallySendMPUData()
{
  Serial.print("Ax");
  Serial.println(AccX);
  Serial.print("Ay");
  Serial.println(AccY);
  Serial.print("Az");
  Serial.println(AccZ);
  Serial.print("Gx");
  Serial.println(GyroX);
  Serial.print("Gy");
  Serial.println(GyroY);
  Serial.print("Gz");
  Serial.println(GyroZ);
}

void SeriallySendMPUData_CAL()
{
  Serial.print("CAL_Gx");
  Serial.println(GyroX);
  Serial.print("CAL_Gy");
  Serial.println(GyroY);
  Serial.print("CAL_Gz");
  Serial.println(GyroZ);
  
  Serial.print("CAL_Ax");
  Serial.println(AccX);
  Serial.print("CAL_Ay");
  Serial.println(AccY);
  Serial.print("CAL_Az");
  Serial.println(AccZ);

}

/*
 *  Initialize Drone
 * 
 * -> Initialize MPU6050
 * -> Perform Calibration to determine stable position
 * 
 */
void initDrone()
{

  // Initialize Motors
  Serial.println("Initialize BLDC Motors ... ");

  Serial.println("Motor 1 ... ");
  esc_signal_motor_1.attach(ESC_MOTOR_1);  //Specify here the pin number on which the signal pin of ESC is connected.
  esc_signal_motor_1.write(30);            //ESC arm command. ESCs won't start unless input speed is less during initialization.
  delay(1000);                             //ESC initialization delay.
  
  Serial.println("Motor 2 ... ");
  esc_signal_motor_2.attach(ESC_MOTOR_2);  //Specify here the pin number on which the signal pin of ESC is connected.
  esc_signal_motor_2.write(30);            //ESC arm command. ESCs won't start unless input speed is less during initialization.
  delay(1000);                             //ESC initialization delay.
  
  Serial.println("Motor 3 ... ");
  esc_signal_motor_3.attach(ESC_MOTOR_3);  //Specify here the pin number on which the signal pin of ESC is connected.
  esc_signal_motor_3.write(30);            //ESC arm command. ESCs won't start unless input speed is less during initialization.
  delay(1000);                             //ESC initialization delay.
  
  Serial.println("Motor 4 ... ");
  esc_signal_motor_4.attach(ESC_MOTOR_4);  //Specify here the pin number on which the signal pin of ESC is connected.
  esc_signal_motor_4.write(30);            //ESC arm command. ESCs won't start unless input speed is less during initialization.
  delay(3000);                             //ESC initialization delay.

  Serial.println("Motor init complete ... ");
  
  // Set up the accelerometer and gyro
  Serial.println("MPU6050 init ... ");
  setUpI2C_MPU();
  Serial.println("MPU6050 init Complete !!! ....");
  delay(1000);
  Serial.println("START_CAL");
  Serial.println("Calibration in progress ... ");

  // calibration Loop
  for(int i = 0; i<CAL_DATA_MAX_SEND; i++)
  {
    MPU_ReadAll();
    SeriallySendMPUData_CAL();
  }
  // Send message to do the calculations
  Serial.println("CALC_CAL");
  Serial.println("Drone Init Complete ... ");
  delay(1000);

}

void toggleHeartBeat()
{
  //Toggle the heart beat LED
  digitalRead(HEART_BEAT_PIN) == LOW ? digitalWrite(HEART_BEAT_PIN, HIGH) : digitalWrite(HEART_BEAT_PIN, LOW);
}

void flightControl()
{
    if(checkIfReceived(ROTOR_CONTROL_WORD, 6))
    {
      
      NoRotorControlAge = 0;
      
      esc_signal_motor_1.write(RX_buffer[6]);

      esc_signal_motor_2.write(RX_buffer[7]);

      esc_signal_motor_3.write(RX_buffer[8]);
 
      esc_signal_motor_4.write(RX_buffer[9]);
      
    } else {
      
      NoRotorControlAge++;
      if(NoRotorControlAge >= 500) {FaultIndicated = 2;}
      
   }

}

void CheckFaultCode()
{
  switch(FaultIndicated)
  {
    case 1:
      Serial.println("No response from main application ... !!");
      break;

    case 2:
      Serial.println("No PID response for long time ... !!");
      break;

    default:
      Serial.println("Unknown error ... !!");
      break;
  }
}

