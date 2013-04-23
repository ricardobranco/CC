/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.tp2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ricardobranco
 */
public class Servidor implements Runnable {

    List<DatagramPacket> pedidos;
    List<Thread> threads;

    public Servidor() {
        pedidos = new ArrayList<>();
        threads = new ArrayList<>();
    }

    public void run() {
        try {
            DatagramSocket s = new DatagramSocket(28280);
            while (true) {
                byte[] aReceber = new byte[1024];
                DatagramPacket pedido = new DatagramPacket(aReceber, aReceber.length);
                s.receive(pedido);
                pedidos.add(pedido);
                Daemon d = new Daemon(s, pedido);
                Thread t = new Thread(d);
                threads.add(t);
                t.start();
            }

        } catch (Exception e) {
            for (Thread t1 : threads) {
                try {
                    t1.join();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            e.printStackTrace();
        }
    }
}
