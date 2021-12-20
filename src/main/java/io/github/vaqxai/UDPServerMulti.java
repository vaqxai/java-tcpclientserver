package io.github.vaqxai;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UDPServerMulti extends UDPServer {

	/**
	 * Key = Sender's address:port
	 * Val = Message
	 */
	private HashMap<String, Queue<Message>> receivedBySenders = new HashMap<>();

	/**
	 * 
	 * @return Key = Sender address:port, Val = Message
	 */
	public HashMap<String, Queue<Message>> getReceivedList(){
		return receivedBySenders;
	}

	public int countConnectedClients(){
		if (receivedBySenders.size() == 0) { return 0; }
		if (receivedBySenders.keySet().size() == 0) { return 0; }
		return receivedBySenders.keySet().size();
	}

	/**
	 * 
	 * @param addrStr address:port of a given client
	 * @return the amount of messages received from this client
	 */
	public int countClientsMessages(String addrStr){
		if(receivedBySenders.containsKey(addrStr)){
			return receivedBySenders.get(addrStr).size();
		} else {
			return 0;
		}
	}

	/**
	 * 
	 * @return all connected clients' address:port strings
	 */
	public List<String> getAllClientAddrStrs(){
		if(receivedBySenders.size() > 0){
			return receivedBySenders.keySet().stream().collect(Collectors.toList());
		} else {
			return new ArrayList<>();
		}
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
	 * Sends result of callback function back to each "connected" client. If successful, the message is removed from the queue.
	 * @param callback Function which takes the last received message from this client and then returns a response. If it returns null, no response will be sent to the client.
	 */
	public void respondToAllByLast(Function<Message, String> callback){
		for (String senderAddr : receivedBySenders.keySet()){ // for each sender
			String toSendAddr = senderAddr.split(":")[0];
			String toSendPort = senderAddr.split(":")[1];
			Queue<Message> messagesOfSender = receivedBySenders.get(senderAddr);
			Message msg = messagesOfSender.peek(); // get youngest message
			String response = callback.apply(msg);
			if(response != null){
				messagesOfSender.remove(); // remove younget message
				this.send(response, toSendAddr, Integer.parseInt(toSendPort));
			}
		}
	}

	/**
	 * Sends result of callback function back to each "connected" client, if that result is not null.
	 * @param callback Function which takes all received messages as a queue, and returns a reeponse.
	 */
	public void respondToAllQueue(Function<Queue<Message>, String> callback){
		for (String senderAddr : receivedBySenders.keySet()){ // for each sender
			String toSendAddr = senderAddr.split(":")[0];
			String toSendPort = senderAddr.split(":")[1];
			String response = callback.apply(receivedBySenders.get(senderAddr));
			if(response != null){
				this.send(response, toSendAddr, Integer.parseInt(toSendPort));
			}
		}
	}

	public List<Message> getLastMessages(){
		ArrayList<Message> lastMessages = new ArrayList<>();
		for (String senderAddr : receivedBySenders.keySet()){
			Queue<Message> messagesOfSender = receivedBySenders.get(senderAddr);
			Message msg = messagesOfSender.peek(); // get youngest message
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
				String receivedStrNice = receivedStr.replaceAll("\n","\\n");

				System.out.println(String.format("SERVER got packet from %s:%s, contents: %s", incomingAdddress.getHostAddress(), packet.getPort(), receivedStrNice));

				String incAddrStr = incomingAdddress.getHostAddress() + ":" + packet.getPort();

				if(receivedBySenders.size() == 0 || !receivedBySenders.containsKey(incAddrStr)){
					receivedBySenders.put(incAddrStr, new LinkedList<Message>());
				}

				receivedBySenders.get(incAddrStr).add(new Message(receivedStr, incomingAdddress.getHostAddress(), packet.getPort()));

			} catch (IOException e) {

				System.out.println(e);

			}
		}
	}
	
}
