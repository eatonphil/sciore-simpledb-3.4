package simpledb.parse;
import java.util.Scanner;

// Will successfully read in lines of text denoting an
// SQL expression of the form "id = c" or "c = id".

public class LexerTest {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		while (sc.hasNext()) {
			String s = sc.nextLine();
			Lexer lex = new Lexer(s);
			String x; int y;
			if (lex.matchId()) {
				x = lex.eatId();
				lex.eatDelim('=');
				y = lex.eatIntConstant();
			}
			else {
				y = lex.eatIntConstant();
				lex.eatDelim('=');
				x = lex.eatId();	
			}
			System.out.println(x + " equals " + y);
		}
		sc.close();
	}
}
