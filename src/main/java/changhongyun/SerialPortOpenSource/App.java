package changhongyun.SerialPortOpenSource;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		System.out.println("open source serialPort");
		SerialPortConnection spc = SerailPortForDemol.getSerailPortConnection();
		// test data, for humiture sensor
		byte[] data = new byte[8];
		data[0] = 0x00;
		data[1] = 0x01;
		data[2] = 0x01;
		data[3] = 0x01;
		data[4] = 0x01;
		data[5] = 0x01;
		data[6] = (byte) 0xC4;
		data[7] = (byte) 0x0B;
		try {
			spc.sendMessage(data);
		} catch (IOException e) {
			System.out.println("error:" + e.getMessage());
		}
	}
}
