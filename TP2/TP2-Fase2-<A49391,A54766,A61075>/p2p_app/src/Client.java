
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jline.ConsoleReader;

public class Client implements Runnable {

    Peer peer;
    List<Thread> threads;

    public Client(Peer peer) {
        this.peer = peer;
        threads = new ArrayList<>();
    }

    @Override
    public void run() {
        try {
            ConsoleReader cr = new jline.ConsoleReader();
            while (true) {
                String getline = cr.readLine();
                ReadLine rl = new ReadLine(getline);
                Thread trl = new Thread(rl);
                threads.add(trl);
                trl.start();


            }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            for (Thread t : threads) {
                try {
                    t.join();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }


    }
}
