
import java.io.IOException;
import java.io.RandomAccessFile;
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
                    Peer.addIP(dp.getAddress());
                    HelloResponse hr = new HelloResponse(dp.getAddress(), dp.getPort(), Peer.validPeer, Peer.myFiles, Peer.knowFiles);
                    byte_resposta = Peer.serializa(hr);
                    resposta = new DatagramPacket(byte_resposta, byte_resposta.length, dp.getAddress(), dp.getPort());
                    ds.send(resposta);
                    break;
                case "Find":
                    Find find = (Find) Peer.desSerializa(dp.getData());
                    Peer.addIP(dp.getAddress());
                    FindResponse fr = new FindResponse(dp.getAddress(), dp.getPort(), Peer.MatchFiles(find.arg));
                    byte_resposta = Peer.serializa(fr);
                    resposta = new DatagramPacket(byte_resposta, byte_resposta.length, dp.getAddress(), dp.getPort());
                    ds.send(resposta);
                    break;
                case "Chunk":
                    Chunk chunk = (Chunk) Peer.desSerializa(dp.getData());
                    Peer.addIP(dp.getAddress());
                    RandomAccessFile raf = new RandomAccessFile(Peer.myFiles.get(chunk.nome).path, "rw");
                    byte[] bchunk = new byte[chunk.size];
                    System.out.println("Offset: "+chunk.offset+ " size: "+chunk.size);
                    raf.read(bchunk, chunk.offset-1, chunk.size);
                    ChunkResponse cresp = new ChunkResponse(dp.getAddress(), dp.getPort(), bchunk);
                    byte_resposta = Peer.serializa(cresp);
                    resposta = new DatagramPacket(byte_resposta, byte_resposta.length, dp.getAddress(), dp.getPort());
                    ds.send(resposta);


                    break;




            }


        } catch (IOException ex) {
            Logger.getLogger(Request.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Request.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
