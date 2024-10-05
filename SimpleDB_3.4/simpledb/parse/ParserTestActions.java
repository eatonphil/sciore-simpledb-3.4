package simpledb.parse;
import java.util.Scanner;

public class ParserTestActions {
   public static void main(String[] args) {
      Scanner sc = new Scanner(System.in);
      System.out.print("Enter an SQL statement: ");
      while (sc.hasNext()) {
         String s = sc.nextLine();
         Parser p = new Parser(s);
         try {
            String result;
            if (s.startsWith("select")) 
               result = p.query().toString();
            else
               result = p.updateCmd().getClass().toString();
            System.out.println("Your statement is: " + result);
         }
         catch (BadSyntaxException ex) {
            System.out.println("Your statement is illegal");
         }
         System.out.print("Enter an SQL statement: ");
      }
      sc.close();
   }
}


