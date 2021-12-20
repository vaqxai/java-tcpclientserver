package io.github.vaqxai;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
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

    private static long nwd(long a, long b) { return a == 0 ? b : nwd(b % a, a); }

    private static long nwd(ArrayList<Long> nums){
        long res = 0;
        for(long elem : nums){
            res = nwd(res, elem);
            if (res == 1) return 1;
        }
        return res;
    }

    public static void main( String[] args )
    {

        Scanner waiter = new Scanner(System.in); // Do ręcznego oczekiwania

        TCPClient initiator = new TCPClient("172.21.48.200",39799); // Adres serwera TCP z zadania

        UDPServerMulti udpServer = new UDPServerMulti(4444); // Tworzenie naszego serwera UDP
        new Thread(udpServer).start(); // Startowanie naszego serwera UDP

        initiator.send("155262"); // Flaga poczatkowa
        initiator.send("172.23.129.20:4444"); // Adres naszego serwera UDP

        wait(1000); // Moment na synchronizacje, i odczekanie na ew. laga po stronie uczelni

        System.out.println("Total connected distinct UDP clients: " + udpServer.countConnectedClients());

        List<String> allConnectedClientStrs = udpServer.getAllClientAddrStrs();

        ArrayList<Long> initialNumbers = new ArrayList<>();

        // Add "First contact" numbers to a list to be used later.
        udpServer.respondToAllByLast((msg) -> {
        String msgSanitized = msg.getData().replaceAll("\n","");
        Long msgNum = Long.parseLong(msgSanitized);
        initialNumbers.add(msgNum);
        return msg.getData();
        });

        udpServer.getReceivedList().keySet().removeAll(udpServer.getReceivedList().keySet()); // purge
        System.out.println("We responded to each of them. And purged the sender list");

        wait(100); // poczekamy na lagi uczelni

        udpServer.respondToAllByLast((msg) -> {
            initialNumbers.add(Long.parseLong(msg.getData().replaceAll("\n","")));
            return msg.getData();
        });

        System.out.println("We responded with the same message we got from them.");

        wait(100);

        udpServer.respondToAll("4444\n");

        System.out.println("We sent our port number.");

        udpServer.respondToAll(nwd(initialNumbers) + "\n");
        System.out.println("We sent the nwd of the initial-communication numbers.");

        udpServer.respondToAll("2\n");
        System.out.println("We sent count of connected clients.");

        wait(100); 

        udpServer.respondToAllByLast((msg) -> {
            String sanMsg = msg.getData().replaceAll("\n","");
            return sanMsg + sanMsg + "\n";
        });
        System.out.println("We responded with the client's message, back to back 2 times.");

        wait(100);

        for(String senderAddr : udpServer.getReceivedList().keySet()){
            ArrayList<Long> toNWD = new ArrayList<>();
            for(Message msg : udpServer.getReceivedList().get(senderAddr)){
                String strSan = msg.getData().replaceAll("\n","");
                Long strLng = Long.parseLong(strSan);
                toNWD.add(strLng);
            }
            udpServer.getReceivedList().get(senderAddr).clear(); // purge the queue

            Long nwdLng = nwd(toNWD);
            System.out.println(nwdLng);
            udpServer.send(nwdLng + "\n", senderAddr.split(":")[0], Integer.parseInt(senderAddr.split(":")[1]));
            System.out.println("We responded by calculating an NWD from all the " + senderAddr + " client's messages.");
        }

        wait(100);

        System.out.println("And we got the final flaggg ^");

        /*

        udpServer.respondToAllByLast((msg) -> {
            return msg.getData().replaceAll("2","");
        });
        System.out.println("We responded by removing all occurences of '2' and replying with that.");

        wait(100);

        Long sum = 0l;
        for(Long l : initialNumbers){
            sum+=l;
        }
        udpServer.respondToAll(sum + "\n");
        System.out.println("We sent the sum of the initial-communication numbers.");

        wait(100);

        udpServer.respondToAllByLast((msg) -> {
            String sanMsg = msg.getData().replaceAll("\n","");
            return sanMsg + sanMsg + sanMsg + sanMsg + sanMsg + "\n";
        });
        System.out.println("We responded with the client's message, back to back 5 times.");

        wait(100);

        udpServer.respondToAll(nwd(initialNumbers) + "\n");
        System.out.println("We sent the nwd of the initial-communication numbers.");

        wait(100);

        udpServer.respondToAllByLast((msg) -> {
            long msgLng = Long.parseLong(msg.getData().replaceAll("\n",""));
            int k = 0;
            while(Math.pow(k+1,5) < msgLng){
                k++;
            }
            return k + "\n";
        });
        System.out.println("We responded by calculating such a number, that when raised to its' 5th power, it was less than what we got.");
        */
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
