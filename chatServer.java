import java.util.*;
import java.io.*;
import java.net.*;

public class chatServer extends Thread {

	DatagramSocket Socket;
	InetAddress addr;
	String IP;
	HashSet<String> IPList = new HashSet<String>();

	private static String clientIP = "192.168.86.74";
	static int port = 5000;

	public static void main(String[] args) {
		chatServer u = new chatServer();
		try {
			u.run(); // run the server
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {
		System.out.println("Server started");
		
		try {
			Socket = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
		}

		try {
			InetAddress addr = InetAddress.getLocalHost();
			String hostName = addr.getHostName();
			String hostAddress = addr.getHostAddress();
			System.out.println("IP Address:" + hostAddress);
			System.out.println("Port #:" + port);
			System.out.println("HostName =" + hostName);

		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		System.out.println("Chat Log");

		Thread send2client = new send2client();
		send2client.start();

		while (true) {
			byte[] buffer = new byte[1024];
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			try {
				Socket.receive(packet);
				String clientIP = packet.getAddress().getHostAddress();

				if (!IPList.contains(clientIP)) {
					String welcome = "Welcome new client!";
					byte[] data = welcome.getBytes();
					DatagramPacket packet2 = new DatagramPacket(data, data.length, InetAddress.getByName(clientIP),
							5000);
					// Send the packet
					Socket.send(packet2);
					// Add the client's IP address to the HashSet
					IPList.add(clientIP);

				}

				String message = new String(packet.getData(), 0, packet.getLength());
				System.out.println("client message: " + message);
			} catch (IOException e) {
				e.printStackTrace();
			}

//					
		}
	}

	public class send2client extends Thread { // this lets the server send messages to the client
		public void run() {
			try {

				try (Scanner kb = new Scanner(System.in)) { // create the scanner that can read the server's message

					while (true) { // infinite loop to send messages

						String message = kb.nextLine(); // get the server's message

						byte[] data = message.getBytes(); // turn the message into a byte

						DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getByName(clientIP),
								5000); // turn the byte array object into a packet

						Socket.send(packet); // Send the packet to the client
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
