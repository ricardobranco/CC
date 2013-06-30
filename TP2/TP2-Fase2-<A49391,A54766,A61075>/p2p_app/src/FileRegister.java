
import java.io.Serializable;
import java.net.InetAddress;
import java.util.Objects;

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

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.ip);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FileRegister other = (FileRegister) obj;
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        if (!Objects.equals(this.path, other.path)) {
            return false;
        }
        if (!Objects.equals(this.ip, other.ip)) {
            return false;
        }
        if (this.port != other.port) {
            return false;
        }
        if (this.size != other.size) {
            return false;
        }
        return true;
    }
    
 
    
}
