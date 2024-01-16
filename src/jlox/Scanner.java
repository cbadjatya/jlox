package jlox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static jlox.TokenType.*;

class Scanner {
	
	
	private final String source;
	private final List<Token> tokens = new ArrayList<>();
	
	private int start = 0;
	private int current = 0;
	private int line = 1;
	
	
	private static final Map<String, TokenType> keywords;
	
	static {
		keywords = new HashMap<>();
		keywords.put("and", AND);
		keywords.put("class", CLASS);
		keywords.put("else", ELSE);
		keywords.put("false", FALSE);
		keywords.put("for", FOR);
		keywords.put("fun", FUN);
		keywords.put("if", IF);
		keywords.put("nil", NIL);
		keywords.put("or", OR);
		keywords.put("print", PRINT);
		keywords.put("return", RETURN);
		keywords.put("super", SUPER);
		keywords.put("this", THIS);
		keywords.put("true", TRUE);
		keywords.put("var", VAR);
		keywords.put("while", WHILE);
		
	}

	
	Scanner(String source){
		this.source = source;
	}
	
	List<Token> scanTokens(){
		
		while(!isAtEnd()) {
			start = current;
			scanToken();
		}
		
		tokens.add(new Token(EOF, "", null, line));
		return tokens;
		
	}
	
	private void scanToken() {
		char c = advance();
		
		switch(c) {
		// SINGLE CHARACTER LEXEMES
			case '(': addToken(LEFT_PAREN); break;
			case ')': addToken(RIGHT_PAREN); break;
			case '{': addToken(LEFT_BRACE); break;
			case '}': addToken(RIGHT_BRACE); break;
			case ',': addToken(COMMA); break;
			case '.': addToken(DOT); break;
			case '-': addToken(MINUS); break;
			case '+': addToken(PLUS); break;
			case ';': addToken(SEMICOLON); break;
			case '*': addToken(STAR); break;
			
		// SINGLE-DOUBLE  CHARACTER  LEXEMES
			case '!':
				addToken(match('=') ? BANG_EQUAL : BANG);
				break;
			
			case '>':
				addToken(match('=') ? GREATER_EQUAL : GREATER);
				break;
				
			case '<':
				addToken(match('=') ? LESS_EQUAL : LESS);
				break;
				
			case '=':
				addToken(match('=') ? EQUAL_EQUAL : EQUAL);
				break;
				
			case '/': // SPECIAL CASE
				if(match('/')) {
					while (peek() != '\n') advance();
				}
				else {
					addToken(SLASH);
				}
				break;
			
			// IGNORE WHITESPACE
			
			case ' ':
			case '\r':
			case '\t':
				break;
			
			case '\n':
				line++;
				break;
				
			//STRING LITERAL
			case '"':
				string(); break;
				
			//NUM LITERAL
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
				number();
				break;
				
			//IDENTIFIERS - adding in default
			
			
			default:
				
				if(isAlpha(c)) {
					
					identifier();
					break;
 				}
				
				Lox.error(line, "Unexpected Character " + c);
				break;
		}
	}
	
	private void identifier() {
		while(isAlphaNumeric(peek())) advance();
		
		
		String lexeme = source.substring(start,current);
		TokenType type = keywords.get(lexeme);
		
		if(type == null) type = IDENTIFIER;
		addToken(type);
	}
	
	private boolean isAlpha(char peek) {
		
		if(peek >= 'a' && peek <= 'z') return true;
		if(peek>='A' && peek<='Z') return true;
		if(peek == '_') return true;
	
 		return false;
	}

	private boolean isAlphaNumeric(char peek) {
		if(isAlpha(peek) || (peek >= '0' && peek <= '9')) return true;
		return false;
	}
	
	private void number() {
		while(!isAtEnd() && isDigit(peek())) {
			advance();
		}
		if(peek() == '.'){
			advance();
			if(!isDigit(peek())) {
				current--; // should make a function rollback() for this. Don't like messing with current willy-nilly.
			}
			else {
				while(!isAtEnd() && isDigit(peek())) {
					advance();
				}
			}
		}
		
		addToken(NUMBER, 
				Double.parseDouble(source.substring(start, current)));
	}
	
	private boolean isDigit(char peek) {
		if(peek >= '0' && peek <= '9') return true;
		return false;
	}

	private void string() {
		while(!isAtEnd() && peek() != '"') {
			if(peek() == '\n') line++;
			advance();
		}
		if(isAtEnd()) {
			Lox.error(line, "Reached EOF. Unterminated  String.");
			return;
		}
		advance();
		
		String val = source.substring(start+1,current-1);
		addToken(STRING, val);
	}
	
	private boolean match(char check) {
		
		if(isAtEnd()) return false;
		
		char nxt = advance();
		if(nxt == check) {
			return true;
		}
		
		current--;
		return false;
	}
	
	private char peek() {
		if(isAtEnd()) return '\0';
		return source.charAt(current);
		
	}
	
	// addToken is an overloaded function.
	
	private void addToken(TokenType type) {
		addToken(type, null);
	}
	
	private void addToken(TokenType type, Object literal) {
		String source = this.source.substring(start,current);
		Token token = new Token(type, source, literal, line);
		tokens.add(token);
	}
	
	char advance() {
		return source.charAt(current++);
	}
	
	boolean isAtEnd() {
		return current >= source.length();
	}
	
}