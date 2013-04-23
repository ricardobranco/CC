/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.tp2;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ricardobranco
 */
public class Peer {

    public static void main(String args[]) {

        try {
            Servidor servidor = new Servidor();
            Cliente cliente = new Cliente();
            Thread tservidor = new Thread(servidor);
            Thread tcliente = new Thread(cliente);

            tservidor.start();
            tcliente.start();

            tservidor.join();
            tcliente.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
