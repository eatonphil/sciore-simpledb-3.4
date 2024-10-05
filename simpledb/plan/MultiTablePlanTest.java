package simpledb.plan;
import simpledb.server.SimpleDB;
import simpledb.metadata.MetadataMgr;
import simpledb.query.*;
import simpledb.tx.Transaction;

public class MultiTablePlanTest {
   public static void main(String[] args) throws Exception {
      SimpleDB db = new SimpleDB("studentdb");
      MetadataMgr mdm = db.mdMgr();
      Transaction tx = db.newTx();

      //the STUDENT node
      Plan p1 = new TablePlan(tx, "student", mdm);

      //the DEPT node
      Plan p2 = new TablePlan(tx, "dept", mdm);

      //the Product node for student x dept
      Plan p3 = new ProductPlan(p1, p2);

      // the Select node for "majorid = did"
      Term t = new Term(new Expression("majorid"), new Expression("did"));
      Predicate pred = new Predicate(t); 
      Plan p4 = new SelectPlan(p3, pred);

      // Look at R(p) and B(p) for each plan p.
      printStats(1, p1); printStats(2, p2); printStats(3, p3); printStats(4, p4);

      // Change p3 to be p4 to see the select scan in action.
      Scan s = p3.open();
      while (s.next())
         System.out.println(s.getString("sname") + " " + s.getString("dname"));
      s.close();
   }

   private static void printStats(int n, Plan p) {
      System.out.println("Here are the stats for plan p" + n);
      System.out.println("\tR(p" + n + "): " + p.recordsOutput());
      System.out.println("\tB(p" + n + "): " + p.blocksAccessed());
      System.out.println();	
   }
}
