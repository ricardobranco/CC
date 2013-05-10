
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import jline.ConsoleReader;

public class Cliente implements Runnable {

    List<Thread> threads;
    Peer peer;

    public Cliente(Peer p) {
        threads = new ArrayList<>();
        peer = p;
    }

    @Override
    public void run() {
        try {
            ConsoleReader cr = new jline.ConsoleReader();
            byte[] aEnviar;
            while (true) {
                String readline = cr.readLine();
                aEnviar = readline.getBytes();

                if (readline.length()>9 &&readline.toLowerCase().substring(0, 0x9).equals("register ")) {
                    InetAddress IPAddress = InetAddress.getByName("localhost");
                    ClienteDaemon daemon = new ClienteDaemon(IPAddress, aEnviar);
                    Thread t = new Thread(daemon);
                    threads.add(t);
                    t.start();
                } else {
                    for (InetAddress ia : peer.ips) {
                        ClienteDaemon daemon = new ClienteDaemon(ia, aEnviar);
                        Thread t = new Thread(daemon);
                        threads.add(t);
                        t.start();
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            for (Thread t : threads) {
                try {
                    t.join();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }

        }
    }
}
