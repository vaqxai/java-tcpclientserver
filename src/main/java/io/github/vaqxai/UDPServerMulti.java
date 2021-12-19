package io.github.vaqxai;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.*;
import java.util.function.Function;

public class UDPServerMulti extends UDPServer {

	/**
	 * Key = Sender's address:port
	 * Val = Message
	 */
	private HashMap<String, ArrayList<Message>> receivedBySenders;

	/**
	 * 
	 * @return Key = Sender address:port, Val = Message
	 */
	public HashMap<String, ArrayList<Message>> getReceivedList(){
		return receivedBySenders;
	}

	public int countConnectedClients(){
		return receivedBySenders.keySet().size();
	}

	/**
	 * Sends a response to all clients which have been in conctact with the server.
	 * @param response raw string response
	 */
	public void respondToAll(String response){
		for (String senderAddr : receivedBySenders.keySet()){ // for each sender
			String toSendAddr = senderAddr.split(":")[0];
			String toSendPort = senderAddr.split(":")[1];
			this.send(response, toSendAddr, Integer.parseInt(toSendPort));
		}
	}

	/**
	 * Sends result of callback function back to each "connected" client.
	 * @param callback Function which takes the last received message from this client and then returns a response
	 */
	public void respondToAllByLast(Function<String, String> callback){
		for (String senderAddr : receivedBySenders.keySet()){ // for each sender
			String toSendAddr = senderAddr.split(":")[0];
			String toSendPort = senderAddr.split(":")[1];
			ArrayList<Message> messagesOfSender = receivedBySenders.get(senderAddr);
			Message msg = messagesOfSender.get(messagesOfSender.size() - 1); // get youngest message
			this.send(callback.apply(msg.getData()), toSendAddr, Integer.parseInt(toSendPort));
		}
	}

	public List<Message> getLastMessages(){
		ArrayList<Message> lastMessages = new ArrayList<>();
		for (String senderAddr : receivedBySenders.keySet()){
			ArrayList<Message> messagesOfSender = receivedBySenders.get(senderAddr);
			Message msg = messagesOfSender.get(messagesOfSender.size() - 1); // get youngest message
			lastMessages.add(msg);
		}
		return lastMessages;
	}

	public UDPServerMulti(int port){
		super(port);
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
