package neuralNetwork.workUnits;

import tools.Matrix;

public class Evaluation {
    private int counter = 0;

    /**
     * Überprüft das Ergebnis nach der Signalverarbeitung und zählt die Treffer mit.
     * @param results das Ergebnis nach der Signalverarbeitung
     * @param expectedResult das erwartete Ergebnis in Matrixform
     */
    public void checkResult(Matrix results, Matrix expectedResult) {
        int expected = -1;
        int result = -1;
        double placeholder = 0;

        for (int i = 0; i < expectedResult.getMatrix().length; i++) {
            if (expectedResult.getMatrix()[i][0] == 1) {
                expected = i;
            }
            if (results.getMatrix()[i][0] > placeholder) {
                placeholder = results.getMatrix()[i][0];
                result = i;
            }
        }

        if (result == expected) {
            counter++;
        }
    }

    /**
     * Gibt die Trefferrate des Netzwerks aus.
     * @param threadNumber die Nummer des Netzwerkes
     * @param numberOfItems die Anzahl der Daten
     */
    public void printAccuracy(int threadNumber, int numberOfItems) {
        System.out.println("Trefferrate Thread " + threadNumber + " = " + (double) counter / numberOfItems);
    }

    /**
     * Setzt den Zähler zurück, wenn es mehrere Durchläufe gibt.
     */
    public void resetCounter() {
        counter = 0;
    }
}
