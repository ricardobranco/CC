
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author core
 */
public class Teste {
    private  int porta;
    private int T1;
    private int K;
    private String SHAREPATH;
    private int T2;
    private int T3;
    private int R;
    private int M;
    private int N;
    private int V; 
    private Set<InetAddress> viz;
    public Teste() {
    }

    public Teste(int porta, int T1, int K, String SHAREPATH, int T2, int T3, int R, int M, int N, int V, Set<InetAddress> viz) {
        this.porta = porta;
        this.T1 = T1;
        this.K = K;
        this.SHAREPATH = SHAREPATH;
        this.T2 = T2;
        this.T3 = T3;
        this.R = R;
        this.M = M;
        this.N = N;
        this.V = V;
        this.viz = viz;
    }

   

    

    public int getPorta() {
        return porta;
    }

    public void setPorta(int porta) {
        this.porta = porta;
    }

    public int getT1() {
        return T1;
    }

    public void setT1(int T1) {
        this.T1 = T1;
    }

    public int getK() {
        return K;
    }

    public void setK(int K) {
        this.K = K;
    }

    public String getSHAREPATH() {
        return SHAREPATH;
    }

    public void setSHAREPATH(String SHAREPATH) {
        this.SHAREPATH = SHAREPATH;
    }

    public int getT2() {
        return T2;
    }

    public void setT2(int T2) {
        this.T2 = T2;
    }

    public int getT3() {
        return T3;
    }

    public void setT3(int T3) {
        this.T3 = T3;
    }

    public int getR() {
        return R;
    }

    public void setR(int R) {
        this.R = R;
    }

    public int getM() {
        return M;
    }

    public void setM(int M) {
        this.M = M;
    }

    public int getN() {
        return N;
    }

    public void setN(int N) {
        this.N = N;
    }

    public int getV() {
        return V;
    }

    public void setV(int V) {
        this.V = V;
    }
    
    //public Config_Parser Parser(String args[]){
    public Teste Parser(String args){
        BufferedReader br = null;
        String line = "";
        int porta=0;
        int T1=0;
        int K=0;
        String SHAREPATH="";
        int T2=0;
        int T3=0;
        int R=0;
        int M=0;
        int N=0;
        int V=0;
        Set<InetAddress> vizinhos=new HashSet<InetAddress>();
        try {
            br = new BufferedReader(new FileReader(args));
            while ((line = br.readLine()) != null) {
               Matcher matcher = Pattern.compile("(?<=T1=)([^;]*?)(?=;)").matcher(line);
                if (matcher.find()) {
                     String someNumberStr = matcher.group(1);
                    // if you need this to be an int:
                    T1= Integer.parseInt(someNumberStr.trim());
                }
                matcher = Pattern.compile("(?<=Porta=)([^;]*?)(?=;)").matcher(line);
                if (matcher.find()) {
                     String someNumberStr = matcher.group(1);
                    // if you need this to be an int:
                    porta= Integer.parseInt(someNumberStr.trim());
                }
               matcher = Pattern.compile("(?<=K=)([^;]*?)(?=;)").matcher(line);
                if (matcher.find()) {
                     String someNumberStr = matcher.group(1);
                    // if you need this to be an int:
                    K= Integer.parseInt(someNumberStr.trim());
                }
                matcher = Pattern.compile("(?<=T2=)([^;]*?)(?=;)").matcher(line);
                if (matcher.find()) {
                     String someNumberStr = matcher.group(1);
                    // if you need this to be an int:
                    T2= Integer.parseInt(someNumberStr.trim());
                }
                matcher = Pattern.compile("(?<=T3=)([^;]*?)(?=;)").matcher(line);
                if (matcher.find()) {
                     String someNumberStr = matcher.group(1);
                    // if you need this to be an int:
                    T3= Integer.parseInt(someNumberStr.trim());
                }
                matcher = Pattern.compile("(?<=N=)([^;]*?)(?=;)").matcher(line);
                if (matcher.find()) {
                     String someNumberStr = matcher.group(1);
                    // if you need this to be an int:
                    N= Integer.parseInt(someNumberStr.trim());
                }
                matcher = Pattern.compile("(?<=M=)([^;]*?)(?=;)").matcher(line);
                if (matcher.find()) {
                     String someNumberStr = matcher.group(1);
                    // if you need this to be an int:
                    M= Integer.parseInt(someNumberStr.trim());
                }
                matcher = Pattern.compile("(?<=R=)([^;]*?)(?=;)").matcher(line);
                if (matcher.find()) {
                     String someNumberStr = matcher.group(1);
                    // if you need this to be an int:
                    R= Integer.parseInt(someNumberStr.trim());
                }
                 matcher = Pattern.compile("(?<=V=)([^;]*?)(?=;)").matcher(line);
                if (matcher.find()) {
                     String someNumberStr = matcher.group(1);
                    // if you need this to be an int:
                    V= Integer.parseInt(someNumberStr.trim());
                }
                matcher = Pattern.compile("(?<=Vizinho=)([^;]*?)(?=;)").matcher(line);
                if (matcher.find()) {
                     String someNumberStr = matcher.group(1);
                     System.out.println(someNumberStr);
                    // if you need this to be an int:
                    vizinhos.add(InetAddress.getByName(someNumberStr));
                }       
            }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Teste.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Teste.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(Teste.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
       return (new Teste(porta,T1,K,SHAREPATH,T2,T3,R,M,N,V,vizinhos));//falta os vizinhos / e o sharepath
    }

    @Override
    public String toString() {
        return "Teste{" + "porta=" + porta + ", T1=" + T1 + ", K=" + K + ", SHAREPATH=" + SHAREPATH + ", T2=" + T2 + ", T3=" + T3 + ", R=" + R + ", M=" + M + ", N=" + N + ", V=" + V + ", viz=" + viz + '}';
    }

   
    public static void main(String[] args) {
        Teste t=new Teste();
        String argss="conf.txt";
        t=t.Parser(argss);
         System.out.println(t.toString());
    }
}
