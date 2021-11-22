package io.github.umbranium;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

/**
* TCPServer is a multithreaded ServerSocket wrapper that can respond to requests from multiple clients at once, using a predetermined callback.
 */
public class TCPServer implements Runnable { // runnable so it doesn't block rest of program.

	private int port;
	private ServerSocket server = null;
	private boolean silentMode = false;
	private ArrayList<ClientHandler> connectedClients = new ArrayList<>();

	/**
	 * Creates a server instance, one which will reply to all incoming requests putting them through the given callback function
	 * @param port the port on which to create the server
	 */
	public TCPServer(int port){ // we set up a server that will respond to messages using a given function
		this.port = port;
	}

	/**
	 * Gives you the current serverSocket for direct interaction
	 * @return the socket
	 * @since 1.0.3
	 */
	public ServerSocket getSocket(){
		return this.server;
	}

	/**
	 * 
	 * @return all connected clients
	 * @since 1.1.0
	 */
	public ArrayList<ClientHandler> getAllClients(){
		return this.connectedClients;
	}

	/**
	 * 
	 * @return current listening port
	 */
	public int getPort(){
		return this.port;
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
				System.out.println("SERVER START [" + server.getLocalPort() + "]");

			while(true){

				Socket client = server.accept();

				if(!silentMode)
					System.out.println(String.format("CLIENT CONN [%s]", client.getInetAddress().getHostAddress()));

				ClientHandler clientSock = new ClientHandler(client); // we forward the function to each client handler we create
				new Thread(clientSock).start();
				this.connectedClients.add(clientSock);

			}

		} catch (IOException e) {
			System.out.println(e);
		}

	}

}
