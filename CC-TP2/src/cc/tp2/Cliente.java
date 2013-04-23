/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.tp2;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

/**
 *
 * @author ricardobranco
 */
public class Cliente implements Runnable {

    public void run() {
        Scanner scanner = new Scanner(System.in);

        try {
            while (true) {
                InetAddress IPAddress = InetAddress.getByName("localhost");
                DatagramSocket s = new DatagramSocket();

                String readline = scanner.nextLine();
                byte[] aEnviar = readline.getBytes();
                DatagramPacket p = new DatagramPacket(aEnviar, aEnviar.length, IPAddress, 28280);
                s.send(p);
                byte[] aReceber = new byte[1024];
                DatagramPacket r = new DatagramPacket(aReceber, aReceber.length);
                s.receive(r);
                String resposta = new String(r.getData(), 0, r.getLength());
                System.out.println("Resposta:" + resposta);
                s.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            scanner.close();
        }
    }
}
