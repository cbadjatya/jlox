package jlox;

import jlox.Expr.Binary;
import jlox.Expr.Grouping;
import jlox.Expr.Literal;
import jlox.Expr.Unary;
import jlox.TokenType;

// this  class is a visitor with return type Object...as it should be ig
// I can make operators do anything I want!
// Mwahaha
class Interpreter implements Expr.Visitor<Object>{

	@Override
	public Object visitBinaryExpr(Binary expr) {
		Object left = evaluate(expr.left);
		Object right = evaluate(expr.right);
		
		switch(expr.op.type) {
		case MINUS:
			return (double)left - (double)right;
			
		case SLASH:
			return (double)left / (double)right;
			
		case STAR:
			return (double)left * (double)right;
		
		case PLUS:
			if(left instanceof Double && right instanceof Double) {
				return (double)left + (double)right;
			}
			else if(left instanceof String && right instanceof String) {
				return (String)left + (String)right;
			}
			break;
		}
		// Error or unreachable
		return null;
	}

	/*
	 * Get the second subexpr.
	 * Get the first subexpr.
	 * if a negation sign, return the number
	 *
	 *WHAT is a truth in our language? is a string true?  is 0 true?
	 *
	 */
	@Override
	public Object visitUnaryExpr(Unary expr) {
		Object right = evaluate(expr.right);
		Token op = expr.op;
		
		switch(op.type) {
		case MINUS:
			return -(double)right;
		case BANG:
			return !isTruthy(right);
		}
		
		// Unreachable
		return null;
	}
	
	//defining the truth in our own little universe
	// false and nil are false
	// all else is true...yes even 0
	private boolean isTruthy(Object ob) {
		if(ob == null) return false;
		if(ob instanceof Boolean) return (boolean)ob;
		
		return false;
	}

	/*
	 * simply evaluate the expression inside the parentheses.
	 */
	@Override
	public Object visitGroupingExpr(Grouping expr) {
		
		return evaluate(expr.expr);
		
	}
	
	/*
	 * Whichever expression type is to be evaluated, call function for "interpreter" visitor there.
	 */
	private Object evaluate(Expr expr) {
		return expr.accept(this);
	}

	@Override
	public Object visitLiteralExpr(Literal expr) {
		
		return expr.value;
		
	}
	
	
}