package com.jn.langx.expression.operator;


import com.jn.langx.expression.BaseExpression;
import com.jn.langx.expression.Expression;

public abstract class AbstractUnaryOperator<E extends Expression<R>, R> extends BaseExpression<R> implements UnaryOperator<E, R> {
    private E target;
    protected String operateSymbol;

    @Override
    public void setOperateSymbol(String operateSymbol) {
        this.operateSymbol = operateSymbol;
    }

    @Override
    public String getOperateSymbol() {
        return this.operateSymbol;
    }

    @Override
    public void setTarget(E target) {
        this.target = target;
    }

    @Override
    public E getTarget() {
        return this.target;
    }

    @Override
    public String toString() {
        return operateSymbol + " " + target.toString();
    }
}
