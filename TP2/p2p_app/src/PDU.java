
import java.io.Serializable;
import java.net.InetAddress;

public abstract class PDU implements Serializable {

    InetAddress ip;
    int port;

    public PDU(InetAddress ip, int port) {
        this.ip = ip;
        this.port = port;
    }
}
