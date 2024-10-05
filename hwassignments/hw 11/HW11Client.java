
import java.sql.*;
import simpledb.jdbc.embedded.EmbeddedDriver;

public class HW11Client {
	public static void main(String[] args) {
		String url = "jdbc:simpledb:studentdb";
		String cmd = "update STUDENT "    +
		             "set GradYear=null " +
		             "where MajorId=30";
		
		String qry = "select SName, GradYear, MajorId " +
					 "from STUDENT ";

		Driver d = new EmbeddedDriver();
		try (Connection conn = d.connect(url, null)) {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(cmd);
			System.out.println("Name\tYear\tMajor");
			ResultSet rs = stmt.executeQuery(qry);
			rs.afterLast();
			while (rs.previous()) {
				String sname = rs.getString("SName");
				int gradyear = rs.getInt("GradYear");
				String gy = rs.wasNull() ? "null" : gradyear+"";
				int major = rs.getInt("MajorId");		
				System.out.println(sname + "\t" + gy + "\t" + major);
			}
			rs.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
}

