
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class ReadLine implements Runnable {

    String readline;

    public ReadLine(String readLine) {
        this.readline = readLine;
    }

    public void run() {

        String fileREG = "([a-zA-Z0-9_-]+.?[a-zA-Z0-9_-]+)";
        String pathREG = "(\\/?([A-Za-z_0-9]+\\/))?";
        String register = "register\\s";
        String get = "get\\s";
        String find = "find\\s";
        String status = "status";
        Pattern pregister = Pattern.compile(register + pathREG + fileREG);
        Pattern pget = Pattern.compile(get + fileREG);
        Pattern pstatus = Pattern.compile(status);
        Pattern pfind = Pattern.compile(find+fileREG);
        if (pregister.matcher(readline).matches()) {//REGISTO DE UM FICHEIRO
            List<String> regToken = new ArrayList<>();
            StringTokenizer st = new StringTokenizer(readline, " ");
            while (st.hasMoreTokens()) {
                regToken.add(st.nextToken());
            }
            Peer.register(regToken.get(1));



        } else if (pget.matcher(readline).matches()) {
        } else if (pstatus.matcher(readline).matches()) {
            Peer.status();
        } else if (pfind.matcher(readline).matches()) {
            Peer.find(readline.split(" ", 2)[1]);
        } else if (readline.equals("refresh")) {
            Checker.refresh();
        } else {
            System.out.println("Comando Invalido");
        }






    }
}
