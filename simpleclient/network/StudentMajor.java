package network;
import java.sql.*;

import simpledb.jdbc.network.NetworkDriver;

public class StudentMajor {
   public static void main(String[] args) {
      String url = "jdbc:simpledb://localhost";
      String qry = "select SName, DName "
                 + "from DEPT, STUDENT "
                 + "where MajorId = DId";

      Driver d = new NetworkDriver();
      try (Connection conn = d.connect(url, null);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(qry)) {

         System.out.println("Name\tMajor");
         while (rs.next()) {
            String sname = rs.getString("SName");
            String dname = rs.getString("DName");
            System.out.println(sname + "\t" + dname);
         }
      }
      catch(SQLException e) {
         e.printStackTrace();
      }
   }
}

