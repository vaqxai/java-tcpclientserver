package com.umbranium;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    /**
     * Create client & server, then check if they communicate
     */
    @Test
    public void testClientServer(){

        System.out.println("testClientServer");

        TCPServer tcpServer = new TCPServer(4447);
        new Thread(tcpServer).start(); // don't hold up rest of program.

        TCPClient tcpClient = new TCPClient("0.0.0.0", 4447);
        tcpClient.send("Big Ass");

        tcpServer.getAllClients().forEach(client -> {
            System.out.println("Server response");
            client.send(String.format("You have sent: %s to our server.", client.get()));
        });

        System.out.println(tcpClient.getOwnPort());
        
        String response = tcpClient.get();

        System.out.println(response);

        assertTrue(response.equals("You have sent: Big Ass to our server."));

    }

    /**
     * Can you send to a disconnected client?
     */
    @Test
    public void sendToDisconnected(){

        System.out.println("testsendDisconnected");

        TCPServer tcpServer = new TCPServer(6444);
        new Thread(tcpServer).start();

        TCPClient client1 = new TCPClient("0.0.0.0",6444);
        client1.send("dupa");
        System.out.println("SENT!");
        try{
        client1.getSocket().close();
       
        for (int i = 0; i < 1000; i++){
            client1.connect("0.0.0.0", 6444);
            client1.getSocket().close();
        }

        } catch (IOException e){}

        tcpServer.getAllClients().forEach(client -> {
            client.send("Big ass");
        });

        System.out.println("Done");
        System.out.println("Clients total: " + tcpServer.getAllClients().size());

        assertTrue(tcpServer.getAllClients().size() == (1001));

    }

    /**
     * Create seq server
     */
    @Test
    public void testServerSequential(){

        System.out.println("testServerSequential");

        TCPServer emulatorUczelni = new TCPServer(7445);
        new Thread(emulatorUczelni).start();

        TCPServer tcpServer = new TCPServer(7444);
        new Thread(tcpServer).start(); // don't hold up rest of program.

        TCPClient messenger = new TCPClient("0.0.0.0",7445);
        messenger.send("0.0.0.0" + ":" + 7444);

        TCPClient dummy = new TCPClient();
        emulatorUczelni.getAllClients().forEach(client -> {
            String response = client.get();
            System.out.println("Response: " + response);
            String address = response.split(":")[0];
            int port = Integer.parseInt(response.split(":")[1]);

            dummy.connect(address, port);
            dummy.send("Dupa");
            System.out.println("Sent!");
        });

        tcpServer.getAllClients().forEach(client -> {
            client.send(client.get());
        });

        tcpServer.getAllClients().forEach(client -> {
            client.send(String.valueOf(tcpServer.getAllClients().size()));
        });

        tcpServer.getAllClients().forEach(client -> {
            client.send(tcpServer.getPort());
        });

        tcpServer.getAllClients().forEach(client -> {

        });

        System.out.println(dummy.get());

    }
}
