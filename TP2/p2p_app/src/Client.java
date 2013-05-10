
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jline.ConsoleReader;


public class Client implements Runnable{

    Peer peer;
    
    public Client(Peer peer){
        this.peer = peer;
    }
    
    public void run(){
        try {
            ConsoleReader cr = new jline.ConsoleReader();
            while (true) {
                String getline = cr.readLine();
                if(getline.toLowerCase().equals("status")){
                    peer.status();
                }
                
                
            }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        
    }
    
    
    
}
