package simpledb.metadata;

import java.util.*;
import simpledb.tx.Transaction;
import simpledb.record.*;

/**
 * The statistics manager is responsible for
 * keeping statistical information about each table.
 * The manager does not store this information in the database.
 * Instead, it calculates this information on system startup,
 * and periodically refreshes it.
 * @author Edward Sciore
 */
class StatMgr {
   private TableMgr tblMgr;
   private Map<String,StatInfo> tablestats;
   private int numcalls;
   
   /**
    * Create the statistics manager.
    * The initial statistics are calculated by
    * traversing the entire database.
    * @param tx the startup transaction
    */
   public StatMgr(TableMgr tblMgr, Transaction tx) {
      this.tblMgr = tblMgr;
      refreshStatistics(tx);
   }
   
   /**
    * Return the statistical information about the specified table.
    * @param tblname the name of the table
    * @param layout the table's layout
    * @param tx the calling transaction
    * @return the statistical information about the table
    */
   public synchronized StatInfo getStatInfo(String tblname, 
                              Layout layout, Transaction tx) {
      numcalls++;
      if (numcalls > 100)
         refreshStatistics(tx);
      StatInfo si = tablestats.get(tblname);
      if (si == null) {
         si = calcTableStats(tblname, layout, tx);
         tablestats.put(tblname, si);
      }
      return si;
   }
   
   private synchronized void refreshStatistics(Transaction tx) {
      tablestats = new HashMap<String,StatInfo>();
      numcalls = 0;
      Layout tcatlayout = tblMgr.getLayout("tblcat", tx);
      TableScan tcat = new TableScan(tx, "tblcat", tcatlayout);
      while(tcat.next()) {
         String tblname = tcat.getString("tblname");
         Layout layout = tblMgr.getLayout(tblname, tx);
         StatInfo si = calcTableStats(tblname, layout, tx);
         tablestats.put(tblname, si);
      }
      tcat.close();
   }
   
   private synchronized StatInfo calcTableStats(String tblname, 
                              Layout layout, Transaction tx) {
      int numRecs = 0;
      int numblocks = 0;
      TableScan ts = new TableScan(tx, tblname, layout);
      while (ts.next()) {
         numRecs++;
         numblocks = ts.getRid().blockNumber() + 1;
      }
      ts.close();
      return new StatInfo(numblocks, numRecs);
   }
}
