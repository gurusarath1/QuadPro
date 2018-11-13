#include "G:\Guru_Sarath\Study\K2\Main_Arduino_Code\MainCode\Define_Constants.ino"
#include <Servo.h>

extern int serialWaitStr(int length_of_string);
extern bool checkIfReceived(char c[], int length);

extern char RX_buffer[SOFTWARE_RX_BUFFER_SIZE];

int selected_rotor = 0;
void SeriallySendMPUData();
void MPU_ReadAll();

char ROTOR_1[] = "R1";
char ROTOR_2[] = "R2";
char ROTOR_3[] = "R3";
char ROTOR_4[] = "R4";
char ALL_ROTORS[] = "AR";
char GET_SENSOR_DATA[] = "SR";
char EXIT_TEST_MODE[] = {"EXT"};

extern Servo esc_signal_motor_1;
extern Servo esc_signal_motor_2;
extern Servo esc_signal_motor_3;
extern Servo esc_signal_motor_4;

void testMode()
{
  Serial.println("\n\nWaiting for test mode commands > ")
  while(1)
  {
    serialWaitStr(6);

    if(checkIfReceived(ALL_ROTORS, 2))
    {
      Serial.println("Rotor X");
      esc_signal_motor_1.write(RX_buffer[2]);
      esc_signal_motor_2.write(RX_buffer[3]);
      esc_signal_motor_3.write(RX_buffer[4]);
      esc_signal_motor_4.write(RX_buffer[5]);
    }
    
    if(checkIfReceived(ROTOR_1, 2))
    {
      selected_rotor = 1;
      Serial.println("Rotor 1 selected");
      esc_signal_motor_1.write(RX_buffer[2]);
    }

    if(checkIfReceived(ROTOR_2, 2))
    {
      selected_rotor = 2;
      Serial.println("Rotor 2 selected");
      esc_signal_motor_2.write(RX_buffer[2]);
    }

    if(checkIfReceived(ROTOR_3, 2))
    {
      selected_rotor = 3; 
      Serial.println("Rotor 3 selected");
      esc_signal_motor_3.write(RX_buffer[2]);
    }

    if(checkIfReceived(ROTOR_4, 2))
    {
      selected_rotor = 4;
      Serial.println("Rotor 4 selected");
      esc_signal_motor_4.write(RX_buffer[2]);
    }

    if(checkIfReceived(GET_SENSOR_DATA, 2))
    {
        MPU_ReadAll();
        SeriallySendMPUData();
    }

    if(checkIfReceived(EXIT_TEST_MODE, 3))
    {
      Serial.println("Exit To Application wait");
      break;
    }

    
  }
}

