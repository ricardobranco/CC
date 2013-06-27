
import java.io.Serializable;
import java.net.InetAddress;

public class Find extends PDU implements Serializable {

    String arg;

    public Find(String arg, InetAddress ip, int port) {
        super(ip, port);
        this.arg = arg;
    }
}
