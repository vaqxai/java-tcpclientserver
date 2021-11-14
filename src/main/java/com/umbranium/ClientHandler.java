package com.umbranium;

import java.io.*;
import java.net.*;
import java.util.function.*;

/**
	* This class handles a single client that's connected to the server.
 */
public class ClientHandler implements Runnable {
	private final Socket clientSocket;
	private final Function<String, String> responseCallback;

	/**
	 * Creates a ClientHandler with the provided server socket, and a response callback.
	 * @param socket the connected client's socket instance
	 * @param responseCallback the {@link java.util.function.Function} which accepts a String and returns a String to be ran on every incoming message.
	 */
	public ClientHandler(Socket socket, Function<String, String> responseCallback){
		this.clientSocket = socket;
		this.responseCallback = responseCallback;
	}

	public void run(){
		
		BufferedReader input = null;
		PrintWriter output = null;

		try {

			output = new PrintWriter(clientSocket.getOutputStream(), true);
			input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			String line;
			while ((line = input.readLine()) != null) {
				output.println(responseCallback.apply(line)); // apply supplied function to every received message.
			}

		} catch (IOException e) {
			System.out.println(e);
		}

	}

}
