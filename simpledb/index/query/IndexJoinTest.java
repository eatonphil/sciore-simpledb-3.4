package simpledb.index.query;

import java.util.Map;
import simpledb.server.SimpleDB;
import simpledb.tx.Transaction;
import simpledb.record.*;
import simpledb.metadata.*;
import simpledb.plan.*;
import simpledb.query.*;
import simpledb.index.*;
import simpledb.index.planner.IndexJoinPlan;

// Find the grades of all students.

public class IndexJoinTest {
	public static void main(String[] args) {
		SimpleDB db = new SimpleDB("studentdb");
      MetadataMgr mdm = db.mdMgr();
      Transaction tx = db.newTx();

		// Find the index on StudentId.
		Map<String,IndexInfo> indexes = mdm.getIndexInfo("enroll", tx);
		IndexInfo sidIdx = indexes.get("studentid");

		// Get plans for the Student and Enroll tables
		Plan studentplan = new TablePlan(tx, "student", mdm);
		Plan enrollplan = new TablePlan(tx, "enroll", mdm);

		// Two different ways to use the index in simpledb:
		useIndexManually(studentplan, enrollplan, sidIdx, "sid");		
		useIndexScan(studentplan, enrollplan, sidIdx, "sid");

		tx.commit();
	}

	private static void useIndexManually(Plan p1, Plan p2, IndexInfo ii, String joinfield) {
		// Open scans on the tables.
		Scan s1 = p1.open();
		TableScan s2 = (TableScan) p2.open();  //must be a table scan
		Index idx = ii.open();

		// Loop through s1 records. For each value of the join field, 
		// use the index to find the matching s2 records.
		while (s1.next()) {
			Constant c = s1.getVal(joinfield);
			idx.beforeFirst(c);
			while (idx.next()) {
				// Use each datarid to go to the corresponding Enroll record.
				RID datarid = idx.getDataRid();
				s2.moveToRid(datarid);  // table scans can move to a specified RID.
				System.out.println(s2.getString("grade"));
			}
		}
		idx.close();
		s1.close();
		s2.close();
	}

	private static void useIndexScan(Plan p1, Plan p2, IndexInfo ii, String joinfield) {
		// Open an index join scan on the table.
		Plan idxplan = new IndexJoinPlan(p1, p2, ii, joinfield);
		Scan s = idxplan.open();

		while (s.next()) {
			System.out.println(s.getString("grade"));
		}
		s.close();
	}
}
