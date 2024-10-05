package simpledb.plan;
import simpledb.server.SimpleDB;
import simpledb.tx.Transaction;
import simpledb.metadata.MetadataMgr;
import simpledb.query.*;
import java.util.*;

public class SingleTablePlanTest {
	public static void main(String[] args) throws Exception {
      SimpleDB db = new SimpleDB("studentdb");
      MetadataMgr mdm = db.mdMgr();
      Transaction tx = db.newTx();

		//the STUDENT node
		Plan p1 = new TablePlan(tx, "student", mdm);

		// the Select node for "major = 10"
		Term t = new Term(new Expression("majorid"), new Expression(new Constant(10)));
		Predicate pred = new Predicate(t); 
		Plan p2 = new SelectPlan(p1, pred);

		// the Select node for "gradyear = 2020"
		Term t2 = new Term(new Expression("gradyear"), new Expression(new Constant(2020)));
		Predicate pred2 = new Predicate(t2); 
		Plan p3 = new SelectPlan(p2, pred2);

		// the Project node
		List<String> c = Arrays.asList("sname", "majorid", "gradyear");
		Plan p4 = new ProjectPlan(p3, c);

		// Look at R(p) and B(p) for each plan p.
		printStats(1, p1); printStats(2, p2); printStats(3, p3); printStats(4, p4);
		
		// Change p2 to be p2, p3, or p4 to see the other scans in action.
		// Changing p2 to p4 will throw an exception because SID is not in the projection list.
		Scan s = p2.open();
		while (s.next())
			System.out.println(s.getInt("sid") + " " + s.getString("sname")  
			+ " " + s.getInt("majorid") + " " + s.getInt("gradyear"));
		s.close();
	}
	
	private static void printStats(int n, Plan p) {
		System.out.println("Here are the stats for plan p" + n);
		System.out.println("\tR(p" + n + "): " + p.recordsOutput());
		System.out.println("\tB(p" + n + "): " + p.blocksAccessed());
		System.out.println();	
	}
}
