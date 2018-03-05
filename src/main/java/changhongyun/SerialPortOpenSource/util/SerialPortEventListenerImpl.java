package changhongyun.SerialPortOpenSource.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import changhongyun.SerialPortOpenSource.SerialPortConnection;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class SerialPortEventListenerImpl implements SerialPortEventListener {

	// private static final Logger log =
	// Logger.getLogger(SerialPortEventListenerImpl.class);
	private final static Logger logger = Logger.getLogger(SerialPortEventListenerImpl.class.getName());

	private boolean logAllRawInput = false; // enable to true if we want to log
											// all raw input history
	private InputStream byteDataStream = null;

	private PortConnection portConnection;
	public String portName = "";
	public static int index = 0;
	private byte[] data = new byte[3];
	private byte[] messageData = null;
	private static final SimpleDateFormat DF = new SimpleDateFormat("yyyy-MM-dd't'HH:mm:ss.SSS'z'");

	/**
	 * @param byteDataStream
	 *            Incoming byte data.
	 * @param byteDataHandler
	 *            Handler that reads the byte data.
	 */
	public SerialPortEventListenerImpl(InputStream byteDataStream, SerialPortConnection serialPortConnection) {
		this.byteDataStream = byteDataStream;
		this.portConnection = serialPortConnection;
		portName = serialPortConnection.portName;
	}

	public void serialEvent(SerialPortEvent arg0) {
		// We're here if new data came in.

		try {
			// log.debug("Reading bytes ...");
			logger.info("Reading bytes ...");

			int available = byteDataStream.available();

			byte[] data = new byte[available];
			byteDataStream.read(data);
			// Log the raw data stream
			logger.info("data:" + ByteArrayToHexString(data));
			processData(data);

		} catch (IOException e) {
			// log.error("Error during serial port listen event.", e);
			logger.info(e.getMessage());
			try {
				portConnection.close();
			} catch (Exception ex) {
				logger.info(ex.getMessage());
			}

		}
	}

	public static String ByteArrayToHexString(byte[] ba) {
		StringBuilder output = new StringBuilder();
		for (int i = 0; i < ba.length; i++)
			output.append(ByteToHexString(ba[i]));
		return output.toString();
	}

	public static String ByteToHexString(byte b) {
		if ((b & 0x80) != 0) {
			char c = (char) b;
			c &= 0x00FF;
			return Integer.toHexString(c);
		} else {
			if ((b & 0xF0) == 0)
				return "0" + Integer.toHexString(b);
			else
				return Integer.toHexString(b);
		}
	}

	private synchronized void processData(byte[] moreData) {
		// 解析每个字节判断是否结束
		for (byte b : moreData) {
			if (index <= 2) {
				this.data[index] = b;
			}
			index++;
			int dataInt = Convert.unsignedByteToInt(b);
			if (index == 3) {
				if (dataInt <= 0) {
					logger.info("message data length error");
				}
				this.messageData = new byte[dataInt + 3 + 2];
				System.arraycopy(this.data, 0, this.messageData, 0, this.data.length);
			}

			if (index > 3) {
				this.messageData[index - 1] = b;
				if (index == this.messageData.length) {
					System.out.println("recevice message:" + Convert.ByteArrayToHexString(this.messageData));
					// will deal this data,
					index = 0;
					this.data = new byte[3];
					this.messageData = null;
				}
			}

		}
	}

}
