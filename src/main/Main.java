package main;

import files.DataSet;
import files.IdxFileReader;
import neuralNetwork.NeuralNetwork;
import org.jetbrains.annotations.NotNull;
import tools.functions.IFunction;
import tools.functions.SigmoidFunction;

public class Main {

    /**
     * Liest die Programmargumente und startet die Threads der neuronalen Netzwerke.
     * @param args An erster Stelle wird der Modus erwartet Training/Test. Danach kommt der Bildpfad und der Labelpfad.
     */
    public static void main(String @NotNull [] args) {
        String mode = args[0];
        String imageFilePath = args[1];
        String labelFilePath = args[2];

        IFunction activationFunction = new SigmoidFunction();
        DataSet data = IdxFileReader.readFile(imageFilePath, labelFilePath);

        new Thread(new NeuralNetwork(data, activationFunction, mode, new int[]{81}, 0.1, 1)).start();
        new Thread(new NeuralNetwork(data, activationFunction, mode, new int[]{81}, 0.01, 1)).start();
        new Thread(new NeuralNetwork(data, activationFunction, mode, new int[]{71}, 0.1, 1)).start();
        new Thread(new NeuralNetwork(data, activationFunction, mode, new int[]{91}, 0.1, 1)).start();
        new Thread(new NeuralNetwork(data, activationFunction, mode, new int[]{81, 26}, 0.1, 1)).start();
    }
}