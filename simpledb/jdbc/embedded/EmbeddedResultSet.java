package simpledb.jdbc.embedded;

import java.sql.*;
import simpledb.record.Schema;
import simpledb.query.Scan;
import simpledb.plan.Plan;
import simpledb.jdbc.ResultSetAdapter;

/**
 * The embedded implementation of ResultSet.
 * @author Edward Sciore
 */
public class EmbeddedResultSet extends ResultSetAdapter {
   private Scan s;
   private Schema sch;
   private EmbeddedConnection conn;

   /**
    * Creates a Scan object from the specified plan.
    * @param plan the query plan
    * @param conn the connection
    * @throws RemoteException
    */
   public EmbeddedResultSet(Plan plan, EmbeddedConnection conn) throws SQLException {
      s = plan.open();
      sch = plan.schema();
      this.conn = conn;
   }

   /**
    * Moves to the next record in the result set,
    * by moving to the next record in the saved scan.
    */
   public boolean next() throws SQLException {
      try {
         return s.next();
      }
      catch(RuntimeException e) {
         conn.rollback();
         throw new SQLException(e);
      }
   }

   /**
    * Returns the integer value of the specified field,
    * by returning the corresponding value on the saved scan.
    */
   public int getInt(String fldname) throws SQLException {
      try {
         fldname = fldname.toLowerCase(); // to ensure case-insensitivity
         return s.getInt(fldname);
      }
      catch(RuntimeException e) {
         conn.rollback();
         throw new SQLException(e);
      }
   }

   /**
    * Returns the integer value of the specified field,
    * by returning the corresponding value on the saved scan.
    */
   public String getString(String fldname) throws SQLException {
      try {
         fldname = fldname.toLowerCase(); // to ensure case-insensitivity
         return s.getString(fldname);
      }
      catch(RuntimeException e) {
         conn.rollback();
         throw new SQLException(e);
      }
   }

   /**
    * Returns the result set's metadata,
    * by passing its schema into the EmbeddedMetaData constructor.
    */
   public ResultSetMetaData getMetaData() throws SQLException {
      return new EmbeddedMetaData(sch);
   }

   /**
    * Closes the result set by closing its scan, and commits.
    */
   public void close() throws SQLException {
      s.close();
      conn.commit();
   }
}

