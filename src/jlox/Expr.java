package jlox;

abstract class Expr{

	interface Visitor<R> {
		R visitBinaryExpr(Binary expr);
		R visitUnaryExpr(Unary expr);
		R visitGroupingExpr(Grouping expr);
		R visitLiteralExpr(Literal expr);
	}


	abstract <R> R accept(Visitor<R> visitor);

	static class Binary extends Expr{

		final Expr left;
		final Expr right;
		final Token op;

		Binary( Expr left, Expr right, Token op ){

			this.left = left;
			this.right = right;
			this.op = op;

		}

		@Override
		<R> R accept(Visitor<R> visitor){
			return visitor.visitBinaryExpr(this);
		}
	}

	static class Unary extends Expr{

		final Expr right;
		final Token op;

		Unary( Expr right, Token op ){

			this.right = right;
			this.op = op;

		}

		@Override
		<R> R accept(Visitor<R> visitor){
			return visitor.visitUnaryExpr(this);
		}
	}

	static class Grouping extends Expr{

		final Expr expr;

		Grouping( Expr expr ){

			this.expr = expr;

		}

		@Override
		<R> R accept(Visitor<R> visitor){
			return visitor.visitGroupingExpr(this);
		}
	}

	static class Literal extends Expr{

		final Object value;

		Literal( Object value ){

			this.value = value;

		}

		@Override
		<R> R accept(Visitor<R> visitor){
			return visitor.visitLiteralExpr(this);
		}
	}

}

