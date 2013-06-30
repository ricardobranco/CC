
import java.io.Serializable;
import java.net.InetAddress;


public class Chunk extends PDU implements Serializable{
    
    int offset;
    int size; //EM BYTES
    String nome;
    public Chunk(InetAddress ip, int port, int offset, int size, String nome){
        super(ip, port);
        this.offset = offset;
        this.size = size;
        this.nome = nome;
    }
    

}
