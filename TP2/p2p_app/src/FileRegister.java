
import java.io.Serializable;
import java.net.InetAddress;

public class FileRegister implements Serializable{

    String nome;
    String path;
    InetAddress ip;
    int port;
    long size; //em bytes
    
    
    public FileRegister(String nome, String path, InetAddress ip, int port, long size) {
        this.nome = nome;
        this.path = path;
        this.ip = ip;
        this.port = port;
        this.size = size;
    }
    
    public int hashCode(){
        return ip.hashCode();
    }
    
}
