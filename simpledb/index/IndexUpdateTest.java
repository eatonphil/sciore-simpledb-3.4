package simpledb.index;

import java.util.*;
import simpledb.metadata.*;
import simpledb.plan.*;
import simpledb.query.*;
import simpledb.record.RID;
import simpledb.server.SimpleDB;
import simpledb.tx.Transaction;

public class IndexUpdateTest {
   public static void main(String[] args) {
      SimpleDB db = new SimpleDB("studentdb");
      Transaction tx = db.newTx();
      MetadataMgr mdm = db.mdMgr();
      Plan studentplan = new TablePlan(tx, "student", mdm);
      UpdateScan studentscan = (UpdateScan) studentplan.open();

      // Create a map containing all indexes for STUDENT.
      Map<String,Index> indexes = new HashMap<>();
      Map<String,IndexInfo> idxinfo = mdm.getIndexInfo("student", tx);
      for (String fldname : idxinfo.keySet()) {
         Index idx = idxinfo.get(fldname).open();
         indexes.put(fldname, idx);
      }

      // Task 1: insert a new STUDENT record for Sam
      //    First, insert the record into STUDENT.
      studentscan.insert();
      studentscan.setInt("sid", 11);
      studentscan.setString("sname", "sam");
      studentscan.setInt("gradyear", 2023);
      studentscan.setInt("majorid",  30);

      //    Then insert a record into each of the indexes.
      RID datarid = studentscan.getRid();
      for (String fldname : indexes.keySet()) {
         Constant dataval = studentscan.getVal(fldname);
         Index idx = indexes.get(fldname);
         idx.insert(dataval, datarid);
      }

      // Task 2: find and delete Joe's record
      studentscan.beforeFirst();
      while (studentscan.next()) {
         if (studentscan.getString("sname").equals("joe")) {

            // First, delete the index records for Joe.
            RID joeRid = studentscan.getRid();
            for (String fldname : indexes.keySet()) {
               Constant dataval = studentscan.getVal(fldname);
               Index idx = indexes.get(fldname);
               idx.delete(dataval, joeRid);
            }

            // Then delete Joe's record in STUDENT.
            studentscan.delete();
            break;
         }
      }
      
      // Print the records to verify the updates.
      studentscan.beforeFirst();
      while (studentscan.next()) {
         System.out.println(studentscan.getString("sname") + " " + studentscan.getInt("sid"));
      }
      studentscan.close();
      
      for (Index idx : indexes.values())
         idx.close();
      tx.commit();
   }
}
