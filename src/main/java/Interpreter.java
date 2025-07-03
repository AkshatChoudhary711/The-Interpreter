public class Interpreter implements Expr.Visitor<Object>{



    @Override
    public Object visitBinaryExpr(Expr.Binary expr) {
        Object left = evaluate(expr.left);
        Object right = evaluate(expr.right);

        switch(expr.operator.type){
            case MINUS :
                if(checkNumberOperands(expr.operator, left, right)) return null;
                if(left instanceof Double && right instanceof Double) {
                    return (double)left - (double)right;
                }
                break;

            case SLASH:
                if(checkNumberOperands(expr.operator, left, right)) return null;
                if(left instanceof Double && right instanceof Double) {
                    if((double)right == 0) {
                        throw new RuntimeException("Division by zero");
                    }
                    return (double)left / (double)right;
                }
                break;

            case STAR:
                if(checkNumberOperands(expr.operator, left, right)) return null;
                if(left instanceof Double && right instanceof Double) {
                    return (double)left * (double)right;
                }
                break;

            case PLUS:
                if(left instanceof Double && right instanceof Double) {
                    return (double)left + (double)right;
                } else if(left instanceof String && right instanceof String) {
                    return (String)left + (String)right;
                } else{
                    throw new RuntimeError(expr.operator, "Operands must be two numbers or two strings.");
                }


            case GREATER:
                if(checkNumberOperands(expr.operator, left, right)) return null;
                if(left instanceof Double && right instanceof Double) {
                    return (double)left > (double)right;
                }
                break;

            case GREATER_EQUAL:
                if(checkNumberOperands(expr.operator, left, right)) return null;
                if(left instanceof Double && right instanceof Double) {
                    return (double)left >= (double)right;
                }
                break;

            case LESS:
                if(checkNumberOperands(expr.operator, left, right)) return null;
                if(left instanceof Double && right instanceof Double) {
                    return (double)left < (double)right;
                }
                break;

            case LESS_EQUAL:
                if(checkNumberOperands(expr.operator, left, right)) return null;
                if(left instanceof Double && right instanceof Double) {
                    return (double)left <= (double)right;
                }
                break;

            case EQUAL_EQUAL:
                return isEqual(left, right);
            case BANG_EQUAL:
                return !isEqual(left, right);


        }
        return null;
    }

    @Override
    public Object visitUnaryExpr(Expr.Unary expr) {
        Object right = evaluate(expr.right);

        switch (expr.operator.type) {
            case MINUS:
                if(!checkNumberOperand(expr.operator, right)) return null;
                return -(double)right;
            case BANG:
                return !isTruthy(right);
        }
        return null;
    }

    @Override
    public Object visitGroupingExpr(Expr.Grouping expr) {
        return evaluate(expr.expression);
    }

    @Override
    public Object visitLiteralExpr(Expr.Literal expr) {
        return expr.value;
    }

    private Object evaluate(Expr expr) {
        return expr.accept(this);
    }

    private boolean isTruthy(Object obj){
        if(obj == null) return false;
        if(obj instanceof Boolean) return (boolean)obj;
        return true;
    }

    private boolean isEqual(Object a, Object b){
        if(a == null && b == null) return true;
        if(a == null) return false;
        return a.equals(b);
    }

    private boolean checkNumberOperand(Token operator, Object operand) {
        if (operand instanceof Double) {
            return true;
        } else {
            throw new RuntimeError(operator, "Operand must be a number.");
        }
    }

    private boolean checkNumberOperands(Token operator, Object left, Object right) {
        if (left instanceof Double && right instanceof Double) {
            return false;
        } else {
            throw new RuntimeError(operator, "Operands must be numbers.");
        }
    }

    void interpret(Expr expression) {
        try {
            Object value = evaluate(expression);
            System.out.println(stringify(value));
        } catch (RuntimeError error) {
            Lox.runtimeError(error);
        }
    }

    private String stringify(Object object) {
        if (object == null) return "nil";

        if (object instanceof Double) {
            String text = object.toString();
            if (text.endsWith(".0")) {
                text = text.substring(0, text.length() - 2);
            }
            return text;
        }

        return object.toString();
    }



}
