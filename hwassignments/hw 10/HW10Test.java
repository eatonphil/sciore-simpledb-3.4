package simpledb.plan;

import simpledb.metadata.MetadataMgr;
import simpledb.parse.*;
import simpledb.query.*;
import simpledb.record.*;
import simpledb.server.SimpleDB;
import simpledb.tx.Transaction;

public class HW10Test {
	public static void main(String[] args) {
		SimpleDB db = new SimpleDB("hw10testdb");
		String tblname = "t";
		Transaction tx = db.newTx();
		MetadataMgr mdm = db.mdMgr();
		
		boolean b = createTable(mdm, tblname, tx);
		if (b)
			populateTable(mdm, tblname, tx);
		tx.commit();
		tx = db.newTx();
		Planner plnr = db.planner();
		query1(plnr, tblname, tx);
		query2(plnr, tblname, tx);
		tx.commit();
	}

	private static boolean createTable(MetadataMgr mdm, String tblname, Transaction tx) {
		Layout layout = mdm.getLayout(tblname, tx);
		if (layout.slotSize() > 0) 
			return false;  // Use existing table
		else {   
			String stmt1 = "create table " + tblname + " (A int, B varchar(15), C int)";
			Parser p = new Parser(stmt1);
			CreateTableData data = (CreateTableData) p.updateCmd();
			BasicUpdatePlanner up = new BasicUpdatePlanner(mdm);
			up.executeCreateTable(data, tx);
			return true;
		}
	}

	// Create 500 records having A-values from 0 to 499,
	// B-values = A mod 100, and C-values that are either 999 or 123.
	private static void populateTable(MetadataMgr mdm, String tblname, Transaction tx) {
		for (int i=0; i<500; i++) {
			String stmt = makeInsertStmt(tblname, i);
			Parser p = new Parser(stmt);
			InsertData data = (InsertData) p.updateCmd();
			BasicUpdatePlanner up = new BasicUpdatePlanner(mdm);
			up.executeInsert(data, tx);
		}
	}

	private static String makeInsertStmt(String t, int i) {
		String stmt = "insert into " + t + "(A, B, C) values (";
		String cval = (i%2 == 0) ? "999" : "123";
		stmt += i + ", 'b" + (i%100) + "', " + cval + ")";
		return stmt;
	}

	private static void query1(Planner plnr, String t, Transaction tx) {
		System.out.println("Here are the records where B=b1");
		String qry = "select A as X, B, C as Y from " + t + " where B= 'b1' ";
		Plan plan = plnr.createQueryPlan(qry, tx);
		Scan s = plan.open();
		while (s.next()) 
			System.out.println(s.getInt("x") + "\t" + s.getString("b") + "\t" + s.getInt("y"));
		s.close();
	}
	private static void query2(Planner plnr, String t, Transaction tx) {
		System.out.println("Here are the records where B=b1 or B=b5");
		String qry = "select A, B, C from " + t + " where B= 'b1' "
				+ "union select A, B, C from " + t + " where B= 'b5' ";
		Plan plan = plnr.createQueryPlan(qry, tx);
		Scan s = plan.open();
		while (s.next()) 
			System.out.println(s.getInt("a") + "\t" + s.getString("b") + "\t" + s.getInt("c"));
		s.close();
	}
}
