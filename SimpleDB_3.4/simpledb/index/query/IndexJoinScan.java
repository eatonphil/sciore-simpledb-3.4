package simpledb.index.query;

import simpledb.index.Index;
import simpledb.record.TableScan;
import simpledb.query.*;

/**
 * The scan class corresponding to the indexjoin relational
 * algebra operator.
 * The code is very similar to that of ProductScan, 
 * which makes sense because an index join is essentially
 * the product of each LHS record with the matching RHS index records.
 * @author Edward Sciore
 */
public class IndexJoinScan implements Scan {
   private Scan lhs;
   private Index idx;
   private String joinfield;
   private TableScan rhs;  
   
   /**
    * Creates an index join scan for the specified LHS scan and 
    * RHS index.
    * @param lhs the LHS scan
    * @param idx the RHS index
    * @param joinfield the LHS field used for joining
    * @param rhs the RHS scan
    */
   public IndexJoinScan(Scan lhs, Index idx, String joinfield, TableScan rhs) {
      this.lhs = lhs;
      this.idx  = idx;
      this.joinfield = joinfield;
      this.rhs = rhs;
      beforeFirst();
   }
   
   /**
    * Positions the scan before the first record.
    * That is, the LHS scan will be positioned at its
    * first record, and the index will be positioned
    * before the first record for the join value.
    * @see simpledb.query.Scan#beforeFirst()
    */
   public void beforeFirst() {
      lhs.beforeFirst();
      lhs.next();
      resetIndex();
   }
   
   /**
    * Moves the scan to the next record.
    * The method moves to the next index record, if possible.
    * Otherwise, it moves to the next LHS record and the
    * first index record.
    * If there are no more LHS records, the method returns false.
    * @see simpledb.query.Scan#next()
    */
   public boolean next() {
      while (true) {
         if (idx.next()) {
            rhs.moveToRid(idx.getDataRid());
            return true;
         }
         if (!lhs.next())
            return false;
         resetIndex();
      }
   }
   
   /**
    * Returns the integer value of the specified field.
    * @see simpledb.query.Scan#getVal(java.lang.String)
    */
   public int getInt(String fldname) {
      if (rhs.hasField(fldname))
         return rhs.getInt(fldname);
      else  
         return lhs.getInt(fldname);
   }
   
   /**
    * Returns the Constant value of the specified field.
    * @see simpledb.query.Scan#getVal(java.lang.String)
    */
   public Constant getVal(String fldname) {
      if (rhs.hasField(fldname))
         return rhs.getVal(fldname);
      else
         return lhs.getVal(fldname);
   }
   
   /**
    * Returns the string value of the specified field.
    * @see simpledb.query.Scan#getVal(java.lang.String)
    */
   public String getString(String fldname) {
      if (rhs.hasField(fldname))
         return rhs.getString(fldname);
      else
         return lhs.getString(fldname);
   }
   
   /** Returns true if the field is in the schema.
     * @see simpledb.query.Scan#hasField(java.lang.String)
     */
   public boolean hasField(String fldname) {
      return rhs.hasField(fldname) || lhs.hasField(fldname);
   }
   
   /**
    * Closes the scan by closing its LHS scan and its RHS index.
    * @see simpledb.query.Scan#close()
    */
   public void close() {
      lhs.close();
      idx.close();
      rhs.close();
   }

   private void resetIndex() {
      Constant searchkey = lhs.getVal(joinfield);
      idx.beforeFirst(searchkey);
   }
}
