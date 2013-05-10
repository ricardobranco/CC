
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Checker implements Runnable {

    Peer peer;

    public Checker(Peer peer) {
        this.peer = peer;
    }

    public void run() {
        while (true) {
            try {
                for (InetAddress ip : peer.validPeer) {
                    boolean rhello = Peer.hello(ip, Peer.PORT);
                    if (!rhello) {
                        System.out.println(ip.getHostAddress()+" Removed");
                        peer.validPeer.remove(ip);
                    }
                }
                Thread.sleep(Peer.T1);
            } catch (InterruptedException ex) {
                Logger.getLogger(Checker.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }
}
