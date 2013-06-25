
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

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
            byte[] byte_resposta;
            switch (pduName) {
                case "Hello":
                    HelloResponse hr = new HelloResponse(dp.getAddress(), dp.getPort(), Peer.validPeer,Peer.completed);
                    byte_resposta = Peer.serializa(hr);
                    resposta = new DatagramPacket(byte_resposta, byte_resposta.length, dp.getAddress(), dp.getPort());
                    ds.send(resposta);
                    break;
                case "Chunk":
                    break;
                case "Comand":
                    

                    break;
            }


        } catch (IOException ex) {
            Logger.getLogger(Request.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Request.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
