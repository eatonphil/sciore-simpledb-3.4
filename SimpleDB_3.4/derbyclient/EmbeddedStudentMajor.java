
import java.sql.*;
import org.apache.derby.jdbc.EmbeddedDriver;

public class EmbeddedStudentMajor {
   public static void main(String[] args) {
      String url = "jdbc:derby:studentdb";
      String qry = "select SName, DName "
            + "from DEPT, STUDENT "
            + "where MajorId = DId";

      Driver d = new EmbeddedDriver();
      try ( Connection conn = d.connect(url, null);
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

