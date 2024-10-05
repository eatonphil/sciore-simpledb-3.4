package simpledb.query;

import simpledb.server.SimpleDB;
import simpledb.tx.Transaction;
import simpledb.record.*;

public class ProductTest {
   public static void main(String[] args) throws Exception {
      SimpleDB db = new SimpleDB("producttest");
      Transaction tx = db.newTx();

      Schema sch1 = new Schema();
      sch1.addIntField("A");
      sch1.addStringField("B", 9);
      Layout layout1 = new Layout(sch1);
      TableScan ts1 = new TableScan(tx, "T1", layout1);

      Schema sch2 = new Schema();
      sch2.addIntField("C");
      sch2.addStringField("D", 9);
      Layout layout2 = new Layout(sch2);
      TableScan ts2 = new TableScan(tx, "T2", layout2);

      ts1.beforeFirst();
      int n = 200;
      System.out.println("Inserting " + n + " records into T1.");
      for (int i=0; i<n; i++) {
         ts1.insert();
         ts1.setInt("A", i);
         ts1.setString("B", "aaa"+i);
      }
      ts1.close();

      ts2.beforeFirst();
      System.out.println("Inserting " + n + " records into T2.");
      for (int i=0; i<n; i++) {
         ts2.insert();
         ts2.setInt("C", n-i-1);
         ts2.setString("D", "bbb"+(n-i-1));
      }
      ts2.close();

      Scan s1 = new TableScan(tx, "T1", layout1);
      Scan s2 = new TableScan(tx, "T2", layout2);
      Scan s3 = new ProductScan(s1, s2);
      while (s3.next())
         System.out.println(s3.getString("B")); 
      s3.close();
      tx.commit();
   }
}
