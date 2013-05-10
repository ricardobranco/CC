
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
            DatagramPacket resposta;
            String pduName = pedido.getClass().getSimpleName();


            if (pduName.equals("Hello")) {
                resposta = new DatagramPacket(dp.getData(), dp.getData().length, dp.getAddress(), dp.getPort());
                ds.send(resposta);
            } else if (pduName.equals("Chunk")) {
            }


        } catch (IOException ex) {
            Logger.getLogger(Request.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Request.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
