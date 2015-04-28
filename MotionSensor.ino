//libraries included
#include <SimpleTimer.h>
#include <SPI.h>
#include <WiFi.h>
#include <ArduinoJson.h>

//Variables for setup
SimpleTimer timer;
int motionDetected = 0;
int ledPins[] = {2,3,5,6};
int inputPin = 8; // choose the input pin (for PIR sensor)
int pirState = LOW; // we start, assuming no motion detected
int val = 0; // variable for reading the pin status
StaticJsonBuffer<200> jsonBuffer;

//The network SSID
char ssid[] = "Vista Del Sol South"; 

//Set the status for the wifi shield as idle
int status = WL_IDLE_STATUS;

//Set up a server and a client where the arduino has its own ip address
WiFiServer server(80);
WiFiClient client;
IPAddress ip(10, 180, 53, 106);  

//Set up a json object to post to the client
JsonObject& root = jsonBuffer.createObject();

void setup() 
{
  int index;
  
  //set each LED pin as output
  for(index = 0; index <= 3; index++)
  {
    pinMode(ledPins[index],OUTPUT);
  }
  
  //Set the PIR sensor pin as an input
  pinMode(inputPin, INPUT);
  
  //set the interval for the timer so that the sensor will check the rooms status every 30 seconds
  timer.setInterval(30000, pollRoom);
  
  //Initialize serial and wait for the serial window to open
  Serial.begin(9600); 
  while (!Serial) {
    ;
  }
  
  // check if the wifi shield is attached
  if (WiFi.status() == WL_NO_SHIELD) {
    Serial.println("WiFi shield not present"); 
    // wait in a loop
    while(true);
  } 
  
  //configure the wifi shield so that it is using the ip address previously specified
  WiFi.config(ip);
  
  // Connect to wifi
  while ( status != WL_CONNECTED) { 
    Serial.print("Attempting to connect to SSID: ");
    Serial.println(ssid);
    status = WiFi.begin(ssid);

    // wait 10 seconds for connection
    delay(10000);
  } 
  server.begin();
  
  //print the status of the connection
  printWifiStatus();
  
  //Set the initial status of the room
  root["roomUsed"] = 0;
}

void loop()
{
  timer.run();
  //read the value of the PIR sensor
  val = digitalRead(inputPin);
  int i, duration;
  
    //if the input is high, do the following
    if (val == HIGH)  
    {    
      //turn on all LEDs
      allOn();
      
      //if the PIR state is low, motion was detected and the PIR state is now set to high
      if (pirState == LOW) 
      {
        motionDetected = 1;
        pirState = HIGH;
        
      }
    } 
    
    //if the input is low, do the following
    else 
    {
      //if the PIR state is high, motion is finished being detected and the PIR state is now set to low
      if (pirState == HIGH)
      {
        pirState = LOW;
      }
    }
  
  // listen for incoming clients
  client = server.available();
  
  //if a client is found, do the following
  if (client) {    
    Serial.println("new client");
      //an http request ends with a blank line
      boolean currentLineIsBlank = true;
      //while the client is connected, do the following
      while (client.connected()) {
        //if the client is available, do the following
        if (client.available()) {
          //read characters from the client
          char c = client.read();
          //if the http request has ended, send a response header
          if (c == '\n' && currentLineIsBlank) {
            client.println("HTTP/1.1 200 OK");
            client.println("Content-Type: application/json");
            //the connection will close after the response
            client.println("Connection: close");
            //The page refreshes every 30 seconds
            client.println("Refresh: 30");
            client.println();
            
            root.printTo(client);
            
            //if motion was detected, the client will print to the webpage that the room is in use and the timer begins
            if(motionDetected == 1)
            {
              Serial.println("Motion Detected!");
            }
            
            //if motion was not detected, the client will print to the webpage that the room is not in use
            else
            {
              Serial.println("Motion Not Detected!");
            }
            break;
          }
          
          //if a new line character is found, the current line is blank
          if (c == '\n') {
          currentLineIsBlank = true;
          } 
          
          //There's a character on the current line
          else if (c != '\r') {
            currentLineIsBlank = false;
          }
          
        }
      }
      //delay so that the browser can receive data
      delay(1);
      
      //Stop the connection
      client.stop();
      Serial.println("client disonnected");
    }
}

//Turns the LED's on and off with a delay interval
void allOn()
{
  int index;
  int delayTime;
  delayTime = 500;
  
  for(index = 0; index <= 3; index++)
  {
    digitalWrite(ledPins[index], HIGH);
  }
  
  delay(delayTime);                    
  
  for(index = 0; index <= 3; index++)
  {
    digitalWrite(ledPins[index], LOW);   
  }
  
  delay(delayTime);                   
}

//This polls the room every 30 seconds to see if motion was detected in that time interval
void pollRoom()
{
  //if motion was detected, it prints to serial that the room is in use and motion detected resets to 0
  if(motionDetected == 1)
  {
     Serial.println("Writing 1 to server!");
     root["roomUsed"] = 1;
     motionDetected = 0;
  }
  
  //otherwise, it prints to serial that the room is not in use
  else
  {
      Serial.println("Writing 0 to server!");
      root["roomUsed"] = 0;
  }
}

void printWifiStatus() {
  //Prints the SSID
  Serial.print("SSID: ");
  Serial.println(WiFi.SSID());

  //Prints the Wifi Shield's IP address
  IPAddress ip = WiFi.localIP();
  Serial.print("IP Address: ");
  Serial.println(ip);

  //Prints the signal strength
  long rssi = WiFi.RSSI();
  Serial.print("signal strength (RSSI):");
  Serial.print(rssi);
  Serial.println(" dBm");
}
