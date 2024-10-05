package simpledb.parse;
import java.util.Scanner;

public class ParserTest {
   public static void main(String[] args) {
      Scanner sc = new Scanner(System.in);
      System.out.print("Enter an SQL statement: ");
      while (sc.hasNext()) {
         String s = sc.nextLine();
         Parser p = new Parser(s);
         try {
            if (s.startsWith("select"))
               p.query();
            else
               p.updateCmd();
            System.out.println("yes");
         }
         catch (BadSyntaxException ex) {
            System.out.println("no");
         }
         System.out.print("Enter an SQL statement: ");
      }
      sc.close();
   }
}


