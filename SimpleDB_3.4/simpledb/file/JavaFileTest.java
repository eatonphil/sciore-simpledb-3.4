package simpledb.file;
import java.io.*;

public class JavaFileTest {
   public static void main(String[] args) throws IOException {
      File dbfile = new File("testfile");            
 
      RandomAccessFile f = new RandomAccessFile(dbfile, "rws");
      int pos1 = 88;
      int len1 = writeString(f, pos1, "abcdefghijklm");
      int pos2 = pos1 + len1;
      writeInt(f, pos2, 345);
      f.close();
      
      RandomAccessFile g = new RandomAccessFile(dbfile, "rws");
      System.out.println("offset " + pos2 + " contains " + readInt(g, pos2));
      System.out.println("offset " + pos1 + " contains " + readString(g, pos1));
      g.close();
   }
   
   private static int readInt(RandomAccessFile f, int pos) throws IOException {
      f.seek(pos);
      return f.readInt();
   }
   
   private static int writeInt(RandomAccessFile f, int pos, int n) throws IOException {
      f.seek(pos);
      f.writeInt(n);
      return Integer.BYTES;
   }

   private static String readString(RandomAccessFile f, int pos) throws IOException {
      f.seek(pos);
      int len = f.readInt();
      byte[] bytes = new byte[len];
      f.read(bytes);
      return new String(bytes);
   }
   
   private static int writeString(RandomAccessFile f, int pos, String s) throws IOException {
      f.seek(pos);
      int len = s.length();
      byte[] bytes = s.getBytes();
      f.writeInt(len);
      f.write(bytes);
      return Integer.BYTES + len;
   }
}