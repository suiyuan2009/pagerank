package share;

import java.net.InetAddress;

public class SharedFunc {
	public static String GetIP() throws Exception {
		String ip = InetAddress.getLocalHost().getHostAddress();
		String re = "//" + ip + ":" + Integer.toString((int)(Math.random() * 20000 + 10000));
		re = re + "//FUNCTION";
		return re;
	}
	public static String GetIP(String ip) throws Exception {
		String re = "//" + ip + ":" + Integer.toString((int)(Math.random() * 20000 + 10000));
		re = re + "//FUNCTION";
		return re;
	}
}
