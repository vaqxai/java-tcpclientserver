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

        TCPClient initiator = new TCPClient("",0);

        UDPServer udpServer = new UDPServer(4444);
        new Thread(udpServer).start();

        wait(waiter);

        initiator.send("172.23.129.69:4444\n");
        initiator.send("\n");

        wait(waiter);

        Message msg = udpServer.get();
        System.out.print("Server got this: " + msg);
        udpServer.send("Response", msg.getAddress(), msg.getPort());

        wait(waiter);

        waiter.close();

        System.exit(0);

    }
}
