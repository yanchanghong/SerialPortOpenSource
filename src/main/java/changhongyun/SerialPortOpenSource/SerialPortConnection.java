package changhongyun.SerialPortOpenSource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;

import changhongyun.SerialPortOpenSource.Exception.PortNotAvailableException;
import changhongyun.SerialPortOpenSource.util.Convert;
import changhongyun.SerialPortOpenSource.util.PortConnection;
import changhongyun.SerialPortOpenSource.util.SerialPortEventListenerImpl;
import changhongyun.SerialPortOpenSource.util.SerialPortManager;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEventListener;

public class SerialPortConnection implements PortConnection {

	public final String portName;

	protected  SerialPort hostPort = null;

	protected SerialPortManager.BaudRate br;
	
	protected static final long ReTRY_WAIT_INTERVAL = 5000L; // how long to
																// wait, in
																// ms,and reties
																// of send

	protected static final int DEFAULT_QUEUE_SIZE = 50;

	private OutputStream out = null;

	public static SerialPortConnection newConnection(String portName, SerialPortManager.BaudRate br) {
		return new SerialPortConnection(portName, br);
	}

	public void open() throws PortNotAvailableException, TooManyListenersException, Exception {
		// Get the port to listen from.

		CommPortIdentifier portId = SerialPortManager.getSerialPort(portName);

		// Open that port. We use baud rate 57600.
		hostPort = SerialPortManager.openPort(portId, br);
		out = hostPort.getOutputStream();
		// Create event listener on port.
		// When new data comes in, event listener will process the data.
		InputStream inputStr = hostPort.getInputStream();
		hostPort.notifyOnDataAvailable(true);

		try {
			hostPort.addEventListener((SerialPortEventListener) new SerialPortEventListenerImpl(inputStr, this));
		} catch (Exception e) {
			// log.error("Could not add event listener.", e);
			throw new PortNotAvailableException("Could not add event listener." + e.getMessage());
		}
	}

	public void close() throws Exception {
		if (hostPort != null) {
			hostPort.getOutputStream().close();
			hostPort.getInputStream().close();
			hostPort.removeEventListener();
			if (hostPort != null)
				hostPort.close();

			hostPort = null;

			System.out.println("Port is closed");
		}
	}

	public void sendMessage(byte[] data) throws IOException {
		System.out.println("add message:" + Convert.ByteArrayToHexString(data));
		if (out != null)
			out.write(data);
	}

	public void sendRawDataToSerialPort(byte[] data) throws IOException {
		if (out == null)
			out = hostPort.getOutputStream();
		out.write(data);
		// Thread.sleep(50);
		System.out.println("finally data to serial port:" + Convert.ByteArrayToHexString(data));
	}

	private SerialPortConnection(String portName, SerialPortManager.BaudRate br) {
		this.portName = portName;
		this.br = br;
	}

	public void readMessage(byte[] message) {

	}

}
