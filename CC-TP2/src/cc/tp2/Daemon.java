/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.tp2;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 * @author ricardobranco
 */
public class Daemon implements Runnable {

    DatagramSocket s;
    DatagramPacket pedido;

    public Daemon(DatagramSocket ds, DatagramPacket dp) {
        s = ds;
        pedido = dp;
    }

    public void run() {

        try {
            String mensagem = new String(pedido.getData(), 0, pedido.getLength());
            InetAddress IPAddress = pedido.getAddress();
            int porta = pedido.getPort();
            byte[] aEnviar;
            
            switch(mensagem.toUpperCase()){
                case "HELLO":{
                aEnviar = "Cenas".getBytes();
                break;
                }
                default:{
                aEnviar = mensagem.getBytes();
                break;
                }
                    
            } 
            
            
            
            DatagramPacket resposta = new DatagramPacket(aEnviar, aEnviar.length, IPAddress, porta);
            s.send(resposta);
        } catch (Exception e) {
            e.printStackTrace();
        }




    }
}
