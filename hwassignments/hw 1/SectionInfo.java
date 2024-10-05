package network;

import java.sql.*;
import java.util.Scanner;
import simpledb.jdbc.network.NetworkDriver;

public class SectionInfo {
   public static void main(String[] args) {
      System.out.print("Enter a section number: ");
      Scanner sc = new Scanner(System.in);
      int sect = sc.nextInt();
      sc.close();

      String url = "jdbc:simpledb://localhost";
      String qry1 = "select prof from SECTION "
            + "where sectid = " + sect;
      String qry2 = "select grade from ENROLL "
            + "where sectionid = " + sect;

      Driver d = new NetworkDriver();
      try (Connection conn = d.connect(url, null);
            Statement stmt = conn.createStatement()) {
         ResultSet rs = stmt.executeQuery(qry1);
         String prof = null;
         if (rs.next()) 
            prof = rs.getString("prof");
         rs.close();

         if (prof == null)
            System.out.println("No such section.");        
         else {
            rs = stmt.executeQuery(qry2);
            int count = 0, countOfA = 0;
            while (rs.next()) {
               count++;
               if (rs.getString("grade").equals("A"))
                  countOfA++;
            }
            rs.close();
            System.out.println("Professor " + prof 
                  + " had " + count + " students and gave "
                  + countOfA + " A's.");
         }
      }
      catch(Exception e) {
         e.printStackTrace();
      }
   }
}
