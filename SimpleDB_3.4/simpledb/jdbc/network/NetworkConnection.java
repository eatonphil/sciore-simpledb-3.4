package simpledb.jdbc.network;

import java.sql.*;
import simpledb.jdbc.ConnectionAdapter;

/**
 * An adapter class that wraps RemoteConnection.
 * Its methods do nothing except transform RemoteExceptions
 * into SQLExceptions.
 * @author Edward Sciore
 */
public class NetworkConnection extends ConnectionAdapter {
   private RemoteConnection rconn;
   
   public NetworkConnection(RemoteConnection c) {
      rconn = c;
   }
   
   public Statement createStatement() throws SQLException {
      try {
         RemoteStatement rstmt = rconn.createStatement();
         return new NetworkStatement(rstmt);
      }
      catch(Exception e) {
         throw new SQLException(e);
      }
   }
   
   public void close() throws SQLException {
      try {
         rconn.close();
      }
      catch(Exception e) {
         throw new SQLException(e);
      }
   }
}

