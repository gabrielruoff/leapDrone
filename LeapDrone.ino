#include <string.h>
#include <Servo.h>

Servo throttle;
Servo ail;
Servo elev;
Servo rud;

void setup()
{
  
Serial.begin(115200);

throttle.attach(5);
ail.attach(10);
elev.attach(9);
rud.attach(6);

 throttle.write(0);
   rud.write(1187);
   Serial.println("ARMED");
   delay(1000);
   rud.write(0);
   
}

void loop()
{

  int t, rudd, eleve, aile;
  
if (Serial.available()) {
  
   t = (Serial.parseInt());
  Serial.print("Data0: ");
  Serial.println(t);
  
 /* rudd = (Serial.parseInt());
  Serial.print("Data1: ");
  Serial.println(rudd);

   eleve = (Serial.parseInt());
  Serial.print("Data2: ");
  Serial.println(eleve);
  
   aile = (Serial.parseInt());
  Serial.print("Data3: ");
  Serial.println(aile);*/


  throttle.write(t);
  /*rud.write(rudd);
  elev.write(eleve);
  ail.write(aile);*/

Serial.println("DID IF");

}
  Serial.print("Data0: ");
    Serial.println(t);
Serial.println("end");
  
}

