package com.umbranium;

import java.io.*;
import java.net.*;
import java.util.function.*;

public class ClientHandler implements Runnable {
	private final Socket clientSocket;
	private final Function<String, String> responseCallback;

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
