package tools.functions;

import static java.lang.Math.exp;
import static java.lang.Math.pow;

public class GaussianFunction implements IFunction {
    @Override
    public double function(double x) {
        return exp(-pow(x, 2));
    }

    @Override
    public double derivative(double x) {
        return -2 * x * exp(pow(-x, 2));
    }
}
