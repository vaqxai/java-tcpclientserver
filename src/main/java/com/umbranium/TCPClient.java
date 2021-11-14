package com.umbranium;

import java.net.*;
import java.io.*;

/**
* TCPClient is the clientside wrapper that can receive and send strings.
 */
public class TCPClient {

	private Socket socket = null;
	private BufferedReader input = null;
	private PrintWriter output = null;
	private boolean silentMode = false;

	/**
	 * Creates the client instance, needs to connect to a specified server.
	 * @param address Server's IPv4 address
	 * @param port Server's port
	 */
	public TCPClient(String address, int port){

		try {
			socket = new Socket(address, port);

			if(!silentMode)
				System.out.println("CLIENT START");

			output = new PrintWriter(socket.getOutputStream(), true);
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e){
			System.out.println(e);
		}

	}

	/**
	 * Gives you the local port automatically assigned to create the connection.
	 * @return the local port you used to make the connection.
	 */
	public int GetOwnPort(){
		return socket.getLocalPort();
	}

	/**
	 * Gives you the current socket for direct interaction.
	 * @return the socket
	 */
	public Socket GetSocket(){
		return socket;
	}

	/**
	 * In silent mode, only errors are printed.
	 * @param shouldBeSilent true to enable, false to disable.
	 */
	public void setSilentMode(boolean shouldBeSilent){
		this.silentMode = shouldBeSilent;
	}

	/**
	 * You can use this method to send a message to the server you have connected to.
	 * @param message the message to be sent
	 */
	public void Send(String message){
		output.println(message);
	}

	/**
	 * You can use this method to grab the next line of buffered server response.
	 * @return the next Line from server feedback
	 */
	public String Get(){
		try{
			return input.readLine();
		} catch (IOException e) {
			System.out.println(e);
			return "";
		}
	}

}
