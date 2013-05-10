
import java.io.Serializable;
import java.net.InetAddress;


public class Chunk extends PDU implements Serializable{
    
    int offset;
    int size; //EM BYTES
    
    public Chunk(InetAddress ip, int port, int offset, int size){
        super(ip, port);
        this.offset = offset;
        this.size = size;
    }
    

}
