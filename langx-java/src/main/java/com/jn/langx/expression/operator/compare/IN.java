package com.jn.langx.expression.operator.compare;

import com.jn.langx.expression.Expression;
import com.jn.langx.expression.operator.AbstractBinaryOperator;
import com.jn.langx.expression.value.BooleanExpression;
import com.jn.langx.expression.BooleanResultExpression;
import com.jn.langx.util.comparator.Compares;

/**
 * in
 * @param <E>
 */
public class IN<E> extends AbstractBinaryOperator<Expression<E>, Expression<E>, BooleanResultExpression> implements CompareOperator<Expression<E>, Expression<E>> {
    @Override
    public BooleanResultExpression execute() {
        BooleanExpression expression = new BooleanExpression();
        expression.setValue(Compares.in(getLeft().execute(), getRight().execute()));
        return expression;
    }
}