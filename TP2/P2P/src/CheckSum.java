import java.io.*;
import java.security.MessageDigest;


public class CheckSum {
   
 static String FileName;
 
 public CheckSum ( String FileName )
 {
  this.FileName = FileName;
 }
 
   public static byte[] createChecksum(String filename) throws Exception {
       
    File file = new File(FileName);
    int FILE_LENGTH = (int) file.length(); 
    
    System.out.println ("Cheksum Class takes role");
    System.out.println ("FILE LENGTH: "+ FILE_LENGTH );
    
    InputStream fis =  new FileInputStream(filename);


       byte[] buffer = new byte[FILE_LENGTH]; //Byte Array Lenght Could Be Changed
       MessageDigest complete = MessageDigest.getInstance("MD5");
       int numRead;


       do {
           numRead = fis.read(buffer);
           if (numRead > 0) { //If buffer is not empty, some data was read
               complete.update(buffer, 0, numRead); // Updates MessageDigest Object
           }
       } while (numRead != -1); // Until the end of file


       fis.close();
       return complete.digest();
   }
   
   public static String getMD5Checksum ( ) throws Exception {
    
    byte[] byteArray = createChecksum ( FileName );
    String result="";
    
    for ( int i=0; i < byteArray.length ; i++ )
    {
     //Converts Byte Array to Integer
     result+= Integer.toString( (byteArray[i] & 0xff) + 0x100, 16 ).substring(1);
    }
    return result;
   }
}
