package jlox;

import java.util.ArrayList;

import jlox.Expr.Binary;
import jlox.Expr.Grouping;
import jlox.Expr.Literal;
import jlox.Expr.Unary;
import jlox.Token;
import jlox.Lox;
import static jlox.TokenType.*;


class Parser{
	
	private ArrayList<Token> tokens;
	private int current = 0;
	
	Parser(ArrayList<Token> tokens){
		this.tokens = tokens;
	}
	
	
	public Expr expression() {
		return equality();
	}
	
	public Expr equality() {
		Expr expr = comparison(); // THE LHS
		
		while(match(EQUAL_EQUAL, BANG_EQUAL)) {
			Token op = previous();
			Expr left = expr;
			Expr right = comparison();
			
			expr = new Binary(left,right,op);
		}
		
		return expr;
	}
	
	public Expr comparison() {
		Expr expr = term(); // THE LHS
		
		while(match(EQUAL, GREATER, GREATER_EQUAL, LESS, LESS_EQUAL)) {
			Token op = previous();
			Expr left = expr;
			Expr right = term();
			
			expr = new Binary(left,right,op);
		}
		
		return expr;
	}
	
	public Expr term() {
		
		Expr expr = factor(); // THE LHS
		
		while(match(PLUS, MINUS)) {
			Token op = previous();
			Expr left = expr;
			Expr right = factor();
			
			expr = new Binary(left,right,op);
		}
		
		return expr;
		
	}
	
	public Expr factor() {
		
		Expr expr = unary(); // THE LHS
		
		while(match(STAR, SLASH)) {
			Token op = previous();
			Expr left = expr;
			Expr right = unary();
			
			expr = new Binary(left,right,op);
		}
		
		return expr;
		
	}
	
	public Expr unary() {
		
		if(match(BANG, MINUS)) {
			Token op = previous();
			Expr right = unary();
			
			return new Unary(right,op);
		}
		
		
		return primary();
		
	}
	
	public Expr primary() {
		if(match(NUMBER, STRING)) return new Literal(previous().literal);
		if(match(TRUE)) return new Literal(true);
		if(match(FALSE)) return new Literal(false);
		if(match(NIL)) return new Literal(null);
		
		if(match(LEFT_PAREN)) {
			Expr expr = expression();
			consume(RIGHT_PAREN, "Expected ) after expression");
			return new Grouping(expr);
		}
	}
	
	
	
	
	public Token advance() {
		if(!isAtEnd()) current++;
		return previous();
	}
	
	Token peek() {
		return tokens.get(current);
	}
	
	boolean isAtEnd() {
		return peek().type == EOF;
	}
	
	public boolean check(TokenType type) {
		if(isAtEnd()) return false;
		return peek().type == type;
	}
	
	public Token previous() {
		return tokens.get(current-1);
	}
	
	public boolean match(TokenType...tokenTypes) {
		for(TokenType token : tokenTypes) {
			if(check(token)) {
				advance();
				return true;
			}
		}
		return false;
	}
	
	
	
	
}