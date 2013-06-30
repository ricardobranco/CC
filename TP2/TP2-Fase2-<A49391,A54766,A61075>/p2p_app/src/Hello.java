
import java.io.Serializable;
import java.net.InetAddress;

public class Hello extends PDU implements Serializable {

    public Hello(InetAddress ip, int porta) {
        super(ip, porta);
    }

  
}
