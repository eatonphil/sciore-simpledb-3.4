package simpledb.tx.recovery;

import simpledb.buffer.BufferMgr;
import simpledb.file.FileMgr;
import simpledb.log.LogMgr;
import simpledb.server.SimpleDB;
import simpledb.tx.Transaction;

public class HW4TestB {
   public static void main(String[] args) {
      SimpleDB db = new SimpleDB("txtest", 400, 8); 
      FileMgr fm = db.fileMgr();
      LogMgr lm = db.logMgr();
      BufferMgr bm = db.bufferMgr();
      Transaction tx = new Transaction(fm, lm, bm);
      System.out.println("Initiating Recovery");
      System.out.println("Here are the visited log records");
      tx.recover();
   }
}
