package com.umbranium;

import java.net.*;
import java.io.*;

public class TCPClient {

	private Socket socket = null;
	private BufferedReader input = null;
	private PrintWriter output = null;

	public TCPClient(String address, int port){

		try {
			socket = new Socket(address, port);
			System.out.println("CONNECTED");

			output = new PrintWriter(socket.getOutputStream(), true);
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e){
			System.out.println(e);
			System.exit(1);
		}

	}

	public void Send(String message){
		output.println(message);
	}

	public String Get(){
		try{
			return input.readLine();
		} catch (IOException e) {
			System.out.println(e);
			return "";
		}
	}

}
