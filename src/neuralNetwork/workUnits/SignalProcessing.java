package neuralNetwork.workUnits;

import tools.Matrix;
import tools.functions.IFunction;

import java.util.ArrayList;

public class SignalProcessing {

    /**
     * Verarbeitet das Signal von der Eingangs- bis zur Ausgangsschicht.
     * @param signal das zu verarbeitende Signal
     * @param layers die Schichten des Netzwerks
     * @param weights die Gewichte zwischen den Schichten
     * @param activationFunction die Aktivierungsfunktion
     */
    public void process(int[] signal, ArrayList<Matrix> layers, ArrayList<Matrix> weights, IFunction activationFunction) {
        initializeInputLayer(signal, layers.getFirst());

        for (int i = 1; i < layers.size(); i++) {
            layers.set(i, weights.get(i - 1).multiplyWith(layers.get(i - 1)));
            layers.get(i).applyFunction(activationFunction);
        }
    }

    /**
     * Initialisiert die Eingangsschicht mit dem zu verarbeitenden Signal.
     * Normalisiert das Ergebnis auf ein Ergebnis zwischen 0 und 1.
     * @param signal das zu verarbeitende Signal
     * @param inputLayer die Eingangsschicht
     */
    public void initializeInputLayer(int[] signal, Matrix inputLayer) {
        final int UNSIGNED_BYTE_MAX_VALUE = 256;

        double[][] inputLayerMatrix = inputLayer.getMatrix();
        for (int i = 0; i < inputLayer.getRowLength(); i++) {
            inputLayerMatrix[i][0] = (double) signal[i] / UNSIGNED_BYTE_MAX_VALUE;
        }
    }
}
