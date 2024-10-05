package simpledb.plan;

import simpledb.server.SimpleDB;
import simpledb.tx.Transaction;
import simpledb.query.Scan;

public class PlannerTest2 {
   public static void main(String[] args) {
      SimpleDB db = new SimpleDB("plannertest2");
      Transaction tx = db.newTx();
      Planner planner = db.planner();
      
      String cmd = "create table T1(A int, B varchar(9))";
      planner.executeUpdate(cmd, tx);
      int n = 200;
      System.out.println("Inserting " + n + " records into T1.");
      for (int i=0; i<n; i++) {
         int a = i;
         String b = "bbb"+a;
         cmd = "insert into T1(A,B) values(" + a + ", '"+ b + "')";
         planner.executeUpdate(cmd, tx);
      }

      cmd = "create table T2(C int, D varchar(9))";
      planner.executeUpdate(cmd, tx);
      System.out.println("Inserting " + n + " records into T2.");
      for (int i=0; i<n; i++) {
         int c = n-i-1;
         String d = "ddd" + c;
         cmd = "insert into T2(C,D) values(" + c + ", '"+ d + "')";
         planner.executeUpdate(cmd, tx);
      }

      String qry = "select B,D from T1,T2 where A=C";
      Plan p = planner.createQueryPlan(qry, tx);
      Scan s = p.open();
      while (s.next())
         System.out.println(s.getString("b") + " " + s.getString("d")); 
      s.close();
      tx.commit();
   }
}
