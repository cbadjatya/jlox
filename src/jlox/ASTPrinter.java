package jlox;

import jlox.Expr.Binary;
import jlox.Expr.Grouping;
import jlox.Expr.Literal;
import jlox.Expr.Unary;

class ASTPrinter implements Expr.Visitor<String>{
	
	String print(Expr expr) {
		return expr.accept(this);
	}
	
	@Override
	public String visitBinaryExpr(Binary expr) {
		return parenthesize(expr.op.lexeme, expr.left, expr.right);
	}

	@Override
	public String visitUnaryExpr(Unary expr) {
		// TODO Auto-generated method stub
		return parenthesize(expr.op.lexeme, expr.right);
	}

	@Override
	public String visitGroupingExpr(Grouping expr) {
		// TODO Auto-generated method stub
		return parenthesize("group",expr.expr);
	}

	@Override
	public String visitLiteralExpr(Literal expr) {
		if(expr.value == null) return "nil";
		return expr.value.toString();
	}
	
	public String parenthesize(String name, Expr...exprs) {
		String s = "";
		s += " ( ";
		s += name + " ";
		for(Expr ex : exprs) {
			s += ex.accept(this) +" ";
		}
		s += " ) ";
		return s;
	}
	
//	public static void main(String args[]) {
//		Expr expression = new Expr.Binary(
//		new Expr.Unary(
//		new Expr.Literal(123),
//		new Token(TokenType.MINUS, "-", null, 1)),
//		new Expr.Grouping(
//		new Expr.Literal(45.67)),
//		new Token(TokenType.STAR, "*", null, 1));
//		
//		System.out.println(new ASTPrinter().print(expression));
//	}
}



