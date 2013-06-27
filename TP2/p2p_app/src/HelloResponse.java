
import java.io.Serializable;
import java.net.InetAddress;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class HelloResponse extends PDU implements Serializable {

    Set<InetAddress> valid_peer;
    Map<String,FileRegister> filesComp;
    Map<String,FileList> filesKnow;
    
    
    public HelloResponse(InetAddress ip, int port, Set<InetAddress> valid_peer, Map<String,FileRegister> ficheirosCompletos, Map<String,FileList> ficheirosConhecidos) {
        super(ip,port);
        this.valid_peer = valid_peer;
        this.filesComp = ficheirosCompletos;
        this.filesKnow = ficheirosConhecidos;
    }
}
