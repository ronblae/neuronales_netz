package tools;

import tools.functions.IFunction;

import java.io.Serializable;
import java.util.Arrays;

public class Matrix implements Serializable {
    private final double[][] matrix;
    private final int rowLength, columnLength;

    /**
     * Initialisiert die Matrix.
     * @param matrix die Matrix als zweidimensionales Array
     */
    public Matrix(double[][] matrix) {
        if (isJaggedArray(matrix)) {
            System.out.println("Matrix muss ein Rechteck sein!");
            throw new RuntimeException();
        }

        this.matrix = matrix;
        rowLength = matrix.length;
        columnLength = matrix[0].length;
    }

    /**
     * Initialisiert die Matrix als Vektor.
     * @param matrix die Matrix als Array
     */
    public Matrix(double[] matrix) {
        this.matrix = new double[matrix.length][1];
        for (int i = 0; i < matrix.length; i++) {
            this.matrix[i][0] = matrix[i];
        }
        rowLength = matrix.length;
        columnLength = 1;
    }

    /**
     * Kontrolliert, ob die Matrix gezackt und somit keine Matrix ist.
     * @param matrix die betroffene Matrix
     * @return true, wenn Matrix gezackt, sonst false
     */
    private boolean isJaggedArray(double[][] matrix) {
        int rowLength = matrix[0].length;
        for (double[] row : matrix) {
            if (row.length != rowLength) {
                return true;
            }
        }
        return false;
    }

    /**
     * Multipliziert zwei Matrizen miteinander
     * @param rightMatrix die rechte Matrix der Operation
     * @return die Ergebnismatrix
     */
    public Matrix multiplyWith(Matrix rightMatrix) {
        if (rightMatrix == null) {
            return null;
        }

        if (this.columnLength != rightMatrix.getRowLength()) {
            return null;
        }

        double[][] matrixA = matrix;
        double[][] matrixB = rightMatrix.getMatrix();


        double[][] resultingMatrix = new double[matrixA.length][matrixB[0].length];

        for (int i = 0; i < matrixA.length; i++) {
            for (int j = 0; j < matrixB[0].length; j++) {
                resultingMatrix[i][j] = rowTimesColumn(matrixA, matrixB, i, j);
            }
        }
        return new Matrix(resultingMatrix);
    }

    /**
     * Multipliziert die Elemente zweier Matrizen miteinander
     * @param rightMatrix die rechte Matrix der Operation
     * @return die Ergebnismatrix
     */
    public Matrix elementWiseMultiplicationWith(Matrix rightMatrix) {
        if (rightMatrix == null) {
            return null;
        }
        if (this.rowLength != rightMatrix.getRowLength()) {
            return null;
        }
        if (this.columnLength != rightMatrix.getColumnLength()) {
            return null;
        }

        double[][] matrixB = rightMatrix.getMatrix();

        double[][] resultMatrix = new double[rowLength][columnLength];

        for (int i = 0; i < rowLength; i++) {
            for (int j = 0; j < columnLength; j++) {
                resultMatrix[i][j] = matrix[i][j] * matrixB[i][j];
            }
        }

        return new Matrix(resultMatrix);
    }

    /**
     * Addiert die Elemente zweier Matrizen miteinander
     * @param rightMatrix die rechte Matrix der Operation
     * @return die Ergebnismatrix
     */
    public Matrix addMatrix(Matrix rightMatrix) {
        if (rightMatrix == null) {
            return null;
        }
        if (this.rowLength != rightMatrix.getRowLength()) {
            return null;
        }
        if (this.columnLength != rightMatrix.getColumnLength()) {
            return null;
        }

        double[][] matrixB = rightMatrix.getMatrix();

        double[][] resultMatrix = new double[rowLength][columnLength];

        for (int i = 0; i < rowLength; i++) {
            for (int j = 0; j < columnLength; j++) {
                resultMatrix[i][j] = matrix[i][j] + matrixB[i][j];
            }
        }

        return new Matrix(resultMatrix);
    }

    /**
     * Subtrahiert die Elemente zweier Matrizen miteinander
     * @param rightMatrix die rechte Matrix der Operation
     * @return die Ergebnismatrix
     */
    public Matrix subtractMatrix(Matrix rightMatrix) {
        double[][] copy = Arrays.stream(rightMatrix.getMatrix())
                .map(double[]::clone)
                .toArray(double[][]::new);
        Matrix helperMatrix = new Matrix(copy);

        helperMatrix.applyScalar(-1);
        return addMatrix(helperMatrix);
    }

    private static double rowTimesColumn(double[][] matrixA, double[][] matrixB, int row, int column) {
        double result = 0;
        for (int i = 0; i < matrixA[0].length; i++) {
            result += matrixA[row][i] * matrixB[i][column];
        }

        return result;
    }

    /**
     * Füllt eine Matrix mit einem Wert.
     * @param value der Wert
     */
    public void fill(double value) {
        Arrays.stream(matrix)
                .forEach(row -> Arrays.fill(row, value));
    }

    /**
     * Wendet die übergebene Funktion auf jedes Element der Matrix an.
     * @param function die Funktion
     */
    public void applyFunction(IFunction function) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = function.function(matrix[i][j]);
            }
        }
    }

    /**
     * Wendet einen Skalar auf die Elemente der Matrix an.
     * @param scalar der Skalar
     */
    public void applyScalar(double scalar) {
        for (int i = 0; i < rowLength; i++) {
            for (int j = 0; j < columnLength; j++) {
                matrix[i][j] *= scalar;
            }
        }
    }

    /**
     * Berechnet die transponierte Matrix und gibt sie zurück. Ändert nicht die Matrix selbst.
     * @return die transponierte Matrix
     */
    public Matrix transpose() {
        double[][] transposedMatrix = new double[matrix[0].length][matrix.length];

        for (int i = 0; i < transposedMatrix.length; i++) {
            for (int j = 0; j < transposedMatrix[i].length; j++) {
                transposedMatrix[i][j] = matrix[j][i];
            }
        }
        return new Matrix(transposedMatrix);
    }

    @Override
    public String toString() {
        StringBuilder stringRepresentation = new StringBuilder();

        for (double[] row : matrix) {
            stringRepresentation.append('|');
            for (double column : row) {
                stringRepresentation.append(column).append("   ");
            }
            stringRepresentation.delete(stringRepresentation.length() - 3, stringRepresentation.length());
            stringRepresentation.append("|\n");
        }
        return stringRepresentation.toString();
    }

    public double[][] getMatrix() {
        return matrix;
    }

    public int getRowLength() {
        return rowLength;
    }

    public int getColumnLength() {
        return columnLength;
    }
}
