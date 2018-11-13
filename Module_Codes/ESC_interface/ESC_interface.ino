#include <Servo.h>
Servo esc_signal_1;
Servo esc_signal_2;
Servo esc_signal_3;
Servo esc_signal_4;

int speed_Input1 = 55;
void setup()
{
  Serial.begin(9600);
  esc_signal_1.attach(8);  //Specify here the pin number on which the signal pin of ESC is connected.
  esc_signal_1.write(30);   //ESC arm command. ESCs won't start unless input speed is less during initialization.
  delay(3000);            //ESC initialization delay.

    esc_signal_2.attach(9);  //Specify here the pin number on which the signal pin of ESC is connected.
  esc_signal_2.write(30);   //ESC arm command. ESCs won't start unless input speed is less during initialization.
  delay(3000);            //ESC initialization delay.

    esc_signal_3.attach(10);  //Specify here the pin number on which the signal pin of ESC is connected.
  esc_signal_3.write(30);   //ESC arm command. ESCs won't start unless input speed is less during initialization.
  delay(3000);            //ESC initialization delay.

    esc_signal_4.attach(11);  //Specify here the pin number on which the signal pin of ESC is connected.
  esc_signal_4.write(30);   //ESC arm command. ESCs won't start unless input speed is less during initialization.
  delay(3000);            //ESC initialization delay.
}

void loop()
{
esc_signal_1.write(speed_Input1);  //Vary this between 40-130 to change the speed of motor. Higher value, higher speed.
esc_signal_2.write(speed_Input1);  //Vary this between 40-130 to change the speed of motor. Higher value, higher speed.
esc_signal_3.write(speed_Input1);  //Vary this between 40-130 to change the speed of motor. Higher value, higher speed.
esc_signal_4.write(speed_Input1);  //Vary this between 40-130 to change the speed of motor. Higher value, higher speed.

/*
  while(Serial.available() <= 0);
  {
    Serial.print("XO");
  }

  Serial.print("Writing");
    speed_Input1 = Serial.read();

    */

    while(1)
    {
      Serial.print("XO");
    }
}
