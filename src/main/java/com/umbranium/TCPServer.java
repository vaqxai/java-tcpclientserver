package com.umbranium;

import java.io.IOException;
import java.net.*;
import java.util.function.*;

/**
* TCPServer is a multithreaded ServerSocket wrapper that can respond to requests from multiple clients at once, using a predetermined callback.
 */
public class TCPServer implements Runnable { // runnable so it doesn't block rest of program.

	private int port;
	private Function<String, String> responseCallback;
	private ServerSocket server = null;
	private boolean silentMode = false;

	/**
	 * Creates a server instance, one which will reply to all incoming requests putting them through the given callback function
	 * @param port the port on which to create the server
	 * @param responseCallback a {@link java.util.function.Function} accepting a String and returning a String, it is called with every request message the server receives.
	 */
	public TCPServer(int port, Function<String, String> responseCallback){ // we set up a server that will respond to messages using a given function
		this.port = port;
		this.responseCallback = responseCallback;
	}

	/**
	 * Gives you the current serverSocket for direct interaction
	 * @return the socket
	 * @since 1.0.3
	 */
	public ServerSocket GetSocket(){
		return this.server;
	}

	/**
	 * Gives you the current listening port
	 * @return the port
	 */
	public int GetPort(){
		return this.port;
	}

	/**
	 * Changes the response callback to a specified function
	 * @param newCallback new function to set the callback to
	 * @since 1.0.3
	 */
	public void setResponseCallback(Function<String,String> newCallback){
		this.responseCallback = newCallback;
	}
	
	/**
	 * In silent mode, the server will only print errors.
	 * @param shouldBeSilent true to enable, false to disable
	 * @since 1.0.3
	 */
	public void setSilentMode(boolean shouldBeSilent){
		this.silentMode = shouldBeSilent;
	}

	public void run(){

		try {
			server = new ServerSocket(port);
			server.setReuseAddress(true);

			if(!silentMode)
				System.out.println("SERVER START");

			while(true){

				Socket client = server.accept();

				if(!silentMode)
					System.out.println(String.format("CLIENT CONN [%s]", client.getInetAddress().getHostAddress()));

				ClientHandler clientSock = new ClientHandler(client, responseCallback); // we forward the function to each client handler we create

				new Thread(clientSock).start();

			}
		} catch (IOException e) {
			System.out.println(e);
		}

	}

}
