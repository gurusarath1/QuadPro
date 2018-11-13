
extern int16_t AccX,AccY,AccZ,Temp,GyroX,GyroY,GyroZ;

void setup(){
  setUpI2C_MPU();

  Serial.begin(9600);
}
void loop(){
  MPU_ReadAll();
  Serial.println(AccX);
}
