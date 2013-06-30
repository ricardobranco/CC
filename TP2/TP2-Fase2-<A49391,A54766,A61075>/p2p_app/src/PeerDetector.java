
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PeerDetector implements Runnable {

    public static final int FILE = 1;
    public static final int NETWORK = 2;
    private int mode;
    private Peer peer;

    public PeerDetector(Peer peer, int mode) {
        this.peer = peer;
        this.mode = mode;
    }

    public Set<InetAddress> collectIPsFile(String file) {
        Set<InetAddress> res = new HashSet<>();

        try {
            FileInputStream fis = new FileInputStream(file);
            DataInputStream dis = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(dis));
            String newline;
            while ((newline = br.readLine()) != null) {
                InetAddress IPAddress = InetAddress.getByName(newline);
                if (Peer.hello(IPAddress, Peer.PORT)) {
                    res.add(IPAddress);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return res;
        }

    }

    public Set<InetAddress> collectIPsNetwork() {

        int ntry = Peer.R;
        Set<InetAddress> res = new HashSet<>();
        InetAddress myIP = peer.myIP.iterator().next();
        PDU hello = new Hello(myIP, Peer.PORT);

        try {
            byte[] resposta = Peer.serializa(hello);
            InetAddress broadcast = InetAddress.getByName("255.255.255.255");
            DatagramSocket ds = new DatagramSocket();
            DatagramPacket dp = new DatagramPacket(resposta, resposta.length, broadcast, Peer.PORT);
            ds.send(dp);
            while (ntry > 0) {
                ds.setSoTimeout(Peer.T3);
                dp = new DatagramPacket(resposta, resposta.length);
                try {
                    ds.receive(dp);
                    InetAddress getIP = dp.getAddress();
                    peer.addIP(getIP);
                } catch (SocketTimeoutException e) {
                    ntry--;
                }
            }
            ds.close();

        } catch (SocketException ex) {
            //System.out.println("FALHOU BROADCAST");
            Logger.getLogger(PeerDetector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownHostException ex) {
            Logger.getLogger(PeerDetector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PeerDetector.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return res;
        }

    }

    @Override
    public void run() {

        while (true) {
            Set<InetAddress> setIP;
            if (this.mode == FILE) {
                setIP = collectIPsFile(Peer.CONFIGFILE);

            } else {
                setIP = collectIPsNetwork();
            }
            for (InetAddress ip : setIP) {
                peer.addIP(ip);
            }

            try {
                Thread.sleep(Peer.T1*100);
            } catch (InterruptedException ex) {
                Logger.getLogger(PeerDetector.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
