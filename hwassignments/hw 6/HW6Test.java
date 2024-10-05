package simpledb.record;

import simpledb.server.SimpleDB;
import simpledb.file.BlockId;
import simpledb.tx.Transaction;

public class HW6Test {
	public static void main(String[] args) throws Exception {
		SimpleDB db = new SimpleDB("hw6test", 400, 8);
		Transaction tx = db.newTx();

		Schema sch = new Schema();
		sch.addIntField("A");
		sch.addStringField("B", 9);
		Layout layout = new Layout(sch);
		System.out.println("Slot size = " + layout.slotSize());
		for (String fldname : layout.schema().fields()) {
			int offset = layout.offset(fldname);
			int bitpos = layout.bitPosition(fldname);
			System.out.println(fldname + " has offset " + offset + " and bitpos " + bitpos);
		}

		BlockId blk = tx.append("testfile");
		tx.pin(blk);
		RecordPage rp = new RecordPage(tx, blk, layout);
		rp.format();

		System.out.println("Filling the page with records.");
		int slot = rp.insertAfter(-1);
		while (slot >= 0) {  
			rp.setInt(slot, "A", slot);
			rp.setString(slot, "B", "rec"+slot);
			System.out.println("[" + slot + ", " + "rec"+slot + "]");
			slot = rp.insertAfter(slot);
		}

		slot = rp.nextAfter(-1);
		while (slot >= 0) {
			int a = rp.getInt(slot, "A");
			if (a%2 == 0) 
				rp.setNull(slot, "B");
			slot = rp.nextAfter(slot);
		}
		rp.setNull(1,  "B");
		rp.setString(1, "B", "newrec1"); //B is no longer null
		rp.delete(4);
		slot = rp.insertAfter(-1); 
		System.out.println("new slot is " + slot); // should be 4
		String status = rp.isNull(slot, "B") ? "null" : "not null";
		System.out.println("slot's B value is " + status); // should be not null
		rp.setInt(slot,  "A", 99);
		
		System.out.println("After setting the B-value of even slots to null:");
		slot = rp.nextAfter(-1);
		while (slot >= 0) {
			int a = rp.getInt(slot, "A");
			String b = rp.isNull(slot,  "B") ? "null" : rp.getString(slot, "B");
			System.out.println("[" + a + ", " + b + "]");
			slot = rp.nextAfter(slot);
		}
		
		tx.unpin(blk);
		tx.commit();
	}
}
