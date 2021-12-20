package io.github.vaqxai;

public class Message {

	private String data;
	private String address;
	private int port;

	public Message(String data, String address, int port){
		this.data = data;
		this.address = address;
		this.port = port;
	}

	public String getData(){
		return data;
	}

	public String getAddress(){
		return address;
	}

	public int getPort(){
		return port;
	}

	public String toString(){
		return data;
	}

	public String getAddrStr(){
		return address + ":" + port;
	}
	
}
