
import java.io.Serializable;
import java.net.InetAddress;

public abstract class PDU implements Serializable {

    InetAddress ip;
    int porta;

    public PDU(InetAddress ip, int porta) {
        this.ip = ip;
        this.porta = porta;
    }
}
