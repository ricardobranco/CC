
public class FileBuilder {
    
    byte[] bytes;
    int remaining;
    
    public FileBuilder(int size){
        this.bytes = new byte[size];
        this.remaining = size;
    }
    
    public void addChunk(ChunkResponse cr, int offset){
        for(int i = offset-1; i<cr.chunk.length;i++){
            bytes[i]=cr.chunk[i];
            remaining--;
        }
        
    }
    
}
