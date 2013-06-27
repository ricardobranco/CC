
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Checker implements Runnable {

    Peer peer;

    public static void refresh() {
        try {
            for (InetAddress ip : Peer.validPeer) {
                boolean rhello = Peer.hello(ip, Peer.PORT);
                if (!rhello) {
                    System.out.println(ip.getHostAddress() + " Removed");
                    Peer.validPeer.remove(ip);
                }
            }
            Thread.sleep(Peer.T1);
        } catch (Exception ex) {
        }
    }

    public Checker(Peer peer) {
        this.peer = peer;
    }

    public void run() {
        while (true) {
            refresh();

        }

    }
}
