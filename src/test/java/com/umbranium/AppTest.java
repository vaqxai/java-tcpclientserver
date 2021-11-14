package com.umbranium;

import static org.junit.Assert.assertTrue;
import java.util.function.*;
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

        Function<String, String> responseCallback = new Function<String, String>(){
            public String apply (String input){
                return "You have sent: " + input + " to our server."; // TODO: Escape dangerous characters from input.
            }
        };

        TCPServer tcpServer = new TCPServer(4444, responseCallback);
        new Thread(tcpServer).start(); // don't hold up rest of program.

        TCPClient tcpClient = new TCPClient("localhost", 4444);
        tcpClient.Send("Big Ass");
        
        String response = tcpClient.Get();

        System.out.println(response);

        assertTrue(response.equals("You have sent: Big Ass to our server."));

    }
}
