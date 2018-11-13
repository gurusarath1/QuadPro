#include<Wire.h>
#include <Servo.h>

int16_t AccX,AccY,AccZ,Temp,GyroX,GyroY,GyroZ;
const int MPU6050_addr=0x68;

void setUpI2C_MPU();
void MPU_start();
void MPU_ReadAll();

void setUpI2C_MPU()
{

  Wire.begin();
  Wire.beginTransmission(MPU6050_addr);
  Wire.write(0x6B);
  Wire.write(0);
  Wire.endTransmission(true);
  delay(5000);
  
}

void MPU_start()
{
  Wire.beginTransmission(MPU6050_addr);
  Wire.write(0x3B);
  Wire.endTransmission(false);
  Wire.requestFrom(MPU6050_addr,14,true);
  
}

void MPU_ReadAll()
{
  
  MPU_start();

  AccX=Wire.read()<<8|Wire.read();
  AccY=Wire.read()<<8|Wire.read();
  AccZ=Wire.read()<<8|Wire.read();
  Temp=Wire.read()<<8|Wire.read();
  GyroX=Wire.read()<<8|Wire.read();
  GyroY=Wire.read()<<8|Wire.read();
  GyroZ=Wire.read()<<8|Wire.read();

}
  
