
import java.io.Serializable;
import java.net.InetAddress;

public class ChunkResponse extends PDU implements Serializable {

    byte[] chunk;

    public ChunkResponse(InetAddress ip, int port, byte[] chunk) {
        super(ip, port);
        this.chunk = chunk;
    }
}
