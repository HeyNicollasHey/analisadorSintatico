
package com.Auxiliares;

import java.io.IOException;
import com.Erro.LexerException;

public class Parser {

  // Atributos

  private AnalisadorLexico analisadorLexico;
  private Token token;
  private String mensagemErro = "";


  private String tipoExpr = "";

  public Parser(AnalisadorLexico analisadorLexico) {
	
    this.analisadorLexico = analisadorLexico;
	
  }

  public boolean parse() throws IOException, LexerException {
	
    boolean resultado;

    token = analisadorLexico.pegarProximoToken();
    
    //símbolo/variável inicial da gramática que ele vai produzir a regra
    resultado = SE();

    if ( resultado && token.getTipo() != Token.EOF ) {
	
      System.err.println( "\n Fim de arquivo esperado, token = " + token );
		
      resultado = false;
		
    }

    if ( !resultado ) {
	
      mensagemErro = "Token não esperado: " + token;
    
    }
    
    return resultado;
	
  }

  public String errorMessage() {
	
    return mensagemErro;
	
  }

  private boolean SE() throws IOException, LexerException {

    boolean resultado;

    System.out.print("SE[ if ");

    if( match(Token.IF)){

      if( match( Token.PONTUACAO, Token.AP ) ) {

        resultado =  LE() && match( Token.PONTUACAO, Token.FP );

        if( match(Token.PONTUACAO, Token.AC) ){
          resultado = comandos() && match( Token.PONTUACAO, Token.FC );
        }

      }else{
        resultado = false;
      }

    }else{
      resultado = false;
    }

    System.out.print(" ]se");

    return resultado;

  }


  private boolean CHAVES() throws IOException, LexerException {

    boolean resultado;

    System.out.print("Chaves[ { ");

    if( match(Token.PONTUACAO, Token.AC) ){

      resultado = comandos() && match( Token.PONTUACAO, Token.FC );

    }else if (comando()){

      resultado = true;

    }else{

      resultado = false;

    }


    System.out.print(" ]CHAVES");

    return resultado;

  }

  private boolean comando() throws IOException, LexerException {

    boolean resultado;

    System.out.print("comando[ { ");

    resultado = match(Token.ID) && match(Token.AT) && LE();

    System.out.print(" ]CHAVES");

    return resultado;

  }

  private boolean comandos() throws IOException, LexerException {
    return true;
  }

    private boolean LE() throws IOException, LexerException {
	  
    boolean resultado;
	
    System.out.print( "LE[ " );
    
    if( !RE() ) {
    
      return false; 
    
    }
    
    if( match( Token.LOG, Token.AND ) || match( Token.LOG, Token.OR ) ) {
    
      resultado = LE();
      
    } else {
    	
      System.out.print( " €" ); //está sozinho!
      
      resultado = true;
      
    }
    
    System.out.print( " ]le" );
    
    return resultado;
		
  }

  private boolean RE() throws IOException, LexerException {
	    
    boolean resultado;
	
    System.out.print( "RE[ " );
    
    if( !AE() ) {
    	
      return false;
      
    }
    
    if( match( Token.RELOP, Token.GT ) || match( Token.RELOP, Token.GE ) || match( Token.RELOP, Token.LT ) 
     || match( Token.RELOP, Token.LE ) || match( Token.RELOP , Token.EQ ) ) {
    	
      resultado = RE();
        
    } else {
    	
      System.out.print( " €" );
    	 
      resultado = true;
      
    }
    
    System.out.print( " ]re" );
    
    return resultado;
		
  }

  private boolean AE() throws IOException, LexerException {
    boolean resultado;
    String tipoEsquerdo;

    System.out.print("AE[ ");

    if( !ME() ) return false;

    tipoEsquerdo = tipoExpr;

    if( match(Token.OP, Token.AD) || match(Token.OP, Token.SUB) ) {
      if( !AE() ) return false;

      if( !tipoEsquerdo.equals("int") || !tipoExpr.equals("int") ) {
        mensagemErro = "Erro semântico: operação aritmética com tipos incompatíveis";
        return false;
      }

      tipoExpr = "int";
    }

    System.out.print(" ]ae");
    return true;
  }


  private boolean ME() throws IOException, LexerException {
    boolean resultado;
    String tipoEsquerdo;

    System.out.print("ME[ ");

    if (!UE()) return false;

    tipoEsquerdo = tipoExpr; // armazena tipo do primeiro operando

    if (match(Token.OP, Token.MUL) || match(Token.OP, Token.DIV)) {
      if (!ME()) return false;

      // Verificação semântica: ambos operandos precisam ser inteiros
      if (!tipoEsquerdo.equals("int") || !tipoExpr.equals("int")) {
        mensagemErro = "Erro semântico: operação de multiplicação/divisão com tipos incompatíveis";
        return false;
      }

      tipoExpr = "int"; // resultado de uma operação aritmética é int
    }

    System.out.print(" ]me");
    return true;
  }


  private boolean UE() throws IOException, LexerException {
    boolean resultado = false;
    String tipoOperando = "";

    System.out.print("UE[ ");

    if (match(Token.PONTUACAO, Token.AP)) {
      resultado = LE(); // processa a expressão entre parênteses
      tipoOperando = tipoExpr;
      resultado &= match(Token.PONTUACAO, Token.FP);
    } else if (token.getTipo() == Token.LITERALNUMERICO) {
      tipoOperando = "int";
      resultado = match(Token.LITERALNUMERICO);
    } else if (token.getTipo() == Token.TRUE || token.getTipo() == Token.FALSE) {
      tipoOperando = "bool";
      resultado = match(token.getTipo());
    } else if (match(Token.ID)) {
      tipoOperando = "int"; // ou recuperar da tabela de símbolos, se existisse
      resultado = true;
    } else if (match(Token.OP, Token.AD) || match(Token.OP, Token.SUB) || match(Token.LOG, Token.NOT)) {
      resultado = UE();
      tipoOperando = tipoExpr;
    }

    tipoExpr = tipoOperando;

    System.out.print(" ]ue ");
    return resultado;
  }


  private boolean match(int tipoToken) throws IOException, LexerException {

    boolean resultado;
    
    if ( token.getTipo() == tipoToken ) {
           
      if( tipoToken == Token.ID ) {
    	  
        System.out.print( "Id" );
    	  
      }
      
      if( tipoToken == Token.LITERALNUMERICO ) {
    	  
        System.out.print( token.getValor() );
    	  
      }

      token = analisadorLexico.pegarProximoToken();
      
      resultado = true;
		
    }else {
	    	
      resultado = false;
    
    }
    
    return resultado;
	
  }
  
  private boolean match(int tipoToken, int valorToken) throws IOException, LexerException {
		
    boolean resultado;
	    
    if ( token.getTipo() == tipoToken  && (Integer)token.getValor() == valorToken )  {
	
      switch( tipoToken ) {
      
        case Token.LOG: {
      
          if( valorToken == Token.AND ) {
        	
            System.out.print( " && " );
          
          } else if( valorToken == Token.NOT){

            System.out.print( " ! " );

          }else {
        	
            System.out.print( " || " );
            
          }
      
        }break;
        
        case Token.OP : {
      
          switch (valorToken) {
	
            case Token.AD: System.out.print( "+ " );	
		         break;
        
            case Token.SUB: System.out.print( "-" );	
		        break;

            case Token.DIV: System.out.print( "/" );	
		         break;

            case Token.MUL: System.out.print( "*" );	
		         break;

          }
          
        }break;
        
        case Token.PONTUACAO: {
    
          if( valorToken == Token.AP ) {
        	  
            System.out.print( "( " );  
          
          } else if( valorToken == Token.FP) {
        	
            System.out.print( ") " ); 
          
          }
          
        }break;

        //operador relacional
        case Token.RELOP : {
            
          switch (valorToken) {
  	
            case Token.GT: System.out.print( " > " );	
  		          break;
          
            case Token.GE: System.out.print( " >= " );	
  		         break;

            case Token.LT: System.out.print( " < " );	
  		          break;

            case Token.LE: System.out.print( " <= " );	
  		          break;
  		          
            case Token.EQ: System.out.print( " == " );	
	          break;
      

          }
          
        }break;

      }
      
      token = analisadorLexico.pegarProximoToken();
      
	  resultado = true;
		
    } else {
	  
      resultado = false;
    
    }
	
    return resultado;
	
  }

}
