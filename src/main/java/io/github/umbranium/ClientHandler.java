package io.github.umbranium;

import java.io.*;
import java.net.*;
import java.util.function.*;

/**
	* This class handles a single client that's connected to the server.
 */
public class ClientHandler implements Runnable {
	private final Socket clientSocket;
	private BufferedReader input = null;
	private PrintWriter output = null;
	private boolean verbose = false;

	/**
	 * Automatic response to this client: if you return "", it will not send a response.
	 */
	private Function<String, String> onReceiveMessage = null;

	/**
	 * Creates a ClientHandler with the provided server socket, and a response callback.
	 * @param socket the connected client's socket instance
	 */
	public ClientHandler(Socket socket){
		this.clientSocket = socket;
	}

	/**
	 * Makes this clientHanlder very quiet or very loud. On by default.
	 * @param shouldBeVerbose should it write out all debug?
	 */
	public void setVerbose(boolean shouldBeVerbose){
		this.verbose = shouldBeVerbose;
	}

	/**
	 * Changes this server's client's automatic response callback
	 * @param newCallback the new callback, return "" to stop the server from sending an automatic response.
	 */
	public void setAutoResponse(Function<String,String> newCallback){
		this.onReceiveMessage = newCallback;
	}

	/**
	 * 
	 * @return the current callback function
	 */
	public Function<String, String> getAutoResponse(){
		return this.onReceiveMessage;
	}

	/**
	 * 
	 * @return this client's socket for direct interface
	 */
	public Socket getSocket(){
		return this.clientSocket;
	}

	/**
	 * 
	 * @param message send data directly to this client
	 */
	public void send(Object message){
		output.println(String.valueOf(message));
	}

	/**
	 * 
	 * @return get latest message the client has sent to us (will hang its thread if there's no incoming messages)
	 */
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

			String line;

			while ((line = input.readLine()) != null) {
				if(onReceiveMessage != null){

					if(verbose){
						System.out.println("[" + clientSocket.getInetAddress().getHostAddress() + "] SERVER AT " + clientSocket.getLocalPort() + " RECEVIED '" + line + "'");
					}

					String response = onReceiveMessage.apply(line); // apply supplied function to every received message.

					if(!response.equals(""))
						send(response); // auto respond if you get a response
						if(verbose){
							System.out.println("[" + clientSocket.getInetAddress().getHostAddress() + "] SERVER AT " + clientSocket.getLocalPort() + " SENDS RESPONSE: '" + response + "' TO ");
					}

				}
			}


		} catch (IOException e) {
			System.out.println(e);
		}

	}

}
