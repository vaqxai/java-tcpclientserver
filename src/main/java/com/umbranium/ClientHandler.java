package com.umbranium;

import java.io.*;
import java.net.*;

/**
	* This class handles a single client that's connected to the server.
 */
public class ClientHandler implements Runnable {
	private final Socket clientSocket;
	private BufferedReader input = null;
	private PrintWriter output = null;

	/**
	 * Creates a ClientHandler with the provided server socket, and a response callback.
	 * @param socket the connected client's socket instance
	 * @param responseCallback the {@link java.util.function.Function} which accepts a String and returns a String to be ran on every incoming message.
	 */
	public ClientHandler(Socket socket){
		this.clientSocket = socket;
	}

	public Socket getSocket(){
		return this.clientSocket;
	}

	public void send(Object message){
		output.println(String.valueOf(message));
	}

	public String get(){
		try{
		return input.readLine();
		} catch(IOException e){
			System.out.println(e);
			return "ERROR";
		}
	}

	public void run(){
		
		try {

			output = new PrintWriter(clientSocket.getOutputStream(), true);
			input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

		} catch (IOException e) {
			System.out.println(e);
		}

	}

}
