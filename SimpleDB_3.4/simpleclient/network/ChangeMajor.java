package network;
import java.sql.*;

import simpledb.jdbc.network.NetworkDriver;

public class ChangeMajor {
   public static void main(String[] args) {
      Driver d = new NetworkDriver();
      String url = "jdbc:simpledb://localhost";

      try (Connection conn = d.connect(url, null); 
            Statement stmt = conn.createStatement()) {
         String cmd = "update STUDENT set MajorId=30 "
               + "where SName = 'amy'";
         stmt.executeUpdate(cmd);
         System.out.println("Amy is now a drama major.");
      }
      catch(SQLException e) {
         e.printStackTrace();
      }
   }
}
