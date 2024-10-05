package simpledb.tx.recovery;

import simpledb.server.SimpleDB;
import simpledb.file.*;
import simpledb.log.LogMgr;
import simpledb.buffer.BufferMgr;
import simpledb.tx.Transaction;
import java.util.*;

public class HW4TestA {
	private static Collection<Transaction> uncommittedTxs = new ArrayList<Transaction>();
	
	public static void main(String[] args) {
      SimpleDB db = new SimpleDB("txtest", 400, 8); 
      FileMgr fm = db.fileMgr();
      LogMgr lm = db.logMgr();
      BufferMgr bm = db.bufferMgr();

		Transaction[]  txs = new Transaction[18];
		Transaction master = new Transaction(fm, lm, bm);
		for (int i=2; i<18; i++) {
			BlockId blk = master.append("testfile");
			txs[i] = new Transaction(fm, lm, bm);
			uncommittedTxs.add(txs[i]);
			txs[i].pin(blk);
			int x = txs[i].getInt(blk, 99);
			txs[i].setInt(blk, 99, 1000+i, true);
			System.out.println("transaction " + i + " setint old=" + x + " new=" + 1000+i);
			txs[i].unpin(blk);
			if (i%3==2)
				pareDown();
		}	
		master.commit();
	}

	// commit half of the uncommitted txs
	private static void pareDown() {
		Iterator<Transaction> iter = uncommittedTxs.iterator();
		int count = 0;
		while (iter.hasNext()) { // loop back to the beginning
			Transaction tx = iter.next();
			if (count % 2 == 0) {
				tx.commit();
				iter.remove();
			}				
			count++;
		}
	}
}
