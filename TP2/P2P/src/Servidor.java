
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;

public class Servidor implements Runnable {

    List<Thread> threads;
    DatagramSocket s;
    Peer peer;
    ByteReadAndWrite braw;
    public Servidor(Peer p) {
        threads = new ArrayList<>();
        peer = p;
        braw = new ByteReadAndWrite();

    }

    @Override
    public void run() {
        try {
            s = new DatagramSocket(Peer.PORTA_SERVIDOR);
            while (true) {
                byte[] aReceber = new byte[1024];
                DatagramPacket pedido = new DatagramPacket(aReceber, aReceber.length);
                s.receive(pedido);
                peer.addNewIP(pedido.getAddress());
                ServidorDaemon d = new ServidorDaemon(s, pedido,this);
                Thread t = new Thread(d);
                t.start();
                threads.add(t);
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

    public boolean register(String file) { 
        boolean res;
        File f = new File(file);
        if (f.exists()) {
            try {
                peer.files.put(file, braw.readAndFragment(file, Peer.CHUNKSIZE));
                res = true;
            } catch (IOException ex) {
                ex.printStackTrace();
                res = false;
            }
        } else {
            res = false;
        }
        //Foi o cliente associado a este servidor que fez o upload posso enviar a mensagem pelo System.out
        System.out.println("Upload do ficheiro "+file+(res?" com ":" sem ")+"sucesso");
        return res;

    }
    public void notifyRegister(String file){
        //COMPLETAR
    }
}
