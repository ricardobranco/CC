
import java.io.Serializable;
import java.net.InetAddress;
import java.util.Set;

public class FindResponse extends PDU implements Serializable {

    Set<FileRegister> files;

    public FindResponse(InetAddress ip, int port, Set<FileRegister> files) {
        super(ip, port);
        this.files = files;
    }
}
