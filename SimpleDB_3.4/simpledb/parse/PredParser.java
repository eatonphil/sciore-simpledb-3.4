package simpledb.parse;

public class PredParser {
   private Lexer lex;

   public PredParser(String s) {
      lex = new Lexer(s);
   }

   public String field() {
      return lex.eatId();
   }

   public void constant() {
      if (lex.matchStringConstant())
         lex.eatStringConstant();
      else
         lex.eatIntConstant();
   }

   public void expression() {
      if (lex.matchId())
         field();
      else 
         constant();
   }

   public void term() {
      expression();
      lex.eatDelim('=');
      expression();
   }

   public void predicate() {
      term();
      if (lex.matchKeyword("and")) {
         lex.eatKeyword("and");
         predicate();
      }
   }
}

