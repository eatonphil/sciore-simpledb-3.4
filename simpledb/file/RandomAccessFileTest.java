package simpledb.file;
import java.io.*;

public class RandomAccessFileTest {
   public static void main(String[] args) throws IOException {
      File file = new File("testfile");            
      try {
         // initialize the file
         RandomAccessFile f1 = new RandomAccessFile(file, "rws");
         f1.seek(123);
         f1.writeInt(999);
         f1.close();

         // increment the file
         RandomAccessFile f2 = new RandomAccessFile(file, "rws");
         f2.seek(123);
         int n = f2.readInt();
         f2.seek(123);
         f2.writeInt(n+1);
         f2.close();

         // re-read the file
         RandomAccessFile f3 = new RandomAccessFile(file, "rws");
         f3.seek(123);
         System.out.println("The new value is " + f3.readInt());
         f3.close();
      }
      catch (IOException e) {
         e.printStackTrace();
      }
   }
}