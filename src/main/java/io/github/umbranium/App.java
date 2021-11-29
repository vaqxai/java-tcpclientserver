package io.github.umbranium;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Hello world!
 *
 */ 
public class App
{

    private static void wait(Scanner s){
        System.out.println("Press ENTER to continue...");
        s.nextLine();
    }

    private static int nwd(int a, int b) { return a == 0 ? b : nwd(b % a, a); }

    private static int nwd(ArrayList<Integer> ints){
        int res = 0;
        for(int elem : ints){
            res = nwd(res, elem);
            if (res == 1) return 1;
        }
        return res;
    }
    public static void main( String[] args )
    {

        Scanner waiter = new Scanner(System.in);

        UDPServer udpServer = new UDPServer(4444);
        new Thread(udpServer).start();

        UDPClient udpClient = new UDPClient("localhost", 4444);
        udpClient.send("Dudududu duuupaaaa\n");

        wait(waiter);

        System.out.println("Server got this: " + udpServer.get());
        udpServer.send("Duuuupaaaaaaaaaaa", "127.0.0.1", udpClient.getSocket().getLocalPort());

        wait(waiter);

        System.out.println("Client got this: " + udpClient.get());

        waiter.close();

    }
}
