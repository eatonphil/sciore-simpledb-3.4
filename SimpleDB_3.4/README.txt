                      THE SIMPLEDB DATABASE SYSTEM
                  General Information and Instructions


This document contains the following sections:
    * Release Notes
    * Server Installation
    * Running the Server
    * Running Client Programs
    * SimpleDB Limitations
    * The Organization of the Server Code


I. Release Notes:

  This release of the SimpleDB system is Version 3.4, which was
  uploaded on March 24, 2021.  This release contains fixes a problem with the 
  file MultibufferProductScan.java from Version 3.3.

  SimpleDB is distributed in a WinZip-formatted file. This file contains
  five items:

    * The folder simpledb, which contains the server-side Java code.
    * The folder simpleclient, which contains some client-side code 
      for a SimpleDB database.
    * The folder derbyclient, which contains client-side code 
      For the Derby database, but with added features not supported
      by SimpleDB. These fies are examined in my "Database Design 
      and Implementation" text.
    * The file BookErrata.pdf, which describes how to update the code
      in my revised textbook "Database Design and Implementation" so
      that it conforms to version 3.4.
    * This document.

  The author welcomes all comments, including bug reports, suggestions
  for improvement, and anecdotal experiences.  His email address is 
  sciore@bc.edu
  

II. Installation Instructions:

  1)  Install the Java SDK.

  2) To install the SimpleDB engine, you must add the simpledb folder to 
     your classpath. To do so using Eclipse, first create a new project; 
     call it “SimpleDB Engine”. Then from the operating system, copy the 
     simpledb folder to the src folder of the project. Finally, refresh 
     the project from Eclipse, using the refresh command in the File menu.

  3) The simpleclient folder contains example programs that call the SimpleDB 
     engine. You should create a new project for them; call it “SimpleDB Clients”. 
     To ensure that the example programs can find the SimpleDB engine code, you 
     should add the SimpleDB Engine project to the build path of SimpleDB Clients. 
     Then use the operating system to copy the contents of simpleclient into the 
     src directory of SimpleDB Clients. 

  4) The derbyclient folder contains example programs that call the Derby engine. 
     This code illustrates features of JDBC that SimpleDB does not support, and
     is used in Chapter 2 of my "Database Design and Implementation" text.

III. Running the SimpleDB Server:

  You run the server code on a host machine, where it will sit and wait for 
  connections from clients. It is able to handle multiple simultaneous requests 
  from clients, each on possibly different machines. You can then run a client 
  program from any machine that is able to connect to the host machine.

  To run the SimpleDB server, run Java on the simpledb.server.StartServer class.  
  The argument to the class is the name of a folder that SimpleDB will use to 
  hold the database. If you leave out the argument, it will use "studentdb".
  If a folder with that name does not exist, then one will be created 
  automatically in the current directory.
 
  If everything is working correctly, when you run the server with a
  new database folder the following will be printed in the server's 
  window:

      creating new database
      new transaction: 1
      transaction 1 committed
      database server ready

  If you run the server with an existing database folder, the following
  will be printed instead:

      recovering existing database
      database server ready

  In either case, the server will then sit awaiting connections from
  clients.  As connections arrive, the server will print additional
  messages in its window.

  The server is implemented using RMI, on port 1099. If a registry is 
  running when the server is started, it will use that registry; 
  otherwise, it will create and run the registry itself.


IV. Running Client Code:

  SimpleDB clients can be run in embedded mode or network mode. To run a 
  client in embedded mode, use the EmbeddedDriver JDBC class with the 
  connection string "jdbc:simpledb:xyz", where xyz is the name of the database. 
  The database will be created if it does not exist, in the current directory 
  of the client program. No server is necessary

  To run a client in network mode, use the NetworkDriver class with the
  connection string "jdbc:simpledb://xyz", where xyz is the name or IP address
  of the machine running the SimpleDB server. Note that you cannot specify a
  database, because the client must use the database bound to the server.

  SimpleDB does not require a username and password, although
  it is easy enough to modify the server code to do so.

  The following list briefly describes the provided SimpleDB clients.

    * CreateStudentDB creates and populates the student database used
      by the other clients. It therefore must be the first client run 
      on a new database. 
    * StudentMajors prints a table listing the names of students and 
      their majors.
    * FindMajors asks the user for the name of a department. It then
      prints the name and graduation year of students having that major.
    * SimpleIJ repeatedly prints a prompt asking you to enter a 
      single line of text containing an SQL statement. The program then 
      executes that statement.  If the statement is a query, the output 
      table is displayed.  If the statement is an update command, then
      the number of affected records is printed. If the statement is ill
      formed, and error message will be printed. SimpleDB understands 
      only a limited subset of SQL, which is described below.
    * ChangeMajor changes the student named Amy to be a drama major.  
      It is the only client that updates the database (although you can 
      use SQLInterpreter to run update commands).

  These clients connect to the server at "localhost".  If the client is  
  to be run from a different machine than the server, then its source code 
  must be modified so that localhost is replaced by the domain name (or IP 
  address) of the server machine. 
  


V. SimpleDB Limitations

  SimpleDB is a teaching tool. It deliberately implements a tiny subset
  of SQL and JDBC, and (for simplicity) imposes restrictions not present
  in the SQL standard.  Here we briefly indicate these restrictions.


  SimpleDB SQL
  
  A query in SimpleDB consists only of select-from-where clauses in which
  the select clause contains a list of fieldnames (without the AS 
  keyword), and the from clause contains a list of tablenames (without
  range variables).
 
  The where clause is optional.  The only Boolean operator is and.  The
  only comparison operator is equality.  Unlike standard SQL, there are
  no other comparison operators, no other Boolean operators, no arithmetic
  operators or built-in functions, and no parentheses.  Consequently,
  nested queries, aggregation, and computed values are not supported.

  Views can be created, but a view definition can be at most 100 
  characters.
 
  Because there are no range variables and no renaming, all field names in
  a query must be disjoint.  And because there are no group by or order by
  clauses, grouping and sorting are not supported.  Other restrictions:

    * The "*" abbreviation in the select clause is not supported.
    * There are no null values.
    * There are no explicit joins or outer joins in the from clause.
    * The union and except keywords are not supported.
    * Insert statements take explicit values only, not queries.
    * Update statements can have only one assignment in the set clause.


  SimpleDB JDBC
  
  SimpleDB implements only the following JDBC methods:

   Driver

      public Connection connect(String url, Properties prop);
      // The method ignores the contents of variable prop.

   Connection

      public Statement createStatement();
      public void      close();

   Statement

      public ResultSet executeQuery(String qry);
      public int       executeUpdate(String cmd);

   ResultSet

      public boolean   next();
      public int       getInt();
      public String    getString();
      public void      close();
      public ResultSetMetaData getMetaData();

   ResultSetMetaData

      public int        getColumnCount();
      public String     getColumnName(int column);
      public int        getColumnType(int column);
      public int getColumnDisplaySize(int column);


VII. The Organization of the Server Code

  SimpleDB is usable without knowing anything about what the code looks
  like. However, the entire point of the system is to make the code
  easy to read and modify.  The basic packages in SimpleDB are structured
  hierarchically, in the following order:

    * file (Manages OS files as a virtual disk.)
    * log (Manages the log.)
    * buffer (Manages a buffer pool of pages in memory that acts as a
              cache of disk blocks.)
    * tx (Implements transactions at the page level.  Does locking
          and logging.)
    * record (Implements fixed-length records inside of pages.)
    * metadata (Maintains metadata in the system catalog.)
    * query (Implements relational algebra operations. Each operation 
             has a scan class, which can be composed to create a query tree.)
    * parse (Implements the parser.)
    * plan (Implements a naive planner for SQL statements.)
    * jdbc (Implements embedded and network interfaces for JDBC.)
    * server (The place where the startup and initialization code live. 
              The class Startup contains the main method.)

  The basic server is exceptionally inefficient. The following packages
  enable more efficient query processing:

    * index (Implements static hash and btree indexes, as well as 
             extensions to the parser and planner to take advantage
             of them.)
    * materialize (Implements implementations of the relational 
                   operators materialize, sort, groupby, and mergejoin.)
    * multibuffer (Implements modifications to the sort and product 
                   operators, in order to make optimum use of available
                   buffers.)
    * opt (Implements a heuristic query optimizer)
 
  My textbook "Database Design and Implementation", recently revised 
  and published by Springer, describes these packages in considerably 
  more detail. 
   