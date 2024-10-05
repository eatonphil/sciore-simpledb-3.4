package simpledb.index.btree;

import static java.sql.Types.INTEGER;
import simpledb.file.BlockId;
import simpledb.tx.Transaction;
import simpledb.record.*;
import simpledb.index.Index;
import simpledb.query.Constant;

/**
 * A B-tree implementation of the Index interface.
 * @author Edward Sciore
 */
public class BTreeIndex implements Index {
   private Transaction tx;
   private Layout dirLayout, leafLayout;
   private String leaftbl;
   private BTreeLeaf leaf = null;
   private BlockId rootblk;

   /**
    * Opens a B-tree index for the specified index.
    * The method determines the appropriate files
    * for the leaf and directory records,
    * creating them if they did not exist.
    * @param idxname the name of the index
    * @param leafsch the schema of the leaf index records
    * @param tx the calling transaction
    */
   public BTreeIndex(Transaction tx, String idxname, Layout leafLayout) {
      this.tx = tx;
      // deal with the leaves
      leaftbl = idxname + "leaf";
      this.leafLayout = leafLayout;
      if (tx.size(leaftbl) == 0) {
         BlockId blk = tx.append(leaftbl);
         BTPage node = new BTPage(tx, blk, leafLayout);
         node.format(blk, -1);
      }

      // deal with the directory
      Schema dirsch = new Schema();
      dirsch.add("block",   leafLayout.schema());
      dirsch.add("dataval", leafLayout.schema());
      String dirtbl = idxname + "dir";
      dirLayout = new Layout(dirsch);
      rootblk = new BlockId(dirtbl, 0);
      if (tx.size(dirtbl) == 0) {
         // create new root block
         tx.append(dirtbl);
         BTPage node = new BTPage(tx, rootblk, dirLayout);
         node.format(rootblk, 0);
         // insert initial directory entry
         int fldtype = dirsch.type("dataval");
         Constant minval = (fldtype == INTEGER) ?
               new Constant(Integer.MIN_VALUE) :
               new Constant("");
         node.insertDir(0, minval, 0);
         node.close();
      }
   }

   /**
    * Traverse the directory to find the leaf block corresponding
    * to the specified search key.
    * The method then opens a page for that leaf block, and
    * positions the page before the first record (if any)
    * having that search key.
    * The leaf page is kept open, for use by the methods next
    * and getDataRid.
    * @see simpledb.index.Index#beforeFirst(simpledb.query.Constant)
    */
   public void beforeFirst(Constant searchkey) {
      close();
      BTreeDir root = new BTreeDir(tx, rootblk, dirLayout);
      int blknum = root.search(searchkey);
      root.close();
      BlockId leafblk = new BlockId(leaftbl, blknum);
      leaf = new BTreeLeaf(tx, leafblk, leafLayout, searchkey);
   }

   /**
    * Move to the next leaf record having the
    * previously-specified search key.
    * Returns false if there are no more such leaf records.
    * @see simpledb.index.Index#next()
    */
   public boolean next() {
      return leaf.next();
   }

   /**
    * Return the dataRID value from the current leaf record.
    * @see simpledb.index.Index#getDataRid()
    */
   public RID getDataRid() {
      return leaf.getDataRid();
   }

   /**
    * Insert the specified record into the index.
    * The method first traverses the directory to find
    * the appropriate leaf page; then it inserts
    * the record into the leaf.
    * If the insertion causes the leaf to split, then
    * the method calls insert on the root,
    * passing it the directory entry of the new leaf page.
    * If the root node splits, then makeNewRoot is called.
    * @see simpledb.index.Index#insert(simpledb.query.Constant, simpledb.record.RID)
    */
   public void insert(Constant dataval, RID datarid) {
      beforeFirst(dataval);
      DirEntry e = leaf.insert(datarid);
      leaf.close();
      if (e == null)
         return;
      BTreeDir root = new BTreeDir(tx, rootblk, dirLayout);
      DirEntry e2 = root.insert(e);
      if (e2 != null)
         root.makeNewRoot(e2);
      root.close();
   }

   /**
    * Delete the specified index record.
    * The method first traverses the directory to find
    * the leaf page containing that record; then it
    * deletes the record from the page.
    * @see simpledb.index.Index#delete(simpledb.query.Constant, simpledb.record.RID)
    */
   public void delete(Constant dataval, RID datarid) {
      beforeFirst(dataval);
      leaf.delete(datarid);
      leaf.close();
   }

   /**
    * Close the index by closing its open leaf page,
    * if necessary.
    * @see simpledb.index.Index#close()
    */
   public void close() {
      if (leaf != null)
         leaf.close();
   }

   /**
    * Estimate the number of block accesses
    * required to find all index records having
    * a particular search key.
    * @param numblocks the number of blocks in the B-tree directory
    * @param rpb the number of index entries per block
    * @return the estimated traversal cost
    */
   public static int searchCost(int numblocks, int rpb) {
      return 1 + (int)(Math.log(numblocks) / Math.log(rpb));
   }
}
