package simpledb.record;

import simpledb.server.SimpleDB;
import simpledb.tx.Transaction;

public class HW7Test {
   public static void main(String[] args) {
      SimpleDB db = new SimpleDB("hw7test", 400, 8);
      Transaction tx = db.newTx();

      Schema sch = new Schema();
      sch.addIntField("A");
      sch.addStringField("B", 50);
      Layout layout = new Layout(sch);
      TableScan ts = new TableScan(tx, "T", layout);
      ts.beforeFirst();
      
      populateTable(ts);

      System.out.println("Here are the first 10 records");
      ts.beforeFirst();
      for (int i=0; i<10; i++) {
         ts.next();
         printCurrentRecord(ts);
      }

      System.out.println("\nHere are the 5 recent records backwards");
      for (int i=0; i<5; i++) {
         ts.previous();
         printCurrentRecord(ts);
      }
   
      System.out.println("\nHere is the first record again");
      while(ts.previous())
         ; // do nothing
      ts.next();
      ts.setString("B", "not null");
      printCurrentRecord(ts);

   
      System.out.println("\nAnd here are the last 10 records");
      ts.afterLast();
      for (int i=0; i<10; i++) 
         ts.previous();
      for (int i=0; i<10; i++) {
         printCurrentRecord(ts);
         ts.next();
      }
      
      ts.close();
      tx.commit();
   }

   // Create 500 records having A-values from 1 to 500.
   // Every even record will have a B-value of null.
   private static void populateTable(TableScan ts) {
      for (int i=1; i<=500; i++) {
         ts.insert();
         ts.setInt("A", i);
         if (i%2 == 0)
            ts.setNull("B");
         else
            ts.setString("B", "record"+i);
}
      ts.beforeFirst();
      while (ts.next())
    	  printCurrentRecord(ts);
      System.out.println("done");
   }

   private static void printCurrentRecord(TableScan ts) {
      int aval = ts.getInt("A");
      String bval = ts.isNull("B") ? "null" : ts.getString("B");
      System.out.println(aval + "\t" + bval);
   }
}