
import java.io.Serializable;
import java.net.InetAddress;

public class Resposta extends PDU implements Serializable {
    
    
    int sizeResposta;

    public Resposta(InetAddress ip, int port, int sizeResposta) {
        super(ip, port);
        this.sizeResposta = sizeResposta;
    }
}
