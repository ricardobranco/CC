
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NetworkPing implements Runnable {

    public static Collection<InetAddress> getSelfIPs() {
        Collection<InetAddress> res = new HashSet<InetAddress>();

        Enumeration<NetworkInterface> e = null;
        try {
            e = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e1) {
            System.out.println("Exit (Main): Failed find own address.");
            System.exit(1);
        }
        while (e.hasMoreElements()) {
            NetworkInterface n = (NetworkInterface) e.nextElement();
            Enumeration<InetAddress> ee = n.getInetAddresses();
            while (ee.hasMoreElements()) {
                InetAddress i = (InetAddress) ee.nextElement();
                System.out.println("One of my addresses is " + i.getHostAddress());
                if (Inet4Address.class.isInstance(i) && i.isLoopbackAddress() == false) {
                    System.out.println("\tAnd I'm going to add it.");
                    res.add(i);
                }
            }
        }
        return res;
    }

    public void run() {
        try {
            Collection<InetAddress> allIps = NetworkPing.getSelfIPs();

            for (InetAddress localhost : allIps) {
                // this code assumes IPv4 is used
                byte[] ip = localhost.getAddress();

                for (int i = 1; i <= 254; i++) {
                    ip[3] = (byte) i;
                    InetAddress address = InetAddress.getByAddress(ip);
                    try {
                        if (address.isReachable(100)) {
                            System.out.println(address + " machine is turned on and can be pinged");
                        } else if (!address.getHostAddress().equals(address.getHostName())) {
                            System.out.println(address + " machine is known in a DNS lookup");
                        } else {
                            System.out.println(address + " the host address and host name are equal, meaning the host name could not be resolved");
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(NetworkPing.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } catch (UnknownHostException ex) {
            Logger.getLogger(NetworkPing.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}