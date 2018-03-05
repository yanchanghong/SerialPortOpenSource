package changhongyun.SerialPortOpenSource.util;

import java.util.TooManyListenersException;

import changhongyun.SerialPortOpenSource.Exception.PortNotAvailableException;

/*
 * Specifies the public APIs of the porct connection
 */
public interface PortConnection {

	/*
	 * open the connection
	 * 
	 * @thows PortNotAvailabeException,TooManyLisenlerException,Exception
	 */
	public void open() throws PortNotAvailableException, TooManyListenersException, Exception;

	/*
	 * close the port when clean up
	 * 
	 * @throws Exception
	 */
	public void close() throws Exception;

	/*
	 * read the message from port
	 * 
	 * @Param message
	 */
	public void readMessage(byte[] message);

	/*
	 * send data to port
	 * 
	 * @Param data
	 * 
	 * @thows Exception
	 */
	public void sendMessage(byte[] data) throws Exception;
}
