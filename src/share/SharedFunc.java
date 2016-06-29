package share;

import java.net.InetAddress;
import java.util.ArrayList;

public class SharedFunc {
	public static String GetIP() throws Exception {
		String ip = InetAddress.getLocalHost().getHostAddress();
		String re = "//" + ip + ":" + Integer.toString((int)(Math.random() * 20000 + 10000));
		re = re + "//FUNCTION";
		return re;
	}
	public static String GetIP(String ip, ArrayList portList) throws Exception {
		Integer port = ((int)(Math.random() * 20000 + 10000));
		portList.add(port);
		String re = "//" + ip + ":" + Integer.toString(port);
		re = re + "//FUNCTION";
		return re;
	}
}
