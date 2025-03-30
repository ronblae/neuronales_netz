package neuralNetwork;

import files.DataSet;
import neuralNetwork.workUnits.BackPropagation;
import neuralNetwork.workUnits.Evaluation;
import neuralNetwork.workUnits.SignalProcessing;
import neuralNetwork.workUnits.WeightManagement;
import tools.Matrix;
import tools.functions.IFunction;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class NeuralNetwork implements Runnable {
    private final int[] NUMBERS_OF_NEURONS_IN_HIDDEN_LAYERS;
    private final int NUMBER_OF_HIDDEN_LAYERS;
    private final int NUMBER_OF_LAYERS;
    private final int NUMBER_OF_TIMES;
    private final int threadNumber;
    private static int threadCounter = 0;
    private final DataSet dataSet;
    private final IFunction activationFunction;
    private final String mode;
    private final ArrayList<Matrix> layers = new ArrayList<>();
    private final SignalProcessing signalProcessor = new SignalProcessing();
    private final BackPropagation backPropagator;
    private final WeightManagement weightManager = new WeightManagement();
    private final Evaluation evaluation = new Evaluation();
    private final File weightFile;
    private ArrayList<Matrix> weightMatrices = new ArrayList<>();

    /**
     * Initialisiert das neuronale Netzwerk mit den nötigen Parametern.
     * @param dataSet die Daten aus der Bild- und Labeldatei
     * @param activationFunction die Aktivierungsfunktion für die Signalverarbeitung
     * @param mode entscheidet, ob sich das Netzwerk im Trainings- oder Testmodus befindet (default Test)
     * @param numbersOfNeuronsInHiddenLayers die Anzahl der Neuronen pro Hidden Layer
     * @param learningRate die Lernrate des Netzwerkes
     * @param times die Anzahl der Durchläufe
     */
    public NeuralNetwork(DataSet dataSet, IFunction activationFunction, String mode, int[] numbersOfNeuronsInHiddenLayers, double learningRate, int times) {
        this.dataSet = dataSet;
        this.activationFunction = activationFunction;
        this.mode = mode;

        threadCounter++;
        threadNumber = threadCounter;
        weightFile = new File("weights" + threadNumber + ".bin");

        System.out.println("Thread " + threadNumber + " start! Hiddenlayer: " + Arrays.toString(numbersOfNeuronsInHiddenLayers) + " Lernrate: " + learningRate);

        NUMBER_OF_TIMES = times;
        NUMBERS_OF_NEURONS_IN_HIDDEN_LAYERS = numbersOfNeuronsInHiddenLayers;
        NUMBER_OF_HIDDEN_LAYERS = NUMBERS_OF_NEURONS_IN_HIDDEN_LAYERS.length;
        NUMBER_OF_LAYERS = 2 + NUMBER_OF_HIDDEN_LAYERS;
        backPropagator = new BackPropagation(learningRate);
    }

    /**
     * Startet die Bildverarbeitung.
     * Holt sich nach und nach die Daten aus dem Datenset und schickt sie durch das Netzwerk.
     * Im Falle eines Trainings werden die Gewichte nach jedem Durchlauf angepasst.
     * Gibt zum Schluss die Trefferrate aus.
     */
    @Override
    public void run() {
        final String TRAINING = "training";
        preparation();

        for (int j = 0; j < NUMBER_OF_TIMES; j++) {
            evaluation.resetCounter();
            for (int i = 0; i < dataSet.numberOfItems(); i++) {
                int[] signal = getNextSignal(i);
                Matrix expectedResult = getExpectedResult(i);

                signalProcessor.process(signal, layers, weightMatrices, activationFunction);
                Matrix outputLayer = layers.getLast();
                evaluation.checkResult(outputLayer, expectedResult);
                if (mode.equals(TRAINING)) {
                    backPropagator.propagate(expectedResult, layers, weightMatrices);
                }
            }
            evaluation.printAccuracy(threadNumber, dataSet.numberOfItems());
        }

        if (mode.equals(TRAINING)) {
            weightManager.saveWeights(weightFile, weightMatrices);
        }
    }

    /**
     * Baut das neuronale Netzwerk auf und initialisiert die Gewichte.
     */
    public void preparation() {
        fillLayersWithNeurons();
        setWeights();
    }

    /**
     * Baut die Neuronenschichten auf.
     * Bestimmt die Größen der Schichten anhand der vorhandenen Daten.
     */
    public void fillLayersWithNeurons() {
        int sizeOfInputLayer = dataSet.numberOfRows() * dataSet.numberOfColumns();
        int[] possibleResults = Arrays.stream(dataSet.labelData())
                .distinct()
                .toArray();
        int sizeOfOutputLayer = possibleResults.length;

        layers.add(new Matrix(new double[sizeOfInputLayer]));
        for (int i = 0; i < NUMBER_OF_HIDDEN_LAYERS; i++) {
            layers.add(new Matrix(new double[NUMBERS_OF_NEURONS_IN_HIDDEN_LAYERS[i]]));
        }
        layers.add(new Matrix(new double[sizeOfOutputLayer]));
    }

    /**
     * Initialisiert die Gewichte. Wenn bereits eine Gewichtsdatei vorhanden ist, wird diese verwendet.
     */
    public void setWeights() {
        if (weightFile.exists()) {
            weightMatrices = weightManager.loadWeights(weightFile);
        } else {
            weightManager.setDefaultWeights(layers, weightMatrices, NUMBER_OF_LAYERS);
        }
    }

    /**
     * Liest ein Signal aus den Bilddaten.
     * @param index der Index des zu lesenden Signals
     * @return das Signal
     */
    public int[] getNextSignal(int index) {
        int sizeOfInputLayer = dataSet.numberOfColumns() * dataSet.numberOfRows();
        int[] signal = new int[sizeOfInputLayer];

        System.arraycopy(dataSet.imageData(), index * signal.length, signal, 0, signal.length);

        return signal;
    }

    /**
     * Liest das Label aus den Labeldaten und packt es in eine Matrix, um diese mit dem Ergebnis vergleichen zu können.
     * @param index der Index des zu lesenden Labels
     * @return die Matrix mit dem Eintrag 1 an der Stelle des Labels
     */
    public Matrix getExpectedResult(int index) {
        int sizeOfOutputLayer = layers.getLast().getRowLength();
        double[] expectedResult = new double[sizeOfOutputLayer];

        Arrays.fill(expectedResult, 0);
        expectedResult[dataSet.labelData()[index]] = 1;

        return new Matrix(expectedResult);
    }
}