/*
  timer : Set the sample timing
    timerFlag : Enable or disable the timerc:\Users\kai\Desktop\ppg\ppg.ino
  TimerSample : Total sampling sample
  
  data : The data is the command from the mobile phone 
  counter : Count the sample number

*/

#include <SimpleTimer.h>
#include <SoftwareSerial.h>

#define RX_PIN 3
#define TX_PIN 4

SimpleTimer Timer;

SoftwareSerial HC06(RX_PIN, TX_PIN);

int timer;
int timerFlag = 0;
int TimerSample = 0;

void setup() {
  HC06.begin(9600);
  
  // Open the serial port and set the baud
  Serial.begin(9600);

  // Clear the serial buffer data
  while(HC06.available())
    int a = HC06.read();

  // Set the timer that triggered every 40 ms, Fs = 25Hz
  timer = Timer.setInterval(40,GetSignal);
  
  // Set the check system that triggered every 5ms
  Timer.setInterval(5,CounterCheck);
  
  // Disable the timer
  Timer.disable(timer);
}

int data = 0;
int counter = 0;

void loop() {
  
  // Wait the command data from mobile phone
  if(HC06.available()){
    data = HC06.read();
  
    Timer.enable(timer);                // Enable the timer
    TimerSample = 1500 * (data - 48);   // Set sample point to 1500 * (1 ~ 5 min)  
    timerFlag = 0;                      // Set the timerFlag to 0
  }
  Timer.run();
}
// Send the data to mobile phone
void GetSignal()
{
  int data = analogRead(A0);
  HC06.write(data >> 8);
  HC06.write((byte)data);
  counter++;
}
// Check the sample point
void CounterCheck()
{
  if(counter == TimerSample){
    Timer.disable(timerFlag);
    counter = 0;
  }
}
