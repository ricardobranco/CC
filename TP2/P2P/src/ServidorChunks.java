
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;

public class ServidorChunks implements Runnable {

    List<Thread> threads;
    DatagramSocket s;
    Peer peer;
    
    public ServidorChunks(Peer p) {
        threads = new ArrayList<>();
        peer = p;
        
    }

    @Override
    public void run() {
        try {
            s = new DatagramSocket(Peer.PORTA_CHUNKS);
            while (true) {
                byte[] aReceber = new byte[1024];
                DatagramPacket pedido = new DatagramPacket(aReceber, aReceber.length);
                s.receive(pedido);


            }

        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            s.close();
            for (Thread t1 : threads) {
                try {

                    t1.join();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }


        }

    }
}