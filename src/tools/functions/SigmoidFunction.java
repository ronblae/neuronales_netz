package tools.functions;

public class SigmoidFunction implements IFunction {
    @Override
    public double function(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    @Override
    public double derivative(double x) {
        return function(x) * (1 - function(x));
    }
}
