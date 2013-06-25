
import java.io.Serializable;
import java.net.InetAddress;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class HelloResponse extends PDU implements Serializable {

    Set<InetAddress> valid_peer;
    Map<String,Set<FileRegister>> ficheiros;
    
    
    public HelloResponse(InetAddress ip, int port, Set<InetAddress> valid_peer, Map<String,Set<FileRegister>> ficheiros) {
        super(ip,port);
        this.valid_peer = valid_peer;
        this.ficheiros = ficheiros;
    }
}
