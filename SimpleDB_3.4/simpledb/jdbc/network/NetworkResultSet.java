package simpledb.jdbc.network;

import java.sql.*;
import simpledb.jdbc.ResultSetAdapter;

/**
 * An adapter class that wraps RemoteResultSet.
 * Its methods do nothing except transform RemoteExceptions
 * into SQLExceptions.
 * @author Edward Sciore
 */
public class NetworkResultSet extends ResultSetAdapter {
   private RemoteResultSet rrs;
   
   public NetworkResultSet(RemoteResultSet s) {
      rrs = s;
   }
   
   public boolean next() throws SQLException {
      try {
         return rrs.next();
      }
      catch (Exception e) {
         throw new SQLException(e);
      }
   }
   
   public int getInt(String fldname) throws SQLException {
      try {
         return rrs.getInt(fldname);
      }
      catch (Exception e) {
         throw new SQLException(e);
      }
   }
   
   public String getString(String fldname) throws SQLException {
      try {
         return rrs.getString(fldname);
      }
      catch (Exception e) {
         throw new SQLException(e);
      }
   }
   
   public ResultSetMetaData getMetaData() throws SQLException {
      try {
         RemoteMetaData rmd = rrs.getMetaData();
         return new NetworkMetaData(rmd);
      }
      catch (Exception e) {
         throw new SQLException(e);
      }
   }
   
   public void close() throws SQLException {
      try {
         rrs.close();
      }
      catch (Exception e) {
         throw new SQLException(e);
      }
   }
}

