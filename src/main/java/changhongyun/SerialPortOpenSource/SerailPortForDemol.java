package changhongyun.SerialPortOpenSource;

import changhongyun.SerialPortOpenSource.util.SerialPortManager;

public class SerailPortForDemol {
	/*
	 * Singleton Pattern
	 */
	private static SerailPortForDemol instance = new SerailPortForDemol();
	
	private static SerialPortConnection spc = null;
	private SerailPortForDemol(){
	}
	
	public static SerailPortForDemol getSerailPortFormDemol(){
		return instance;
	}
	
	public static SerialPortConnection getSerailPortConnection()
	{
		if(spc != null)
		{
			return spc;
		}
		String portName = "/dev/ttymxc4";  //串口号
		int baudrate = 9600; //波特率
		SerialPortManager.BaudRate br = SerialPortManager.getBaudRate(baudrate);
		spc = SerialPortConnection.newConnection(portName, br);
		try {
			spc.open();
		} catch (Exception e1) {
			System.out.println(" open Error:" + e1.getMessage());
		} finally {

		}
		return spc;
	}
}
