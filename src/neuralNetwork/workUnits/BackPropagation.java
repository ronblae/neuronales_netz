package neuralNetwork.workUnits;

import tools.Matrix;

import java.util.ArrayList;


public class BackPropagation {
    private final double LEARNING_RATE;

    public BackPropagation(double learningRate) {
        LEARNING_RATE = learningRate;
    }

    /**
     * Leitet den Lernprozess nach der Verarbeitung eines Signals ein.
     * @param expectedResult das erwartete Ergebnis
     * @param layers die Schichten des Netzwerks
     * @param weightMatrices die Gewichte zwischen den Schichten.
     */
    public void propagate(Matrix expectedResult, ArrayList<Matrix> layers, ArrayList<Matrix> weightMatrices) {
        ArrayList<Matrix> errorMatrices = createErrorMatrix(layers, expectedResult, weightMatrices);

        for (int i = 0; i < weightMatrices.size(); i++) {
            Matrix errorMatrixOfNextLayer = errorMatrices.get(i + 1);
            Matrix outputMatrixOfNextLayer = layers.get(i + 1);
            Matrix outputMatrixOfPreviousLayer = layers.get(i);

            Matrix helperMatrix = getHelperMatrix(errorMatrixOfNextLayer, outputMatrixOfNextLayer);
            Matrix weightDifferences = helperMatrix.multiplyWith(outputMatrixOfPreviousLayer.transpose());

            weightDifferences.applyScalar(LEARNING_RATE);
            weightMatrices.set(i, weightMatrices.get(i).addMatrix(weightDifferences));
        }
    }

    /**
     * Erstellt die Fehlermatrix.
     * @param layers die Schichten des Netzwerks
     * @param expectedResult das erwartete Ergebnis in Matrixform
     * @param weightMatrices die Gewichte zwischen den Schichten
     * @return die Fehlermatrix
     */
    public ArrayList<Matrix> createErrorMatrix(ArrayList<Matrix> layers, Matrix expectedResult, ArrayList<Matrix> weightMatrices) {
        ArrayList<Matrix> errorMatrices = new ArrayList<>();
        errorMatrices.add(expectedResult.subtractMatrix(layers.getLast()));
        for (int i = layers.size() - 2; i >= 0; i--) {
            Matrix nextErrorMatrix = weightMatrices.get(i).transpose().multiplyWith(errorMatrices.getFirst());
            errorMatrices.addFirst(nextErrorMatrix);
        }

        return errorMatrices;
    }

    private Matrix getHelperMatrix(Matrix errorMatrix, Matrix outputMatrix) {
        Matrix vectorOfOnes = new Matrix(new double[errorMatrix.getRowLength()]);
        vectorOfOnes.fill(1);

        return errorMatrix.elementWiseMultiplicationWith(outputMatrix.elementWiseMultiplicationWith(vectorOfOnes.subtractMatrix(outputMatrix)));
    }
}