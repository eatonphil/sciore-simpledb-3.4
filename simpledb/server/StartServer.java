package simpledb.server;

import java.rmi.registry.*;

import simpledb.jdbc.network.*;

public class StartServer {
   public static void main(String args[]) throws Exception {
      // configure and initialize the database
      String dirname = (args.length == 0) ? "studentdb" : args[0];
      SimpleDB db = new SimpleDB(dirname);
      
      // create a registry specific for the server on the default port
      Registry reg = LocateRegistry.createRegistry(1099);
      
      // and post the server entry in it
      RemoteDriver d = new RemoteDriverImpl(db);
      reg.rebind("simpledb", d);
      
      System.out.println("database server ready");
   }
}
