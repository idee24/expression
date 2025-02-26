package Expression.Operation;

import Exception.VariableNotExistError;
import Expression.OperationExpression;
import Expression.Minimal.NumberExpression;

public class MultiplicationExpression extends OperationExpression {
    public MultiplicationExpression() {
        this.operator = '*';
        this.unit = new NumberExpression(1.0);
    }

    @Override
    public Double evaluate() throws VariableNotExistError {
        return this.left.evaluate() * this.right.evaluate();
    }
}
