package jlox;

import java.util.List;

import jlox.Expr.Binary;
import jlox.Expr.Grouping;
import jlox.Expr.Literal;
import jlox.Expr.Unary;
import jlox.Stmt.Expression;
import jlox.Stmt.Print;
import jlox.TokenType;

// this  class is a visitor with return type Object...as it should be ig
// I can make operators do anything I want!
// Mwahaha
// Expressions return an object
// Statement either print the object return (by the underlying expression) or save some change in the program's state. Either way, they don't return anything.
class Interpreter implements Expr.Visitor<Object>, Stmt.Visitor<Void>{

	void interpret(List<Stmt> statements) {
		try {
			for(Stmt s : statements) {
				execute(s);
			}
		}
		catch(RuntimeError error) {
			Lox.runtimeError(error);
		}
	}
	
	private void execute(Stmt s){
		s.accept(this);
	}
	
	
	private String makeString(Object ob) {
		if(ob == null) return "nil";
		
		if(ob instanceof Double) {
			String text = ob.toString();
			if(text.endsWith(".0")) {
				text = text.substring(0,text.length() - 2);
			}
			return text;
		}
		
		return ob.toString();
	}
	
	private void checkNumberOperand(Token op, Object right) {
		if(right instanceof Double) return;
		throw new RuntimeError(op, "Expected a Number type operand.");
	}
	
	private void checkNumberOperands(Token op, Object left, Object right) {
		if(left instanceof Double && right instanceof Double) return;
		throw new RuntimeError(op, "Expected both Number type operands.");
	}
	
	
	@Override
	public Object visitBinaryExpr(Binary expr) {
		Object left = evaluate(expr.left);
		Object right = evaluate(expr.right);
		
		switch(expr.op.type) {
		
		case GREATER:
			checkNumberOperands(expr.op, left, right);
			return (double)left > (double)right;
			
		case GREATER_EQUAL:
			checkNumberOperands(expr.op, left, right);
			return (double)left >= (double)right;
			
		case LESS:
			checkNumberOperands(expr.op, left, right);
			return (double)left < (double)right;
			
		case EQUAL_EQUAL:
			checkNumberOperands(expr.op, left, right);
			return (double)left == (double)right;
		
		case BANG_EQUAL:
			checkNumberOperands(expr.op, left, right);
			return (double)left != (double)right;
		
		case LESS_EQUAL:
			checkNumberOperands(expr.op, left, right);
			return (double)left <= (double)right;
			
		case MINUS:
			checkNumberOperands(expr.op, left, right);
			return (double)left - (double)right;
			
		case SLASH:
			checkNumberOperands(expr.op, left, right);
			return (double)left / (double)right;
			
		case STAR:
			checkNumberOperands(expr.op, left, right);
			return (double)left * (double)right;
		
		case PLUS:
			
			if(left instanceof Double && right instanceof Double) {
				return (double)left + (double)right;
			}
			else if(left instanceof String && right instanceof String) {
				return (String)left + (String)right;
			}
			throw new RuntimeError(expr.op, "Operands must be both Numbers or Strings.");
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
			checkNumberOperand(expr.op, right);
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

	@Override
	public Void visitExpressionStmt(Expression stmt) {
		evaluate(stmt.expr);
		return null;
	}

	@Override
	public Void visitPrintStmt(Print stmt) {
		Object val = evaluate(stmt.expr);
		System.out.println(makeString(val));
		return null;
	}
	
	
}