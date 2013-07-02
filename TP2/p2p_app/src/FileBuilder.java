
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.logging.Level;
import java.util.logging.Logger;


public class FileBuilder {
    
    String nome;
    byte[] bytes;
    int remaining;
    
    public FileBuilder(int size, String nome){
        this.bytes = new byte[size];
        this.remaining = size;
        this.nome = nome;
    }
    
    public void addChunk(ChunkResponse cr, int offset){
        System.out.println("remaining"+this.remaining+":tamanho do chunk recebido"+cr.chunk.length);
        for(int i = offset-1; i<cr.chunk.length;i++){
            bytes[i]=cr.chunk[i];
          //  remaining--;
        }
        
    }
    
    
    public void saveFile(){
        try {
            String filedest = Peer.SHAREPATH+"/"+nome;
            RandomAccessFile raf = new RandomAccessFile(filedest, "rw");
            raf.write(bytes);
            FileRegister fr = new FileRegister(nome, filedest,Peer.myIP.iterator().next(),Peer.PORT,bytes.length);
            Peer.myFiles.put(nome, fr);
            System.out.println(nome+" criado com sucesso");
            raf.close();
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileBuilder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
