package neuralNetwork.workUnits;

import tools.Matrix;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class WeightManagement {
    private static final double LOWER_BOUND = -0.3;
    private static final double UPPER_BOUND = 0.3;

    /**
     * Lädt die Daten einer Gewichtsdatei in die Gewichtsmatrizen.
     * @param weightFile die Gewichtsdatei
     * @return eine Liste mit Gewichtsmatrizen
     */
    @SuppressWarnings("unchecked")
    public ArrayList<Matrix> loadWeights(File weightFile) {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(weightFile))) {
            return (ArrayList<Matrix>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Speichert die Gewichte in eine Datei.
     * @param weightFile die Gewichtsdatei
     * @param weightMatrices die Liste der Gewichtsmatrizen
     */
    public void saveWeights(File weightFile, ArrayList<Matrix> weightMatrices) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(weightFile))) {
            objectOutputStream.writeObject(weightMatrices);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Initialisiert die Gewichte mit zufälligen Werten zwischen 2 Grenzen.
     * @param layers die Schichten des Netzwerks
     * @param weightMatrices die Gewichte zwischen den Schichten
     * @param numberOfLayers die Anzahl der Schichten.
     */
    public void setDefaultWeights(ArrayList<Matrix> layers, ArrayList<Matrix> weightMatrices, int numberOfLayers) {
        for (int i = 0; i < numberOfLayers - 1; i++) {
            weightMatrices.add(new Matrix(new double[layers.get(i + 1).getRowLength()][layers.get(i).getRowLength()]));
        }

        Random random = new Random();

        for (Matrix weightMatrix : weightMatrices) {
            for (int row = 0; row < weightMatrix.getRowLength(); row++) {
                for (int column = 0; column < weightMatrix.getColumnLength(); column++) {
                    weightMatrix.getMatrix()[row][column] = random.nextDouble(LOWER_BOUND, UPPER_BOUND);
                }
            }
        }
    }
}
