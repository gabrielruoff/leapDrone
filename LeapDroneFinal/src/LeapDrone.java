
import com.leapmotion.leap.*;

import java.io.IOException;

import com.leapmotion.leap.Gesture.State;
import java.io.OutputStream;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Listener;
//import java.lang.Math;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

class SampleListenerMain extends Listener {

	public int posmapxold = 0;
	public int posmapyold = 0;
	public int posmapzold = 0;
	
	public int a = 1;
	
public void onInit(Controller controller) {
System.out.println("Initialized");

}

public void onConnect(Controller controller) {
System.out.println("Connected");
controller.enableGesture(Gesture.Type.TYPE_SWIPE);
controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);


}

public void onDisconnect(Controller controller) {
System.out.println("Disconnected");
}

public void onExit(Controller controller) {
System.out.println("Exited");
}
public void onFrame(Controller controller) {

	
Frame frame = controller.frame();

Hand hand = frame.hands().rightmost();
float positionx = hand.palmPosition().getX();
System.out.println("X: " + positionx);

float positiony = hand.palmPosition().getY();
System.out.println("Y:" + positiony);

float positionz = hand.palmPosition().getZ();
System.out.println("Z: " + positionz);

System.out.println("Hands: " + frame.hands().count());

int in_min = 0;
int in_max = 500;
int in_minx = -200;
int in_maxx = 200;
int out_min = 1100;
int out_max = 2000;
int in_min2 = -230;
int in_max2 = 230;

int positionmapx = (int) ((positionx - in_minx) * (out_max - out_min) / (in_maxx - in_minx)) + out_min;
int positionmapy = (int) ((positiony - in_min) * (out_max - out_min) / (in_max - in_min)) + out_min;
int positionmapz = (int) ((positionz - in_min2) * (out_max - out_min) / (in_max2 - in_min2)) + out_min;


if (frame.hands().count() == 0) {
	//LeapDrone.writeToArduino("0");
}



String data = Integer.toString(positionmapy)+":"+ Integer.toString(positionmapx)+":"+Integer.toString(positionmapz);

System.out.println("data: " + data);


System.out.println("Old X: " + posmapxold);

System.out.println("Difference X: " + Math.abs(positionmapx - posmapxold));


System.out.println("sent");
	LeapDrone.writeToArduino(data);
 
posmapxold = positionmapx;
posmapyold = positionmapy;
posmapzold = positionmapz;

System.out.println("New-Old X: " + posmapxold);



	
	
}
}

class LeapDrone {
static OutputStream out = null;

public static void main(String[] args) {
//Connect to Arduino BT
	
try
{
	//Device
(new LeapDrone()).connect("/dev/cu.usbmodem1421");
}
catch ( Exception e )
{
e.printStackTrace();
System.exit(0);
}

// Create a sample listener and controller
SampleListenerMain listener = new SampleListenerMain();
Controller controller = new Controller();

// Have the sample listener receive events from the controller
controller.addListener(listener);
// Keep this process running until Enter is pressed
System.out.println("Press Enter to quit...");
try {
System.in.read();
} catch (IOException e) {
e.printStackTrace();
}

// Remove the sample listener when done
controller.removeListener(listener);
}

void connect ( String portName ) throws Exception {

CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
if ( portIdentifier.isCurrentlyOwned() )
{
System.out.println("Error: Port is currently in use");
}
else
{
CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);

if ( commPort instanceof SerialPort )
{
SerialPort serialPort = (SerialPort) commPort;
serialPort.setSerialPortParams(115200,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,
SerialPort.PARITY_NONE);
out = serialPort.getOutputStream();
}
else
{
System.out.println("Selected port is not a Serial Port");
}
}
}

public static void writeToArduino(String data)
{
String tmpStr = data;
byte bytes[] = tmpStr.getBytes();
try {
out.write(bytes);
} catch (IOException e) { }
}
}
