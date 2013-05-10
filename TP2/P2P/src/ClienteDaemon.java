
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class ClienteDaemon implements Runnable {

    InetAddress ia;
    byte[] aEnviar;

    public ClienteDaemon(InetAddress ia, byte[] aEnviar) {
        this.ia = ia;
        this.aEnviar = aEnviar;
    }

    @Override
    public void run() {
        try {
            try (DatagramSocket s = new DatagramSocket()) {
                DatagramPacket p = new DatagramPacket(aEnviar, aEnviar.length, ia, Peer.PORTA_SERVIDOR);
                s.send(p);
                byte[] aReceber = new byte[1024];
                DatagramPacket r = new DatagramPacket(aReceber, aReceber.length);
                s.receive(r);
                String resposta = new String(r.getData(), 0, r.getLength());
                System.out.println("Resposta:" + resposta);
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }


    }
}
