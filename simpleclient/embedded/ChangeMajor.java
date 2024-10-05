package embedded;
import java.sql.*;
import simpledb.jdbc.embedded.EmbeddedDriver;

public class ChangeMajor {
   public static void main(String[] args) {
      Driver d = new EmbeddedDriver();
      String url = "jdbc:simpledb:studentdb";

      try (Connection conn = d.connect(url, null); 
            Statement stmt = conn.createStatement()) {
         String cmd = "update STUDENT "
                    + "set MajorId=30 "
                    + "where SName = 'amy'";
         stmt.executeUpdate(cmd);
         System.out.println("Amy is now a drama major.");
      }
      catch(SQLException e) {
         e.printStackTrace();
      }
   }
}
