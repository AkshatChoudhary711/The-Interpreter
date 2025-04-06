abstract public class Expr {

    interface Visitor<T>{
        T visitBinaryExpr(Expr.Binary expr);
        T visitUnaryExpr(Expr.Unary expr);
        T visitGroupingExpr(Expr.Grouping expr);
        T visitLiteralExpr(Expr.Literal expr);
    }
    abstract <R> R accept(Visitor<R> visitor);
    //Binary
    static class Binary extends Expr {
        Binary(Expr left, Token operator, Expr right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitBinaryExpr(this);
        }

        final Expr left;
        final Token operator;
        final Expr right;
    }
    //Unary
    static class Unary extends Expr{
        Unary(Token operator, Expr right){
            this.operator = operator;
            this.right = right;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitUnaryExpr(this);
        }
        final Token operator;
        final Expr right;
    }
    //Grouping
    static class Grouping extends Expr{
        Grouping(Expr expression){
            this.expression=expression;
        }
        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitGroupingExpr(this);
        }
        final Expr expression;
    }
    //Literal
    static class Literal extends Expr{
        Literal(Object value){
            this.value = value;
        }
        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitLiteralExpr(this);
        }
        final Object value;
    }
}
