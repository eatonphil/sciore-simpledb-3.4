import java.sql.*;
import org.apache.derby.jdbc.ClientDriver;

public class StudentMajor {
   public static void main(String[] args) {
      String url = "jdbc:derby://localhost/studentdb";
      String qry = "select SName, DName "
            + "from DEPT, STUDENT "
            + "where MajorId = DId";

      Driver d = new ClientDriver();
      try ( Connection conn = d.connect(url, null);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(qry)) {
         System.out.println("Name\tMajor");
         while (rs.next()) {
            String sname = rs.getString("SName");
            String dname = rs.getString("DName");
            System.out.println(sname + "\t" + dname);
         }
         printSchema(rs);
      }
      catch(SQLException e) {
         e.printStackTrace();
      }
   }
   
   private static void printSchema(ResultSet rs) throws SQLException {
      System.out.println("\nHere is the schema:");
      ResultSetMetaData md = rs.getMetaData();
      for(int i=1; i<=md.getColumnCount(); i++) {
         String name  = md.getColumnName(i);
         int size     = md.getColumnDisplaySize(i);
         int typecode = md.getColumnType(i);
         String type;
         if (typecode == Types.INTEGER)
            type = "int";
         else if (typecode == Types.VARCHAR)
            type = "string";
         else
            type = "other";
         System.out.println("  " + name + "\t" + type + "\t" + size);
      }
   }
}
