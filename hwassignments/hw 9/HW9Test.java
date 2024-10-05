package simpledb.query;

import simpledb.server.SimpleDB;
import simpledb.tx.Transaction;
import java.util.*;
import simpledb.metadata.MetadataMgr;
import simpledb.parse.*;
import simpledb.record.*;

public class HW9Test {
	public static void main(String[] args) {
		SimpleDB db = new SimpleDB("hw9test");
		String tblname = "t";
		createTable(db, tblname);
		testConstant();
		testTerm();
		testParser();
		testGetVal(db, tblname);
		testQuery(db, tblname);
	}

	private static void createTable(SimpleDB db, String tblname) {
		MetadataMgr mdm = db.mdMgr();
		Transaction tx = db.newTx();
		Layout layout = mdm.getLayout(tblname, tx);
		if (layout.slotSize() > 0) {
			tx.commit();
			return;  // Use existing table
		}      
		// Create a table T(A int, B varchar(15));
		Schema sch = new Schema();
		sch.addIntField("a");
		sch.addStringField("b", 15);
		mdm.createTable(tblname, sch, tx);
		layout = mdm.getLayout(tblname, tx);
		// and populate it
		TableScan ts = new TableScan(tx, tblname, layout);
		for (int i=1; i<5; i++) {
			ts.insert();
			ts.setInt("a", i);
			ts.setString("b", "x"+i);
			if (i%2 == 0)
				ts.setNull("b");
		}
		ts.close();
		tx.commit();
		System.out.println("table created\n");
	}

	private static void testConstant() {
		System.out.println("Testing constants:");
		Constant[] cs = new Constant[3];
		cs[0] = new Constant(2);
		cs[1] = new Constant("x");
		cs[2] = new Constant();
		for (Constant c : cs) {
			String status = c.isNull() ? " " : " not ";
			System.out.println("\t" + c + " is" + status + "null"); 
		}
		System.out.println();
	}

	private static void testTerm() {
		System.out.println("Comparing some terms:");
		Expression e0 = new Expression(new Constant());
		Expression e1 = new Expression(new Constant(1));
		Expression e2 = new Expression(new Constant(2));

		Term[] terms = new Term[5];
		terms[0] = new Term(e1, Term.LT, e2);
		terms[1] = new Term(e1, Term.GT, e2);
		terms[2] = new Term(e1, Term.GT, e0);
		terms[3] = new Term(e1, Term.IS, e0);
		terms[4] = new Term(e0, Term.IS, e0);

		for (Term t : terms)
			System.out.println("\t" + t + "\t" + t.isSatisfied(null));
		System.out.println();
	} 

	private static void testParser() {
		System.out.println("Testing the parser changes:");
		String q1 = "select a,b from s where b is null and a < 2";
		Parser p = new Parser(q1);
		QueryData qd = p.query();
		System.out.println("Should print \"b IS NULL and a<2\":");
		System.out.println("\t"+ qd.pred() + "\n");
	}

	private static void testGetVal(SimpleDB db, String tblname) {
		MetadataMgr mdm = db.mdMgr();
		Transaction tx = db.newTx();
		Layout layout = mdm.getLayout(tblname, tx);
		TableScan ts = new TableScan(tx, tblname, layout);
		System.out.println("Testing the getVal method");
		System.out.println("Here is the entire table");
		while (ts.next()) {
			Constant c1 = ts.getVal("a");
			Constant c2 = ts.getVal("b");
			System.out.println("\ta=" + c1 + "\tb=" + c2);
		}
		ts.close();
		tx.commit();
		System.out.println();
	}

	private static void testQuery(SimpleDB db, String tblname) {
		Transaction tx = db.newTx();
		MetadataMgr mdm = db.mdMgr();
		String qry = "select a, b from " + tblname +
				     " where b is null and a<3";
		System.out.println("Executing: " + qry);
		Parser parser = new Parser(qry);
		QueryData qd = parser.query();
		Scan s = createScan(qd, tx, mdm);
		
		while (s.next()) {
			Constant c1 = s.getVal("a");
			Constant c2 = s.getVal("b");
			System.out.println("\ta=" + c1 + "\tb=" + c2);
		}		
		s.close();
		tx.commit();
	}

	private static Scan createScan(QueryData data, Transaction tx, 
			                       MetadataMgr mdm) {
		List<Scan> scans = new ArrayList<>();
		for (String tblname : data.tables()) {
			Layout layout = mdm.getLayout(tblname, tx);
			scans.add(new TableScan(tx, tblname, layout));
		}
		Scan s = scans.remove(0);
		for (Scan nextscan : scans)
			s = new ProductScan(s, nextscan);	      
		s = new SelectScan(s, data.pred());	      
		return new ProjectScan(s, data.fields());
	}
	
	
	
	
	
}
