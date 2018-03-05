package changhongyun.SerialPortOpenSource.util;

import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import changhongyun.SerialPortOpenSource.Exception.PortNotAvailableException;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

/**
 * Management of serial ports.
 * 
 * Contains default settings as well.
 * 
 */
public class SerialPortManager {

	private final static Logger logger = Logger.getLogger(SerialPortManager.class.getName());

	/**
	 * How long to wait to open the port.
	 */
	private static final int CONNECTION_TIMEOUT = 5000;

	/**
	 * Name of the program owner of the port.
	 */
	private static final String SERIAL_READER_ID = "MeshSerialReader";

	/**
	 * Connect to the specified port.
	 * 
	 * @param portId
	 * @param baudRate
	 * @return An opened serial port connection.
	 * @throws Exception
	 */
	public static SerialPort openPort(CommPortIdentifier portId, BaudRate baudRate) throws Exception {
		SerialPort port = (SerialPort) portId.open(SERIAL_READER_ID, CONNECTION_TIMEOUT);
		port.setSerialPortParams(baudRate.getRate(), SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
				SerialPort.PARITY_NONE);
		return port;
	}

	/**
	 * Get the port given the name of the port. The port is only returned if it
	 * is available.
	 * 
	 * @return an available port
	 * @throws Exception
	 */
	public static CommPortIdentifier getSerialPort(String portName) throws PortNotAvailableException {

		CommPortIdentifier portId = null;
		try {
			portId = CommPortIdentifier.getPortIdentifier(portName);
		} catch (Exception e) {
			throw new PortNotAvailableException("No such port exception :" + e.toString() + " portName:"+ portName);
		}

		if (portId != null && portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
			if (!portId.isCurrentlyOwned()) {
				return portId;
			} else {
				// return portId;
				throw new PortNotAvailableException(
						"The requested port is currently open by another process: " + portId.getCurrentOwner());
			}
		} else {
			throw new PortNotAvailableException("Unable to find any serial ports.");
		}

	}

	/**
	 * @return Collection of all serial ports.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static List<CommPortIdentifier> getAllSerialPorts() throws PortNotAvailableException {

		Enumeration<CommPortIdentifier> ports = CommPortIdentifier.getPortIdentifiers();

		LinkedList<CommPortIdentifier> commPortIds = new LinkedList<CommPortIdentifier>();

		if (!ports.hasMoreElements()) {
			throw new PortNotAvailableException("No COM ports found!");
		}
		// log.info("Ports were found.");
		while (ports.hasMoreElements()) {
			CommPortIdentifier portId = ports.nextElement();

			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				commPortIds.add(portId);
			}
		}

		return commPortIds;
	}

	/**
	 * Supported baud rate to connect to serial port.
	 *
	 */
	public enum BaudRate {
		B_115200(115200), B_57600(57600), B_38400(38400), B_19200(19200), B_9600(9600),B_4800(4800);

		private int rate;

		private BaudRate(int rate) {
			this.rate = rate;
		}

		public int getRate() {
			return rate;
		}

		@Override
		public String toString() {
			return Integer.toString(rate);
		}
	};
	
	public static BaudRate getBaudRate(int baudRate) {
		switch(baudRate) {
			case 115200:
				return BaudRate.B_115200;
			case 57600:
				return BaudRate.B_57600;
			case 38400:
				return BaudRate.B_38400;
			case 19200:
				return BaudRate.B_19200;
			case 4800:
				return  BaudRate.B_4800;
			case 9600:
				return BaudRate.B_9600;
				default:
					throw new IllegalArgumentException("baudrate not supported:\n"+
							"only support 115200,57600,38400,19200,9600,4800"
							);
		}	
	}
}
