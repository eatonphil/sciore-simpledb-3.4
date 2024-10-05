package simpledb.parse;
import java.util.Scanner;

public class PredParserTest {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
      System.out.print("Enter an SQL predicate: ");
		while (sc.hasNext()) {
			String s = sc.nextLine();
			PredParser p = new PredParser(s);
			try {
				p.predicate();
				System.out.println("yes");
			}
			catch (BadSyntaxException ex) {
				System.out.println("no");
			}
         System.out.print("Enter an SQL predicate: ");
		}
		sc.close();
	}
}


