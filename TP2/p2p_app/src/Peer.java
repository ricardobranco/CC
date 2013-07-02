
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

    public static String SHAREPATH = "./Novo";
    public static String CONFIGFILE = "p2p_bootstrap.conf";
    public static int T1 = 10000; //INTERVALO DE TEMPO QUE VERIFICA SE UM PEER ESTÁ ACTIVO
    public static int T3 = 1000; //TEMPO PARA OBTER PRIMITIVA
    public static int PORT = 28280;
    public static int R = 7; //Nº TENTATIVAS
    public static long CHUNKSIZE = 28;
    public static Set<InetAddress> validPeer = new HashSet<>();
    public static Set<InetAddress> myIP = Peer.getSelfIPs();
    public static Map<String, FileRegister> myFiles = new HashMap<>();
    public static Map<String, FileList> knowFiles = new HashMap<>();
    public static Map<String, FileBuilder> newfiles = new HashMap<>();
    //INCOMPLETOS
    Server servidor;
    Client cliente;
    PeerDetector filePD;
    PeerDetector networkPD;
    Checker checker;

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



                        for (String skey : hr.filesComp.keySet()) {
                            if (!Peer.knowFiles.containsKey(skey)) {
                                Peer.knowFiles.put(skey, new FileList());
                            }
                            FileRegister fraux = hr.filesComp.get(skey);
                            Peer.addFile(fraux);

                        }

                        for (String skey : hr.filesKnow.keySet()) {
                            if (!Peer.knowFiles.containsKey(skey)) {
                                Peer.knowFiles.put(skey, new FileList());
                            }
                            FileList flaux = hr.filesKnow.get(skey);
                            for (FileRegister fraux : flaux.files) {
                                if (fraux.ip.getHostAddress().equals(Peer.myIP.iterator().next().getHostAddress())) {
                                    Peer.addFile(fraux);
                                }
                            }
                        }


                        res = true;
                        flag = false;
                    } catch (SocketTimeoutException e) {
                        ntry--;
                        System.out.println("A Procurar : " + ip.getHostAddress() + " - Tentativas Restantes:" + ntry);
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

    public static Set<FileRegister> MatchFiles(String arg) {
        Set<FileRegister> ficheiros = new HashSet<>();
        //PROCURA LOCAL
        for (FileList fl : Peer.knowFiles.values()) {
            for (FileRegister fr : fl.files) {
                if (fr.nome.contains(arg)) {
                    ficheiros.add(fr);
                }
            }
        }

        return ficheiros;
    }

    public static void find(String arg) {
        Set<FileRegister> ficheiros = MatchFiles(arg);



        //PROCURA REMOTA

        for (InetAddress ip : Peer.validPeer) {
            PDU pdu = new Find(arg, ip, Peer.PORT);
            try {
                byte[] find = serializa(pdu);
                byte[] resposta = new byte[3 * 1024];

                try (DatagramSocket ds = new DatagramSocket()) {
                    DatagramPacket dp = new DatagramPacket(find, find.length, ip, PORT);
                    DatagramPacket dpresposta;

                    ds.send(dp);
                    ds.setSoTimeout(Peer.T3);
                    dpresposta = new DatagramPacket(resposta, resposta.length);
                    try {
                        ds.receive(dpresposta);
                        FindResponse fresp = (FindResponse) Peer.desSerializa(resposta);

                        for (FileRegister fr : fresp.files) {
                            addFile(fr);
                            ficheiros.add(fr);
                        }


                    } catch (SocketTimeoutException e) {
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(Peer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (SocketException ex) {
                    Logger.getLogger(Peer.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (IOException ex) {
                Logger.getLogger(Peer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        System.out.println(
                "--Ficheiros Encontrados");

        for (FileRegister fr : ficheiros) {
            System.out.println("\t" + fr.nome + " de " + fr.ip.getHostAddress() + " com " + fr.size + " Bytes");
        }
    }

    public static void get(String label) {

        Set<InetAddress> peers = new HashSet<>();

        if (knowFiles.get(label) == null || knowFiles.get(label).files.isEmpty()) {
            System.out.println("O ficheiro não é conhecido");
            return;
        }

        for (FileRegister fr : knowFiles.get(label).files) {
            peers.add(fr.ip);
        }




        int k = peers.size();
        long filesize = knowFiles.get(label).files.iterator().next().size;
        String nome = knowFiles.get(label).files.iterator().next().nome;
        long w = filesize / CHUNKSIZE * k;;
        int offset = 1;

        FileBuilder fb = new FileBuilder((int) filesize, nome);

        while (fb.remaining > 0) { //Até o ficheiro estar concluido
            for (InetAddress ip : peers) {
                try (DatagramSocket ds = new DatagramSocket()) {
                    System.out.println("GET:" + nome + " >> " + fb.remaining + " bytes restantes");
                    if (fb.remaining <= 0) {
                        break;
                    }
                    PDU pdu = new Chunk(ip, PORT, offset, (int) (CHUNKSIZE < fb.remaining ? CHUNKSIZE : fb.remaining), nome);
                    byte[] chunk = serializa(pdu);
                    byte[] resposta = new byte[3 * 1024];
                    DatagramPacket dp = new DatagramPacket(chunk, chunk.length, ip, PORT);
                    DatagramPacket dpresposta;
                    ds.send(dp);
                    ds.setSoTimeout(Peer.T3);
                    dpresposta = new DatagramPacket(resposta, resposta.length);

                    try {
                        ds.receive(dpresposta);
                        ChunkResponse cr = (ChunkResponse) desSerializa(resposta);

                        fb.addChunk(cr, offset);
                        fb.remaining -= CHUNKSIZE;
                        offset += CHUNKSIZE;


                    } catch (SocketTimeoutException e) {
                        System.out.println("ERRO1");
                    } catch (ClassNotFoundException ex) {
                        System.out.println("ERRO2");
                    }


                } catch (SocketException ex) {
                    Logger.getLogger(Peer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Peer.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }
        fb.saveFile();

    }

    public static void main(String args[]) {
        try {


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
            Logger.getLogger(Peer.class
                    .getName()).log(Level.SEVERE, null, ex);
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

    public static void removeValidPeer(InetAddress ip) {
        if (validPeer.contains(ip)) {
            validPeer.remove(ip);
            System.out.println("\t Eliminado o peer." + ip.toString() + "");
            for (FileList fl : knowFiles.values()) {
                for (FileRegister fr : fl.files) {
                    if (fr.ip.equals(ip)) {
                        fl.files.remove(fr);
                        System.out.println("\t Eliminado registo de ficheiro." + fr.nome + "");
                    }
                }
            }
        }
    }

    public static void addFile(FileRegister fr) {
        if (!Peer.knowFiles.get(fr.nome).files.contains(fr)) {
            Peer.knowFiles.get(fr.nome).files.add(fr);
            System.out.println(fr.nome + " de " + fr.ip.getHostAddress() + " adicionado");


        }
    }

    public static void status() {
        System.out.println("--IPs VALIDOS--");
        for (InetAddress ip : validPeer) {
            System.out.println("\t" + ip.getHostAddress());
        }
        System.out.println("--VALORES DEFAULT: ");

        System.out.println("\tPORTA : " + PORT);
        System.out.println("\tT1: " + T1);
        System.out.println("\tT3: " + T3);
        System.out.println("\tR: " + PORT);
        System.out.println("\tCONFIG FILE: " + CONFIGFILE);

        System.out.println("--Ficheiros");
        for (String fkey : myFiles.keySet()) {
            System.out.println("\t" + fkey + ":");

        }



    }

    public static void register(String path) {
        File file = new File(path);
        if (!file.exists()) {
            System.out.println("Ficheiro inexistente");
        } else {

            InetAddress ip = myIP.iterator().next();
            System.out.println(ip.getHostAddress());
            FileRegister fr = new FileRegister(file.getName(), file.getAbsolutePath(), ip, Peer.PORT, file.length());
            Peer.myFiles.put(fr.nome, fr);
            System.out.println("Registado: " + fr.nome + " com o ip " + fr.ip.getHostAddress());
        }
    }
}
