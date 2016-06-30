package share;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;

public class SharedFunc {

	public static String getLocalHostIP() {
		try {
			for (Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces(); nis.hasMoreElements();) {
				NetworkInterface ni = nis.nextElement();
				if (ni.isLoopback() || !ni.isUp())
					continue;
				for (Enumeration<InetAddress> ias = ni.getInetAddresses(); ias.hasMoreElements();) {
					InetAddress ia = ias.nextElement();
					if (ia instanceof Inet6Address)
						continue;
					// return
					System.out.println(ia);
					System.out.println(ia.getHostAddress());

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String GetIP() throws Exception {
		String ip = InetAddress.getLocalHost().getHostAddress();
		String re = "//" + ip + ":" + Integer.toString((int) (Math.random() * 20000 + 10000));
		re = re + "//FUNCTION";
		return re;
	}

	public static String GetIP(String ip, ArrayList portList) throws Exception {
		Integer port = ((int) (Math.random() * 20000 + 10000));
		portList.add(port);
		String re = "//" + ip + ":" + Integer.toString(port);
		re = re + "//FUNCTION";
		return re;
	}

	public static boolean WriteCheckpoint(String path, int Round, ArrayList ID, ArrayList Pr) throws Exception {
		String path2 = "checkpoint" + Round;
		String joinedPath = new File(path, path2).toString();
		// System.out.println(joinedPath);
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(joinedPath);
			for (int i = 0; i < ID.size(); i++) {
				fileWriter.write(ID.get(i) + " " + Pr.get(i) + "\n");
			}
			fileWriter.write("-12345 -12345\n");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			File f = new File(joinedPath);
			if (f.exists())
				f.delete();
			e.printStackTrace();
			return false;
		} finally {
			if (fileWriter != null)
				fileWriter.close();
		}
		return true;
	}

	public static boolean ReadCheckpoint(String path, int Round, ArrayList ID, ArrayList Pr) throws Exception {
		String path2 = "checkpoint" + Round;
		String joinedPath = new File(path, path2).toString();
		// System.out.println(joinedPath);
		File file = new File(joinedPath);
		BufferedReader reader = null;
		ID.clear();
		Pr.clear();
		boolean ok = false;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				String[] tmp = tempString.split(" ");
				int id = Integer.parseInt(tmp[0]);
				double pr = Double.parseDouble(tmp[1]);
				if (id == -12345) {
					ok = true;
					break;
				}
				ID.add(id);
				Pr.add(pr);
			}
			reader.close();
		} catch (IOException e) {
			// e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
			if (!ok) {
				ID.clear();
				Pr.clear();
			}
		}
		return ok;
	}
}
