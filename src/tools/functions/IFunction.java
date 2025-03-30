package tools.functions;

public interface IFunction {
    /**
     * Definiert die Funktion.
     * @param x die Variable
     * @return der Funktionswert
     */
    double function(double x);

    /**
     * Definiert die Ableitung der Funktion
     * @param x die Variable
     * @return der Funktionswert
     */
    double derivative(double x);
}
