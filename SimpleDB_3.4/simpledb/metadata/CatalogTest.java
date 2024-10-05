package simpledb.metadata;

import simpledb.server.SimpleDB;
import simpledb.tx.Transaction;
import simpledb.record.*;

public class CatalogTest {
   public static void main(String[] args) throws Exception {
      SimpleDB db = new SimpleDB("tabletest", 400, 8);
      Transaction tx = db.newTx();
      TableMgr tm = new TableMgr(false, tx);
      Layout tcatLayout = tm.getLayout("tblcat", tx);

      System.out.println("Here are all the tables and their lengths.");
      TableScan ts = new TableScan(tx, "tblcat", tcatLayout);
      while (ts.next()) {
         String tname = ts.getString("tblname");
         int slotsize = ts.getInt("slotsize");
         System.out.println(tname + " " + slotsize);
      }
      ts.close();

      System.out.println("\nHere are the fields for each table and their offsets");
      Layout fcatLayout = tm.getLayout("fldcat", tx);
      ts = new TableScan(tx, "fldcat", fcatLayout);
      while (ts.next()) {
         String tname = ts.getString("tblname");
         String fname = ts.getString("fldname");
         int offset   = ts.getInt("offset");
         System.out.println(tname + " " + fname + " " + offset);
      }
      ts.close();
   }
}

