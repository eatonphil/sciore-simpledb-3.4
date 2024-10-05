package embedded;
import java.sql.*;

import simpledb.jdbc.embedded.EmbeddedDriver;

public class StudentMajor {
   public static void main(String[] args) {
      String url = "jdbc:simpledb:studentdb";
      String qry = "select SName, DName "
                 + "from DEPT, STUDENT "
                 + "where MajorId = DId";

      Driver d = new EmbeddedDriver();
      try (Connection conn = d.connect(url, null);
            Statement stmt = conn.createStatement()) {
         
         System.out.println("Name\tMajor");
         ResultSet rs = stmt.executeQuery(qry);
         while (rs.next()) {
            String sname = rs.getString("SName");
            String dname = rs.getString("DName");
            System.out.println(sname + "\t" + dname);
         }
         rs.close();
      }
      catch(SQLException e) {
         e.printStackTrace();
      }
   }
}

