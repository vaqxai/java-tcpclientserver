package io.github.vaqxai;

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

    private static void wait(int millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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

        TCPClient initiator = new TCPClient("172.21.48.15",34168);

        UDPServer udpServer = new UDPServer(4444);
        new Thread(udpServer).start();

        initiator.send("190435");
        initiator.send("172.23.129.109:4444");

        wait(300);



        /*
        Message msg = udpServer.get();
        String msgReplaced = msg.toString().replaceAll("9","");
        System.out.print("Server got this: " + msg);
        udpServer.send(msgReplaced + "\n", msg.getAddress(), msg.getPort());
        System.out.println("Sent response: " + msgReplaced);

        wait(300);

        msg = udpServer.get();
        String msgStr = msg.toString().substring(0, msg.toString().length()-1); // remove "\n"
        msgReplaced = msgStr + msgStr + msgStr + msgStr;
        System.out.println("Server got this: " + msg);
        udpServer.send(msgReplaced + "\n", msg.getAddress(), msg.getPort());
        System.out.println("Sent response: " + msgReplaced);

        wait(3);

        String resAddr = "";
        int resPort = 0;

        ArrayList<Integer> ints = new ArrayList<Integer>();
        for(int i = 0; i < 5; i++){
            msg = udpServer.get();
            resAddr = msg.getAddress();
            resPort = msg.getPort();
            msgStr = msg.toString().substring(0, msg.toString().length()-1); // remove "\n"
            ints.add(Integer.parseInt(msgStr));
        }
        System.out.println("Received 5 ints: " + ints.toString());
        int nwd = nwd(ints); // pray to god this works
        System.out.println("Sending: " + nwd);
        udpServer.send(String.valueOf(nwd) + "\n", resAddr, resPort);

        wait(100);

        System.out.println("FINAL FLAG:");
        System.out.println(udpServer.get().toString());
        */

        waiter.close();

        System.exit(0);

    }
}
