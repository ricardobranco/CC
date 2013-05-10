
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Peer {

    public static String SHAREPATH = "./";
    public static String CONFIGFILE = "p2p_bootstrap.conf";
    public static int T1 = 1000; //INTERVALO DE TEMPO QUE VERIFICA SE UM PEER ESTÁ ACTIVO
    public static int T3 = 1000; //TEMPO PARA OBTER PRIMITIVA
    public static int PORT = 28280;
    public static int R = 7; //Nº TENTATIVAS
    Set<InetAddress> validPeer;
    Server servidor;
    Client cliente;
    PeerDetector filePD;
    PeerDetector networkPD;
    Checker checker;
    Set<InetAddress> myIP;
    //HashMap<String, Object>

    public static Set<InetAddress> getSelfIPs() {
        Set<InetAddress> res = new HashSet<InetAddress>();

        Enumeration<NetworkInterface> e = null;
        try {
            e = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e1) {
            System.out.println("Exit (Main): Failed find own address.");
            System.exit(1);
        }
        while (e.hasMoreElements()) {
            NetworkInterface n = (NetworkInterface) e.nextElement();
            Enumeration<InetAddress> ee = n.getInetAddresses();
            while (ee.hasMoreElements()) {
                InetAddress i = (InetAddress) ee.nextElement();
                System.out.println("One of my addresses is " + i.getHostAddress());
                if (Inet4Address.class.isInstance(i) && i.isLoopbackAddress() == false) {
                    System.out.println("\tAnd I'm going to add it.");
                    res.add(i);
                }
            }
        }
        return res;
    }

    public static byte[] serializa(Object o) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(o);

        return baos.toByteArray();
    }

    public static Object desSerializa(byte[] ab) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ab);

        ObjectInputStream ois = new ObjectInputStream(bais);

        return ois.readObject();
    }

    public static boolean hello(InetAddress ip, int port) {
        int ntry = Peer.R;
        boolean res = false;
        PDU pdu = new Hello(ip, port);


        try {
            byte[] hello = Peer.serializa(pdu);
            try (DatagramSocket ds = new DatagramSocket()) {
                DatagramPacket dp = new DatagramPacket(hello, hello.length, ip, port);
                DatagramPacket dpresposta;
                boolean flag = true;
                while (ntry > 0 && flag) {
                    ds.send(dp);
                    ds.setSoTimeout(Peer.T3);
                    dpresposta = new DatagramPacket(hello, hello.length);
                    try {
                        ds.receive(dpresposta);
                        res = true;
                        flag = false;
                    } catch (SocketTimeoutException e) {
                        ntry--;
                        System.out.println("FAIL - remaining attempts:" + ntry);
                    }
                }
            }
        } catch (SocketException ex) {
            Logger.getLogger(Peer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Peer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return res;
        }
    }

    public static byte[] readChunk(String file, int offset, int size) {

        return null;

    }

    public static void main(String args[]) {
        try {
            /*
             //TESTAR INPUT
             */

            Peer peer = new Peer();
            Thread tservidor = new Thread(peer.servidor);
            Thread tcliente = new Thread(peer.cliente);
            Thread tpdfile = new Thread(peer.filePD);
            Thread tpdnetwork = new Thread(peer.networkPD);
            Thread tchecker = new Thread(peer.checker);

            tchecker.start();
            tpdnetwork.start();
            tservidor.start();
            tcliente.start();
            tpdfile.start();

            tchecker.join();
            tpdfile.join();
            tpdnetwork.join();
            tservidor.join();
            tcliente.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(Peer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public Peer() {
        servidor = new Server(this);
        cliente = new Client(this);
        validPeer = new HashSet<>();
        myIP = Peer.getSelfIPs();
        filePD = new PeerDetector(this, PeerDetector.FILE);
        networkPD = new PeerDetector(this, PeerDetector.NETWORK);
        checker = new Checker(this);
    }

    public void addIP(InetAddress ip) {
        if (!myIP.contains(ip) && !validPeer.contains(ip)) {
            validPeer.add(ip);
            System.out.println("GET:" + ip.getHostAddress());
        }
    }

    public void status() {
        System.out.println("--IPs VALIDOS--");
        for (InetAddress ip : validPeer) {
            System.out.println("\t" + ip.getHostAddress());
        }
        System.out.println("--VALORES DEFAULT: " + PORT);

        System.out.println("\tPORTA : " + PORT);
        System.out.println("\tT1: " + T1);
        System.out.println("\tT3: " + T3);
        System.out.println("\tR: " + PORT);
        System.out.println("\tCONFIG FILE: " + CONFIGFILE);



    }
}
