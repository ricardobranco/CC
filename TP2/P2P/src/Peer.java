
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.net.*;

public class Peer {
    
    static int PORTA_SERVIDOR = 28280;
    static int PORTA_CHUNKS = 28281;
    static String TEMPFILEPATH = "temp/";
    static String DESTFILEPATH = "dest/";
    static int CHUNKSIZE = 1536;
    Servidor servidor;
    Cliente cliente;
    NetworkPing np;
    List<InetAddress> ips;
    HashMap<String, ArrayList<String>> files;

    public static void main(String args[]) {
        String bootstrap = null;
        try {
            bootstrap = args[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("SEM FICHEIRO");
            System.exit(-1);
        }

        try {
            File temp = new File(TEMPFILEPATH);
            temp.mkdir();
            File dest = new File(DESTFILEPATH);
            dest.mkdir();

            Peer peer = new Peer(bootstrap);
            Thread tservidor = new Thread(peer.servidor);
            Thread tcliente = new Thread(peer.cliente);
            Thread tnp = new Thread(peer.np);
           // tservidor.start();
           // tcliente.start();
            tnp.start();

          //  tservidor.join();
            //tcliente.join();
            tnp.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Peer(String bootstrap) {
        servidor = new Servidor(this);
        cliente = new Cliente(this);
        np = new NetworkPing();
        ips = collectIPs(bootstrap);
        files = new HashMap<>();
    }

    public void addNewIP(InetAddress iA) {
        boolean flag = true;
        for (InetAddress ip : ips) {
            if (iA.getHostAddress().equals(ip.getHostAddress())) {
                flag = false;
                break;
            }
        }
        if (flag) {
            ips.add(iA);
        }

    }

    public List<InetAddress> collectIPs(String file) {
        List<InetAddress> res = new ArrayList<>();

        try {
            FileInputStream fis = new FileInputStream(file);
            DataInputStream dis = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(dis));
            String newline;
            while ((newline = br.readLine()) != null) {
                //System.out.println(newline);
                InetAddress IPAddress = InetAddress.getByName(newline);
                res.add(IPAddress);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return res;
        }

    }
}
