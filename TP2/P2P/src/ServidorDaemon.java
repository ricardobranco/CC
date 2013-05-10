import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ServidorDaemon implements Runnable {

    DatagramSocket s;
    DatagramPacket pedido;
    Servidor servidor;

    public ServidorDaemon(DatagramSocket ds, DatagramPacket dp,Servidor serv) {
        s = ds;
        pedido = dp;
        servidor = serv;
    }

    @Override
    public void run() {

        try {
            String mensagem = new String(pedido.getData(), 0, pedido.getLength());
            InetAddress IPAddress = pedido.getAddress();
            int porta = pedido.getPort();
            System.out.println(IPAddress.getHostAddress()+" > "+mensagem);
            byte[] aEnviar;
            if(mensagem.length() > 9 && mensagem.substring(0, 9).toLowerCase().equals("register ")){
                String file = mensagem.substring(9);
                boolean resUP = servidor.register(file);
                
            }
           
                aEnviar = mensagem.toLowerCase().getBytes();
            DatagramPacket resposta = new DatagramPacket(aEnviar, aEnviar.length, IPAddress, porta);
            s.send(resposta);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
