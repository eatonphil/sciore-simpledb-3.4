package simpledb.file;

import java.io.*;
import simpledb.server.SimpleDB;

public class HW2Test {
	public static void main(String[] args) throws IOException {
		SimpleDB db = new SimpleDB("hw2test", 400, 8);
		FileMgr fm = db.fileMgr();
		BlockId blk = new BlockId("testfile", 2);
		Page p = new Page(fm.blockSize());
		
		int value = 1234;
		int pos0 = fm.blockSize() - Integer.BYTES;
		try {
			p.setInt(pos0+1, value);
		}
		catch (Exception e) {
			System.out.println("*** You didn't test for invalid integer");			
		}
		
		String s = "abcdefghijklm";		
		byte[] b = s.getBytes();
		int bytesize  = b.length;
		int pos1 = fm.blockSize() - bytesize;
		try {
			p.setBytes(pos1+1, b);
		}
		catch (Exception e) {
			System.out.println("*** You didn't test for invalid byte array");			
		}
		
		int stringsize = Page.maxLength(s.length());
		if (stringsize != 2*(s.length() + 1))
			System.out.println("*** Incorrect maxLength");
		int pos2 = fm.blockSize() - stringsize;
		try {
			p.setString(pos2+1, s);
		}
		catch (Exception e) {
			System.out.println("*** You didn't test for invalid string");			
		}
		
		int pos3 = 4+stringsize;
		p.setInt(0, 1234);
		p.setString(4, s);  
		p.setInt(pos3, 5678);
		fm.write(blk, p);

		Page p2 = new Page(fm.blockSize());
		fm.read(blk, p2);
		System.out.println("offset 0 contains " + p2.getInt(0));
		System.out.println("offset 4 contains " + p2.getString(4));
		System.out.println("offset " + pos3 + " contains " + p2.getInt(pos3));
	}
}