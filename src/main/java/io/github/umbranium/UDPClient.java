package io.github.umbranium;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.LinkedList;

public class UDPClient extends Thread {

	private DatagramSocket socket;
	private InetAddress address;
	private int port;
	private byte[] buf = new byte[256];
	private LinkedList<Message> received = new LinkedList<>();


	/**
	 * 
	 * @return internal socket for direct usage
	 */
	public DatagramSocket getSocket() {
		return socket;
	}

	/**
	 * Creates a new client and binds it to the address and port.
	 * @param address the address of a udp server
	 * @param port the port of a udp server
	 */
	public UDPClient(String address, int port){
		try {
			this.socket = new DatagramSocket();
			this.address = InetAddress.getByName(address);
			this.port = port;
		} catch (IOException e) {
			System.out.println(e);
		}

		System.out.println("CLIENT RUNNING ON PORT " + this.socket.getLocalPort());
		new Thread(this).start();
	}

	/**
	 * Sends a message to the address and port defined by the constructor
	 * @param message the message to be sent
	 */
	public void send(String message){
		buf = message.getBytes();
		DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);

		try {
			socket.send(packet);
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	/**
	 * 
	 * @return the oldest packet data in the receiveds
	 */
	public Message get(){
		if (received.size() > 0) {
			return received.removeFirst();
		} else {
			System.out.println("Tried to get data from empty queue, returning nothing.");
			return null;
		}
	}

	public void run() {
		while(true) {

			buf = new byte[256];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);

			try {

				socket.receive(packet);

				String receivedStr = new String(packet.getData(), 0, packet.getLength());
				received.add(new Message(receivedStr, packet.getAddress().getHostAddress(), packet.getPort()));

				System.out.println("CLIENT RECEIVED PACKET FROM " + packet.getAddress().getHostAddress() + ":" + packet.getPort());

			} catch (IOException e) {
				System.out.println(e);
			}

		}
	}
	
}
