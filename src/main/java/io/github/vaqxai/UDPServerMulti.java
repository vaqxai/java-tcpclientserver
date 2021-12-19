package io.github.vaqxai;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.*;

public class UDPServerMulti extends UDPServer {

	/**
	 * Key = Sender's address:port
	 * Val = Message
	 */
	private HashMap<String, List<Message>> receivedBySenders;

	/**
	 * 
	 * @return Key = Sender address:port, Val = Message
	 */
	public HashMap<String, List<Message>> getReceivedList(){
		return receivedBySenders;
	}
	
	public void run() {
		running = true;

		System.out.println("UDP SERVER START");

		while(running) {
			try {

				buf = new byte[256];
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				socket.receive(packet);

				InetAddress incomingAdddress = packet.getAddress();

				String receivedStr = new String(packet.getData(), 0, packet.getLength());

				System.out.println(String.format("SERVER got packet from %s:%s, contents: %s", incomingAdddress.getHostAddress(), packet.getPort(), receivedStr));

				String incAddrStr = incomingAdddress.getHostAddress() + ":" + packet.getPort();

				if(!receivedBySenders.keySet().contains(incAddrStr)){
					receivedBySenders.put(incAddrStr, new ArrayList<Message>());
				}

				receivedBySenders.get(incAddrStr).add(new Message(receivedStr, incomingAdddress.getHostAddress(), packet.getPort()));

			} catch (IOException e) {

				System.out.println(e);

			}
		}
	}
	
}
