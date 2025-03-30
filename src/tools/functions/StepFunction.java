package tools.functions;

public class StepFunction implements IFunction {
    @Override
    public double function(double x) {
        return x < 0 ? 0 : 1;
    }

    @Override
    public double derivative(double x) {
        return 0;
    }
}
