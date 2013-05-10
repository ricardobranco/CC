
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Request implements Runnable {

    DatagramSocket ds;
    DatagramPacket dp;
    Peer peer;

    public Request(DatagramSocket ds, DatagramPacket dp, Peer peer) {
        this.ds = ds;
        this.dp = dp;
        this.peer = peer;
    }

    public void run() {
        try {
            PDU pedido = (PDU) Peer.desSerializa(dp.getData());
            DatagramPacket respostaDP;
            PDU respostaPDU;
            byte[] respostaByte;
            String pduName = pedido.getClass().getSimpleName();
            if (pduName.equals("Hello")) {
                respostaPDU = new Resposta(dp.getAddress(), dp.getPort(), dp.getData().length);
                respostaByte = Peer.serializa(respostaPDU);
                respostaDP = new DatagramPacket(respostaByte, respostaByte.length, respostaPDU.ip, respostaPDU.porta);
                ds.send(respostaDP);
            } else if (pduName.equals("Chunk")) {
            }


        } catch (IOException ex) {
            Logger.getLogger(Request.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Request.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
