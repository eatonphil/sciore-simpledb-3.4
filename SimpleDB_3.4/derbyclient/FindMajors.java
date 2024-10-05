import java.sql.*;
import java.util.Scanner;
import org.apache.derby.jdbc.ClientDataSource;

public class FindMajors {
   public static void main(String[] args) {
      System.out.print("Enter a department name: ");
      Scanner sc = new Scanner(System.in);
      String major = sc.next();
      sc.close();
      String qry = "select sname, gradyear "
            + "from student, dept "
            + "where did = majorid "
            + "and dname = '" + major + "'";

      ClientDataSource ds = new ClientDataSource();
      ds.setServerName("localhost");
      ds.setDatabaseName("studentdb");
      try (Connection conn = ds.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(qry)) {

         System.out.println("Here are the " + major + " majors");
         System.out.println("Name\tGradYear");
         while (rs.next()) {
            String sname = rs.getString("sname");
            int gradyear = rs.getInt("gradyear");
            System.out.println(sname + "\t" + gradyear);
         }
      }
      catch(Exception e) {
         e.printStackTrace();
      }
   }
}
