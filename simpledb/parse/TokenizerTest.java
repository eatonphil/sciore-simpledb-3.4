package simpledb.parse;

import java.io.*;
import java.util.*;
import static java.io.StreamTokenizer.*;

public class TokenizerTest {
   private static Collection<String> keywords = Arrays.asList("select", "from", "where", "and",
         "insert", "into", "values", "delete", "update", "set", 
         "create", "table", "int", "varchar", "view", "as", "index", "on");

   public static void main(String[] args) throws IOException {      
      String s = getStringFromUser();
      StreamTokenizer tok = new StreamTokenizer(new StringReader(s));
      tok.ordinaryChar('.');
      tok.lowerCaseMode(true); //ids and keywords are converted to lower case
      while (tok.nextToken() != TT_EOF)
         printCurrentToken(tok);
   }

   private static String getStringFromUser() {
      System.out.println("Enter tokens:");
      Scanner sc = new Scanner(System.in);
      String s = sc.nextLine();
      sc.close();
      return s;
   }

   private static void printCurrentToken(StreamTokenizer tok) throws IOException {
      if (tok.ttype == TT_NUMBER) 
         System.out.println("IntConstant " + (int)tok.nval);
      else if (tok.ttype == TT_WORD) {
         String word = tok.sval;
         if (keywords.contains(word))
            System.out.println("Keyword " + word);
         else
            System.out.println("Id " + word);
      }
      else if (tok.ttype == '\'')
         System.out.println("StringConstant " + tok.sval);
      else
         System.out.println("Delimiter " + (char)tok.ttype);
   }
}
