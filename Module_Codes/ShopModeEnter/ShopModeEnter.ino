char shopMode[2] = {'s', 'm'};
char ErrorVals[2] = {'e'};
float errorX = 0;
float errorY = 0;
float errorZ = 0;

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  Serial.println("Hare Krishna !!");

  delay(5000);
  if (CheckSerialFor(shopMode, 2))
  {
    Serial.println("Entered shopmode");
    while (1);
  }

}

void loop() {
  // put your main code here, to run repeatedly:
  Serial.println("Entered opmode");
  while (1)
  {

    if (CheckSerialFor(ErrorVals, 1))
      errorX = Serial.parseFloat();

    if (CheckSerialFor(ErrorVals, 1))
      errorY = Serial.parseFloat();

    if (CheckSerialFor(ErrorVals, 1))
      errorZ = Serial.parseFloat();


    Serial.println(errorX);
    Serial.println(errorY);
    Serial.println(errorZ);
    delay(10);
  }

}

int CheckSerialFor(char str[], int l)
{
  int i = 0;

  while (i < l)
  {
    if (Serial.available())
    {
      if (Serial.read() != str[i])
      {
        Serial.flush();
        return 0;
      }
    } else {
      return 0;
    }

    i++;
  }
  return 1;
}
