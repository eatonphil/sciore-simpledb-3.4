package simpledb.jdbc.embedded;

import java.sql.SQLException;
import simpledb.tx.Transaction;
import simpledb.plan.*;
import simpledb.jdbc.StatementAdapter;

/**
 * The embedded implementation of Statement.
 * @author Edward Sciore
 */
class EmbeddedStatement extends StatementAdapter {
   private EmbeddedConnection conn;
   private Planner planner;
   
   public EmbeddedStatement(EmbeddedConnection conn, Planner planner) {
      this.conn = conn;
      this.planner = planner;
   }
   
   /**
    * Executes the specified SQL query string.
    * Calls the query planner to create a plan for the query, 
    * and sends the plan to the ResultSet constructor for processing.
    * Rolls back and throws an SQLException if it cannot create the plan.
    */
   public EmbeddedResultSet executeQuery(String qry) throws SQLException {
      try {
         Transaction tx = conn.getTransaction();
         Plan pln = planner.createQueryPlan(qry, tx);
         return new EmbeddedResultSet(pln, conn);
      }
      catch(RuntimeException e) {
         conn.rollback();
         throw new SQLException(e);
      }
   }
   
   /**
    * Executes the specified SQL update command by sending
    * the command to the update planner and then committing.
    * Rolls back and throws an SQLException on an error.
    */
   public int executeUpdate(String cmd) throws SQLException {
      try {
         Transaction tx = conn.getTransaction();
         int result = planner.executeUpdate(cmd, tx);
         conn.commit();
         return result;
      }
      catch(RuntimeException e) {
         conn.rollback();
         throw new SQLException(e);
      }
   }
   
   public void close() throws SQLException {
   }
}
