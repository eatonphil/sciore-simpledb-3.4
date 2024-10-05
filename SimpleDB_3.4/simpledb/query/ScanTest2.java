package simpledb.query;

import java.util.*;
import simpledb.server.SimpleDB;
import simpledb.tx.Transaction;
import simpledb.record.*;

public class ScanTest2 {
   public static void main(String[] args) throws Exception {
      SimpleDB db = new SimpleDB("scantest2");
      Transaction tx = db.newTx();

      Schema sch1 = new Schema();
      sch1.addIntField("A");
      sch1.addStringField("B", 9);
      Layout layout1 = new Layout(sch1);
      UpdateScan us1 = new TableScan(tx, "T1", layout1);
      us1.beforeFirst();
      int n = 200;
      System.out.println("Inserting " + n + " records into T1.");
      for (int i=0; i<n; i++) {
         us1.insert();
         us1.setInt("A", i);
         us1.setString("B", "bbb"+i);
      }
      us1.close();

      Schema sch2 = new Schema();
      sch2.addIntField("C");
      sch2.addStringField("D", 9);
      Layout layout2 = new Layout(sch2);
      UpdateScan us2 = new TableScan(tx, "T2", layout2);
      us2.beforeFirst();
      System.out.println("Inserting " + n + " records into T2.");
      for (int i=0; i<n; i++) {
         us2.insert();
         us2.setInt("C", n-i-1);
         us2.setString("D", "ddd"+(n-i-1));
      }
      us2.close();

      Scan s1 = new TableScan(tx, "T1", layout1);
      Scan s2 = new TableScan(tx, "T2", layout2);
      Scan s3 = new ProductScan(s1, s2);
      // selecting all records where A=C
      Term t = new Term(new Expression("A"), new Expression("C")); 
      Predicate pred = new Predicate(t);
      System.out.println("The predicate is " + pred);
      Scan s4 = new SelectScan(s3, pred);

      // projecting on [B,D]
      List<String> c = Arrays.asList("B", "D");
      Scan s5 = new ProjectScan(s4, c);
      while (s5.next())
         System.out.println(s5.getString("B") + " " + s5.getString("D")); 
      s5.close();
      tx.commit();
   }
}
