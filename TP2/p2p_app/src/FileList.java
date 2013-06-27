
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


public class FileList implements Serializable{

    Set<FileRegister> files;
    
    
    public FileList(){
        this.files = new HashSet<>();
    }
    
    
    public void addFile(FileRegister fr){
        files.add(fr);
    }
    
    public void remFile(FileRegister fr){
        files.remove(fr);
    }
    
    
    
    
}
