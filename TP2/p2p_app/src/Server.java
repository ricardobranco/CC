
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server implements Runnable {

    Peer peer;
    DatagramSocket ds;
    List<Thread> threads;
    
    public Server(Peer peer) {
        try {
            this.peer = peer;
            threads = new ArrayList<>();
            ds = new DatagramSocket(Peer.PORT);
        } catch (SocketException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        try {

            while (true) {
                int buffsize = 3*1024;
                byte[] aReceber = new byte[buffsize]; 
                DatagramPacket dp = new DatagramPacket(aReceber, aReceber.length);
                ds.receive(dp);
                Request pedido = new Request(ds, dp, peer);
                Thread tpedido = new Thread(pedido);
                threads.add(tpedido);
                tpedido.start();
            }

        } catch (SocketException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ds.close();
            for(Thread thread : threads){
                try {
                    thread.join();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
