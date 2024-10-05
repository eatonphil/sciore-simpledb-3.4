import java.sql.*;
import org.apache.derby.jdbc.ClientDataSource;

public class BadStudentMajor {
   public static void main(String[] args) {
      ClientDataSource ds = new ClientDataSource();
      ds.setServerName("localhost");
      ds.setDatabaseName("studentdb");
      Connection conn = null;
      try {
         conn = ds.getConnection();
         conn.setAutoCommit(false);
         try (Statement stmt1 = conn.createStatement();
              Statement stmt2 = conn.createStatement(
                     ResultSet.TYPE_SCROLL_INSENSITIVE, 
                     ResultSet.CONCUR_READ_ONLY);
               ResultSet rs1 = stmt1.executeQuery(
                     "select * from STUDENT");
               ResultSet rs2 = stmt2.executeQuery(
                     "select * from DEPT") ) {

            System.out.println("Name\tMajor");
            while (rs1.next()) {
               // get the next student
               String sname = rs1.getString("SName");
               String dname = null;
               rs2.beforeFirst();
               while (rs2.next())
                  // search for the major department of that student
                  if (rs2.getInt("DId") == rs1.getInt("MajorId")) {
                     dname = rs2.getString("DName");
                     break;
                  }
               System.out.println(sname + "\t" + dname);
            }
         }
         conn.commit();
         conn.close();
      }
      catch(SQLException e) {
         e.printStackTrace();
         try {
            if (conn != null) {
               conn.rollback();
               conn.close();
            }
         }
         catch (SQLException e2) {}
      }
   }
}
