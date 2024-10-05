package simpledb.jdbc.network;

import simpledb.plan.Plan;
import simpledb.query.*;
import simpledb.record.Schema;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * The RMI server-side implementation of RemoteResultSet.
 * @author Edward Sciore
 */
@SuppressWarnings("serial")
class RemoteResultSetImpl extends UnicastRemoteObject implements RemoteResultSet {
   private Scan s;
   private Schema sch;
   private RemoteConnectionImpl rconn;

   /**
    * Creates a RemoteResultSet object.
    * The specified plan is opened, and the scan is saved.
    * @param plan the query plan
    * @param rconn TODO
    * @throws RemoteException
    */
   public RemoteResultSetImpl(Plan plan, RemoteConnectionImpl rconn) throws RemoteException {
      s = plan.open();
      sch = plan.schema();
      this.rconn = rconn;
   }

   /**
    * Moves to the next record in the result set,
    * by moving to the next record in the saved scan.
    * @see simpledb.jdbc.network.RemoteResultSet#next()
    */
   public boolean next() throws RemoteException {
		try {
	      return s.next();
      }
      catch(RuntimeException e) {
         rconn.rollback();
         throw e;
      }
   }

   /**
    * Returns the integer value of the specified field,
    * by returning the corresponding value on the saved scan.
    * @see simpledb.jdbc.network.RemoteResultSet#getInt(java.lang.String)
    */
   public int getInt(String fldname) throws RemoteException {
		try {
	      fldname = fldname.toLowerCase(); // to ensure case-insensitivity
	      return s.getInt(fldname);
      }
      catch(RuntimeException e) {
         rconn.rollback();
         throw e;
      }
   }

   /**
    * Returns the integer value of the specified field,
    * by returning the corresponding value on the saved scan.
    * @see simpledb.jdbc.network.RemoteResultSet#getInt(java.lang.String)
    */
   public String getString(String fldname) throws RemoteException {
		try {
	      fldname = fldname.toLowerCase(); // to ensure case-insensitivity
	      return s.getString(fldname);
      }
      catch(RuntimeException e) {
         rconn.rollback();
         throw e;
      }
   }

   /**
    * Returns the result set's metadata,
    * by passing its schema into the RemoteMetaData constructor.
    * @see simpledb.jdbc.network.RemoteResultSet#getMetaData()
    */
   public RemoteMetaData getMetaData() throws RemoteException {
      return new RemoteMetaDataImpl(sch);
   }

   /**
    * Closes the result set by closing its scan.
    * @see simpledb.jdbc.network.RemoteResultSet#close()
    */
   public void close() throws RemoteException {
      s.close();
      rconn.commit();
   }
}

