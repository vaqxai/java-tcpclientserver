package io.github.vaqxai;

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
        tcpServer.setAutoResponse(message -> {
            return "You have sent: " + message + " to our server.";
        });
        new Thread(tcpServer).start(); // don't hold up rest of program.

        TCPClient tcpClient = new TCPClient("localhost", 4447);
        tcpClient.send("Big Ass");

        System.out.println("Client's port: " + tcpClient.getOwnPort());
        
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String response = tcpClient.get().toString();

        System.out.println("Reponse the client got: " + response);

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
            for (int i = 0; i < 10; i++){
                client1.connect("0.0.0.0", 6444);
                client1.getSocket().close();
            }
        } catch (IOException e){}

        try{
            System.out.println("Waiting for clients (1s)");
            Thread.sleep(1000); // wait for clients
        } catch (InterruptedException e) {
            System.out.println(e);
        }

        System.out.println("Done");
        System.out.println("Clients total: " + tcpServer.getAllClients().size());

        assertTrue(tcpServer.getAllClients().size() == (11));

    }

    @Test
    public void TestUDP(){

        UDPServer udpServer = new UDPServer(4444);
        new Thread(udpServer).start();

        UDPClient udpClient = new UDPClient("localhost", 4444);
        udpClient.send("Dudududu duuupaaaa\n");

    }
}
