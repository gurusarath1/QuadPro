#include "G:\Guru_Sarath\Study\K2\Main_Arduino_Code\MainCode\Define_Constants.ino"
#include <Servo.h>

char RX_buffer[SOFTWARE_RX_BUFFER_SIZE];
int serialWaitStr(int length_of_string);
bool checkIfReceived(char c[], int length);
void clearRX_Buffer();

extern int SerialWaitAge;
extern bool Inflight;

int serialWaitStr(int length_of_string)
{
  int i = 0;

  // Clear the software receive buffer
  clearRX_Buffer();

  // Wait till you receive some data in the serial buffer
  while(!Serial.available())
  {
    SerialWaitAge++;

    // If you dont receive data for long time, indicate the fault and initiate landing
    if(SerialWaitAge == 10000 && Inflight) {FaultIndicated = 1; break;}
  }

  // Observation: It takes 1ms to receive a character
  delay(length_of_string); 
  //one ms for each char

  // Read all the available data
  while(Serial.available() > 0)
  {
    SerialWaitAge = 0;
    RX_buffer[i] = (char)Serial.read();
    //Serial.println(RX_buffer[i]);
    i++;
  }

  //Return the number of bytes read
  return i;
}

bool checkIfReceived(char c[], int length)
{
  int i = 0;
  bool flag = true;
  
  while(i < SOFTWARE_RX_BUFFER_SIZE && i < length)
  {
    if(c[i] != RX_buffer[i])
    {
      //Serial.print(RX_buffer[i]);
      flag = false;
      break;
    }
      i++;
  }

  /*
  if(RX_buffer[i] != '0')
  {
    flag = false;
  }
  */
  return flag;

}

void clearRX_Buffer()
{
  for(int i=0; i<SOFTWARE_RX_BUFFER_SIZE; i++)
  {
    RX_buffer[i] = '0';
  }
}

