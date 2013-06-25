
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Peer {

    public static String SHAREPATH = "./";
    public static String CONFIGFILE = "p2p_bootstrap.conf";
    public static int T1 = 10000; //INTERVALO DE TEMPO QUE VERIFICA SE UM PEER ESTÁ ACTIVO
    public static int T3 = 1000; //TEMPO PARA OBTER PRIMITIVA
    public static int PORT = 28280;
    public static int R = 7; //Nº TENTATIVAS
    public static Set<InetAddress> validPeer = new HashSet<>();
    public static Set<InetAddress> myIP = Peer.getSelfIPs();
    public static Map<String, Set<FileRegister>> files = new HashMap<>();
     
    Server servidor;
    Client cliente;
    PeerDetector filePD;
    PeerDetector networkPD;
    Checker checker;
    //HashMap<String, Object>

    public static Set<InetAddress> getSelfIPs() {
        Set<InetAddress> res = new HashSet<>();

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
            byte[] resposta = new byte[3 * 1024];
            try (DatagramSocket ds = new DatagramSocket()) {
                DatagramPacket dp = new DatagramPacket(hello, hello.length, ip, port);
                DatagramPacket dpresposta;
                boolean flag = true;
                while (ntry > 0 && flag) {
                    ds.send(dp);
                    ds.setSoTimeout(Peer.T3);
                    dpresposta = new DatagramPacket(resposta, resposta.length);
                    try {
                        ds.receive(dpresposta);
                        HelloResponse hr = (HelloResponse) Peer.desSerializa(resposta);
                        for (InetAddress inA : hr.valid_peer) {
                            Peer.addIP(inA);
                        }
                        for (String skey : hr.ficheiros.keySet()) {
                            if(!Peer.files.containsKey(skey)){
                                Peer.files.put(skey,new HashSet<FileRegister>());
                            }
                            for(FileRegister fr : hr.ficheiros.get(skey)){
                                Peer.files.get(skey).add(fr);
                            }
                        }
                        res = true;
                        flag = false;
                    } catch (SocketTimeoutException e) {
                        ntry--;
                        System.out.println("Try find: "+ip.getHostAddress()+" - remaining attempts:" + ntry);
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
        filePD = new PeerDetector(this, PeerDetector.FILE);
        networkPD = new PeerDetector(this, PeerDetector.NETWORK);
        checker = new Checker(this);
    }

    public static void addIP(InetAddress ip) {
        if (!myIP.contains(ip) && !validPeer.contains(ip)) {
            validPeer.add(ip);
            System.out.println("GET:" + ip.getHostAddress());
        }
    }

    public static void status() {
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

    public static void register(String path) { 
        File file = new File(path);
        if (!file.exists()) {
            System.out.println("Ficheiro inexistente");
        } else {

            FileRegister fr = new FileRegister(file.getName(), file.getAbsolutePath(), null, Peer.PORT, file.length());
            if(!files.containsKey(fr.nome)){
                files.put(fr.nome, new HashSet<FileRegister>());
            }
            files.get(fr.nome).add(fr);
            System.out.println("Register: " + fr.nome);
        }
    }

   
}