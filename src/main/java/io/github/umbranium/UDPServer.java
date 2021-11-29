package io.github.umbranium;

import java.io.IOException;
import java.net.*;
import java.util.LinkedList;

public class UDPServer extends Thread {

	private DatagramSocket socket;
	private boolean running;
	private byte[] buf = new byte[256];
	private LinkedList<String> received = new LinkedList<>();

	/**
	 * 
	 * @return internal socket for direct usage
	 */
	public DatagramSocket getSocket() {
		return socket;
	}

	/**
	 * Creates a UDP Server at a given port
	 * @param port
	 */
	public UDPServer(int port) {
		try{
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			System.out.println(e);
		}
	}

	/**
		* Creates a UDP Server at any port
	 */
	public UDPServer() {
		try{
			socket = new DatagramSocket();
		} catch (SocketException e) {
			System.out.println(e);
		}
	}

	/**
	 * @return the oldest received packet
	 */
	public String get(){
		if (received.size() > 0) {
			return received.removeFirst();
		} else {
			System.out.println("Tried to get data from empty queue, returning nothing.");
			return "";
		}
	}

	/**
	 * Sends string data to the supplied address. Does not modify the input string.
	 * @param data the string to be sent
	 * @param address the receiver's hostname
	 * @param port the receiver's port
	 */
	public void send(String data, String address, int port){
		InetAddress destination = null;

		try {
			destination = InetAddress.getByName(address);
		} catch (UnknownHostException e) {
			System.out.println(String.format("Unknown host %s:%s while sending packet, aborting.", address, port));
			return;
		}

		buf = data.getBytes();

		DatagramPacket packet = new DatagramPacket(buf, buf.length, destination, port);
		try {
		socket.send(packet);
		} catch (IOException e) {
			System.out.println(e);
		}

	}

	public void run() {
		running = true;

		System.out.println("UDP SERVER START");

		while(running) {
			try {

				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				socket.receive(packet);

				InetAddress incomingAdddress = packet.getAddress();

				System.out.println(String.format("SERVER got packet from %s.", incomingAdddress.getHostAddress()));
				String receivedStr = new String(packet.getData(), 0, packet.getLength());
				received.add(receivedStr);

			} catch (IOException e) {

				System.out.println(e);

			}
		}

		System.out.println("UDP SERVER STOPPED");

	}
	
}
