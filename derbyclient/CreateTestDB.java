import java.sql.*;
import org.apache.derby.jdbc.ClientDriver;

public class CreateTestDB {
   public static void main(String[] args) {
      String url = "jdbc:derby://localhost/testdb;create=true";

      Driver d = new ClientDriver();
      try (Connection conn = d.connect(url, null)) {
         System.out.println("Database Created");
      }
      catch(SQLException e) {
         e.printStackTrace();
      }
   }
}

