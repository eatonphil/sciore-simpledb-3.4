package simpledb.plan;

import simpledb.query.Scan;
import simpledb.record.Schema;

/** A Plan class corresponding to the <i>product</i>
  * relational algebra operator that determines the
  * most efficient ordering of its inputs.
  * @author Edward Sciore
  */
public class OptimizedProductPlan implements Plan {
   private Plan bestplan;

   public OptimizedProductPlan(Plan p1, Plan p2) {
   		Plan prod1 = new ProductPlan(p1, p2);
   		Plan prod2 = new ProductPlan(p2, p1);
   		int b1 = prod1.blocksAccessed();
   		int b2 = prod2.blocksAccessed();
   		bestplan = (b1 < b2) ? prod1 : prod2;   		
   }

   public Scan open() {
      return bestplan.open();
   }
   
   public int blocksAccessed() {
      return bestplan.blocksAccessed();
   }

   public int recordsOutput() {
      return bestplan.recordsOutput();
   }

   public int distinctValues(String fldname) {
      return bestplan.distinctValues(fldname);
   }

   public Schema schema() {
      return bestplan.schema();
   }
}
