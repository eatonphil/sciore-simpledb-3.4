import java.sql.*;
import simpledb.jdbc.network.NetworkDriver;
import simpledb.jdbc.embedded.EmbeddedDriver;

public class HW9Client {
	public static void main(String[] args) {
		Driver d = new EmbeddedDriver();
		String url = "jdbc:simpledb:studentdb";
		try (Connection conn = d.connect(url, null);
		     Statement stmt = conn.createStatement() ) {

			System.out.print("Changing amy's grad year to null: ");
			String cmd = "update student set gradyear = NULL "
					   + "where sname = 'amy'";
			stmt.executeUpdate(cmd);
			System.out.println("DONE\n");

			System.out.print("Inserting a record for tom: ");
			cmd = "insert into student(sid, sname, gradyear, majorid) "
				+ "values (10, 'tom', null, 20)";
			stmt.executeUpdate(cmd);
			System.out.println("DONE\n");

			System.out.println("Here are the students graduating after 2019 and before 2022");
			cmd = "select sname from student "
				+ "where gradyear > 2019 and gradyear < 2022";
			ResultSet rs = stmt.executeQuery(cmd);
			while (rs.next()) {
				String sname = rs.getString("SName");
				System.out.print(sname + " ");
			}
			rs.close();
			System.out.println("\n");

			System.out.println("Here are the students having a null grad year");
			cmd = "select sname from student "
				+ "where gradyear is null";
			rs = stmt.executeQuery(cmd);
			while (rs.next()) {
				String sname = rs.getString("SName");
				System.out.print(sname + " ");
			}

			rs.close();
			System.out.println("\n");
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
