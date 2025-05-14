//1-alterar o reconhecimento de tokens de java para a linguagem alvo
//2-incrementar o reconhecimento de tokens que nao está presentes
package com.Auxiliares;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.Erro.LexerException;

public class AnalisadorLexico {

  // Atributos
	
  private BufferedReader reader;
  private int estado;
  private int buffer;
  private boolean bufferValid = false;


  public AnalisadorLexico(BufferedReader reader) {
		
	this.reader = reader;
	
  }
	
  public Token pegarProximoToken() throws IOException, LexerException {
			
	boolean feito = false;
	int tipoAtual = Token.EOF;
	Object valorAtual = null;
	StringBuffer sBuffer = null;

	estado = 0;
	
	while ( !feito ) {
		
	  int caractere = pegardoBuffer();
	  
	  if ( Character.isWhitespace(caractere) && tipoAtual == Token.EOF ) {
		
	    continue;
		
	  } else if ( caractere == -1 && tipoAtual == Token.EOF ) {
					
	    return Token.EOF(); 
		
	  }
		
	  switch (estado) {
				
		case 0: {
				
		  switch( caractere ) {
		      
		    case '<': {
		    		
			  estado = 1;
			  tipoAtual = Token.RELOP;
			  valorAtual = Integer.valueOf( Token.LT );//less than
			
		    }break;
		    
		    case '>': {
		        
		      estado = 10;
		  	  tipoAtual = Token.RELOP;
		  	  valorAtual = Integer.valueOf( Token.GT );
		  	
		    }break;

		    case '=': {

			  estado = 4;
		      tipoAtual = Token.AT;
		      valorAtual = Integer.valueOf( Token.ATR );
		      
		    }break;
		    
		    case '!': {

		      estado = 7;
		      tipoAtual = Token.LOG;
		      valorAtual = Integer.valueOf( Token.NOT );
		      
		    }break;
		    
		    case '+': {
		    	
		      tipoAtual = Token.OP;
		      valorAtual = Integer.valueOf( Token.AD );
		      
		      feito = true;
		      
		    }break;

		    case '-': {
				estado = 51;

		    }break;

		    case '*': {
			  
		      tipoAtual = Token.OP;
			  valorAtual = Integer.valueOf( Token.MUL );
			 
			  feito = true;
			  
		    }break;

		    case '/': {
			  
		      estado = 24;
			  
		    }break;

		    case '&': {
		    	
		      estado = 28;
		      
		    }break;

		    case '|': {
		    	
		      estado = 32;
		      
		    }break;

		    case ',': {
		    	
			  tipoAtual = Token.PONTUACAO;
			  valorAtual = Integer.valueOf( Token.VG );
			  
			  feito = true;
			  
		    }break;

		    case ';': {
		    	
			  tipoAtual = Token.PONTUACAO;
			  valorAtual = Integer.valueOf( Token.PV );
			
			  feito = true;
			
		    }break;
		    
		    case '(': {
		    	
			  tipoAtual = Token.PONTUACAO;
			  valorAtual = Integer.valueOf( Token.AP );
			
			  feito = true;

		    }break;
		    
		    case ')': {
		    	
			  tipoAtual = Token.PONTUACAO;
			  valorAtual = Integer.valueOf( Token.FP );
			
			  feito = true;
			  
		    }break;

		    case '{': {
		    	
		    	
			  tipoAtual = Token.PONTUACAO;
			  valorAtual = Integer.valueOf( Token.AC );
			
			  feito = true;
				
		    }break;
		    
		    case '}': {
		    
		      tipoAtual = Token.PONTUACAO;
			  valorAtual = Integer.valueOf( Token.FC );
			
			  feito = true;
			
		    }break;
			  
		    default:{
		    
		      retoneparaBuffer( caractere );
		      estado = getProximoEstado();
		    
		    }
		    
		  }    
		  
		}break;

		case 1: {
					
		  if ( caractere == '=' ) {
						
		    valorAtual = Integer.valueOf( Token.LE );
			feito = true;
			
		  } else {
						
		    retoneparaBuffer( caractere );
			feito = true;
			
		  }
		  
		}break;
		    
		case 4: {
					
		  if ( caractere == '=' ) {
		
		    tipoAtual = Token.RELOP;
		    valorAtual = Integer.valueOf( Token.EQ );
			
		    feito = true;//identifiquei um token, nao precisa continuar no while
			
		  } else {
			  
		    retoneparaBuffer( caractere );
		    feito = true;
			
		  } 
		  
		}break;
		 
		case 7: {
			      
		  if ( caractere == '=' ) {
						
		    tipoAtual = Token.RELOP;  
			valorAtual = Integer.valueOf( Token.NE );
			feito = true;
			
		  } else {
				
		    retoneparaBuffer( caractere ); 
		    feito = true;
			
		  } 
		
		}break;
		
		case 10: {
			      
		  if ( caractere == '=' ) {
							
			valorAtual = Integer.valueOf( Token.GE );
			feito = true;
			
		  } else {
								
			retoneparaBuffer( caractere );
			feito = true;			
		  } 
			
		}break;
		
		case 24: {
		  	
		  if( caractere == '/' ) {
		    			
			while( caractere != '\n' && caractere != -1 ) {
				 
			  caractere = pegardoBuffer();

			}
						
			estado = 0;
			
		  } else if( caractere == '*' ) {
			  
			caractere = pegardoBuffer();
			 
		    while( caractere != '*' && caractere != -1 ) {
	        	 
			  caractere = pegardoBuffer();
			  
		    }
		    		
		    caractere = pegardoBuffer();
		    
		    if( caractere != '/' ) {
		    	
		      estado = 24;  
		      
		    } else {

		      estado = 0;
	  	
		    }
		    
		  } else {
			  
            tipoAtual = Token.OP;
            valorAtual = Integer.valueOf( Token.DIV );
            
			retoneparaBuffer( caractere );
			feito = true;
			
		  }
	
		}break;

		case 28: {
			
		  if( caractere == '&' ) {
			  
		    tipoAtual = Token.LOG;
		    valorAtual = Integer.valueOf( Token.AND );
		    
		    feito = true;
		    
		  } else {
			  
		    retoneparaBuffer( caractere );
		    estado = getProximoEstado();
		    
		  }
		  
		}break;
		
		case 29: {
			
		  if( caractere == '*' && pegardoBuffer() == '/' ) {
			  
		    valorAtual = sBuffer;
		    feito = true;
		    
		  } else {
			  
		    estado = 29;
		    
		    sBuffer.append( (char)caractere );
		    
		  }
		  
		}break;
		
		case 32: {
			
		  if( caractere == '|' ) {
			   
		    tipoAtual = Token.LOG;
			valorAtual = Integer.valueOf( Token.OR );
			
		    feito = true;

		  } else {
			
		    retoneparaBuffer( caractere );
		    estado = getProximoEstado();
		 
		  }
	
		}break;
		
		case 35: {
	    	
		  if ( Simbolos.isLetra( caractere ) || caractere == '_' ) {			

		    estado = 36;
			
		    tipoAtual = Token.ID;			
		    
			sBuffer = new StringBuffer();
			sBuffer.append( (char) caractere );
			
		  } else {
						
		    retoneparaBuffer( caractere );
			estado = getProximoEstado();
			
		  }

		}break;
				
		case 36: {
			
		  if ( Character.isLetterOrDigit( caractere ) || caractere == '_' ) {

			estado = 36;
			
			sBuffer.append( (char) caractere );
			
		  } else {
						
			retoneparaBuffer( caractere );					
			
			if ( PalavrasChave.isPalavraChave( sBuffer) ) {
							    
			  tipoAtual = PalavrasChave.tipoPalavraChave( sBuffer );
			  valorAtual = null;
			
			} else {
							
			  valorAtual = sBuffer.toString();
			  
			}
			
			feito = true;
			
		  }

		}break;
		
		case 38: {
			
		  if ( Character.isDigit( caractere ) ) {
			    	
			estado = 39;
			
			tipoAtual = Token.LITERALNUMERICO;
			
			sBuffer = new StringBuffer();
			sBuffer.append( (char) caractere );
			
		  } else {
						
			retoneparaBuffer( caractere );
			estado = getProximoEstado();
			
		  }

		}break;

		  case 39: {

			  if ( Character.isDigit( caractere ) ) {
				  sBuffer.append((char) caractere);
				  estado = 39;

			  } else if (caractere == '.') {
				  sBuffer.append((char) caractere);
				  estado = 45;

			  } else {
				  retoneparaBuffer(caractere);
				  valorAtual = Integer.valueOf(sBuffer.toString());
				  tipoAtual = Token.LITERALNUMERICO;
				  feito = true;
			  }

		  } break;


		  case 41: {
			
		  if( caractere == '\'' ) {
			 
		    estado = 42;
		    
		    tipoAtual = Token.LITERALCHAR;
		    
		    sBuffer = new StringBuffer();
		    	
		  } else {
			  
		    retoneparaBuffer( caractere );
		    estado = getProximoEstado();
		    
		  }
		  
		}break;
		
		case 42: {
			
		  if( Simbolos.isLetraouDigito( caractere ) ) {
			  
		    estado = 44;
		    
		    sBuffer.append( (char) caractere );
		    
		  } else if ( caractere == '\\') {
				
		    estado = 43;
			
		    sBuffer.append( (char) caractere );
  
		  } else {
		
			retoneparaBuffer( caractere );
			 estado = getProximoEstado();
		  }
		  
		}break;
		
		case 43: {
			
		  if( caractere == 'r' || caractere == 'n' || caractere == 't' || caractere == '\\' ) {
			  
		    estado = 44;
		    
		    sBuffer.append( (char) caractere );
		    
		  } else {
			  
		    retoneparaBuffer( caractere );
		    estado = getProximoEstado();
		    
		  }
		  
		}break;
		
		case 44: {
			
		  if( caractere == '\'' ) {
			  
		    valorAtual = sBuffer;
		  
		    feito = true;
		    
		  } else {
			  
		    retoneparaBuffer( caractere ); 
		    estado = getProximoEstado();
		    
		  }
		  
		}break;

		  case 45: {

			  if (Character.isDigit(caractere)) {
				  sBuffer.append((char) caractere);
				  estado = 45;

			  } else {
				  retoneparaBuffer(caractere);
				  valorAtual = Float.valueOf(sBuffer.toString());
				  tipoAtual = Token.LITERALFLOAT;
				  feito = true;
			  }

		  } break;


		  case 46: {
			
	      if( caractere == '"' ) {
	    	
			estado = 47;
			
			tipoAtual = Token.LITERALSTRING;
			
			sBuffer = new StringBuffer();
			
	      } else {
					  
			retoneparaBuffer( caractere );
			
			estado = getProximoEstado();
			
	      }

		}break;
		
		case 47: {
			 
		  if( Simbolos.isLetraouDigito( caractere ) || caractere == ' ' ) {

			estado = 47;
			
			sBuffer.append( (char) caractere );
			
		  } else if( caractere == '\\' ){

			estado = 48;

			sBuffer.append( (char) caractere );
		    
		  } else {
				
		    retoneparaBuffer( caractere );
		    estado = 49;
	    
		  }
 
		}break;
		
		case 48: {

		  if( caractere == '\\' || caractere == 'r' || caractere == 't' || caractere == 'n' ) {

		    estado = 47;
		    
		    sBuffer.append( (char) caractere );
		    
		  } else {
			  
		    estado = getProximoEstado();
		    
		  }
		  
		}break;

		case 49: {
			
		  if( caractere == '"' ) {
			  
		    valorAtual = sBuffer;
		    feito = true;
		    
		  } else {
			
			retoneparaBuffer( caractere );
		    estado = getProximoEstado();
		    
		  }
		  
		}break;

		default: {
						
		  throw new Error("Estado nao esperado!!!");
			
		}
		
	  }
		
	}
	//System.out.println(estado);
	return new Token( tipoAtual, valorAtual );
	
  }
	
  private int pegardoBuffer() throws IOException {
			
	int result;
	
	if ( bufferValid ) {
	  
	  result = buffer;
	  bufferValid = false;
	
	} else {
				
	  result = reader.read();
	  bufferValid = false;
	
	}
	
	return result;
	
  }
	
  private void retoneparaBuffer(int c) {
			
	if ( bufferValid ) {
		
	  throw new Error( "Buffer cheio!!" );
		
	}
	
	buffer = c;
	bufferValid = true;
	
  }
	
  private int getProximoEstado() throws LexerException {
			
	int resultado = 0;
	
	switch (estado) {
			
	  case 0: resultado = 35; break;
		
	  case 35: resultado = 38; break; 
		
	  case 38: resultado = 41; break; 
 
	  case 41: resultado = 46; break;
	  
	  default: throw new LexerException( "Erro Lexico: Impossível reconhecer token " );
		
	}
	
	return resultado;
	
  }

}
