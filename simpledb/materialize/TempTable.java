package simpledb.materialize;

import simpledb.tx.Transaction;
import simpledb.query.*;
import simpledb.record.*;

/**
 * A class that creates temporary tables.
 * A temporary table is not registered in the catalog.
 * The class therefore has a method getTableInfo to return the 
 * table's metadata. 
 * @author Edward Sciore
 */
public class TempTable {
   private static int nextTableNum = 0;
   private Transaction tx;
   private String tblname;
   private Layout layout;
   
   /**
    * Allocate a name for for a new temporary table
    * having the specified schema.
    * @param sch the new table's schema
    * @param tx the calling transaction
    */
   public TempTable(Transaction tx, Schema sch) {
      this.tx = tx;
      tblname = nextTableName();
      layout = new Layout(sch);
   }
   
   /**
    * Open a table scan for the temporary table.
    */
   public UpdateScan open() {
      return new TableScan(tx, tblname, layout);
   }
   
   public String tableName() {
      return tblname;
   }
   
   /**
    * Return the table's metadata.
    * @return the table's metadata
    */
   public Layout getLayout() {
      return layout;
   }

   private static synchronized String nextTableName() {
      nextTableNum++;
      return "temp" + nextTableNum;
   }
}