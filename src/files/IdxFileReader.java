package files;

import java.io.FileInputStream;
import java.io.IOException;

public class IdxFileReader {
    private static final int MAGIC_NUMBER_IMAGES = 2051;
    private static final int MAGIC_NUMBER_LABELS = 2049;

    /**
     * Liest die Daten aus der Bild- und Labeldatei und speichert sie in einem DataSet.
     * @param imagePath der Pfad zur Bilddatei
     * @param labelPath der Pfad zur Labeldatei
     * @return das DataSet mit allen Daten
     */
    public static DataSet readFile(String imagePath, String labelPath) {
        try (FileInputStream inImage = new FileInputStream(imagePath);
             FileInputStream inLabel = new FileInputStream(labelPath)) {
            int magicNumberImages = (inImage.read() << 24) | (inImage.read() << 16) | (inImage.read() << 8) | (inImage.read());
            if (magicNumberImages != MAGIC_NUMBER_IMAGES) {
                System.out.println("Magic number does not match images!");
                throw new RuntimeException();
            }
            int numberOfImages = (inImage.read() << 24) | (inImage.read() << 16) | (inImage.read() << 8) | (inImage.read());
            int numberOfRows = (inImage.read() << 24) | (inImage.read() << 16) | (inImage.read() << 8) | (inImage.read());
            int numberOfColumns = (inImage.read() << 24) | (inImage.read() << 16) | (inImage.read() << 8) | (inImage.read());

            int magicNumberLabels = (inLabel.read() << 24) | (inLabel.read() << 16) | (inLabel.read() << 8) | (inLabel.read());
            if (magicNumberLabels != MAGIC_NUMBER_LABELS) {
                System.out.println("Magic number does not match labels!");
                throw new RuntimeException();
            }
            int numberOfLabels = (inLabel.read() << 24) | (inLabel.read() << 16) | (inLabel.read() << 8) | (inLabel.read());

            if (numberOfImages != numberOfLabels) {
                System.out.println("Number of images and labels don't match!");
                throw new RuntimeException();
            }

            byte[] imageData = inImage.readAllBytes();
            byte[] labelData = inLabel.readAllBytes();

            int[] intImageData = new int[imageData.length];
            int[] intLabelData = new int[labelData.length];

            for (int i = 0; i < imageData.length; i++) {
                intImageData[i] = Byte.toUnsignedInt(imageData[i]);
            }
            for (int i = 0; i < labelData.length; i++) {
                intLabelData[i] = Byte.toUnsignedInt(labelData[i]);
            }

            return new DataSet(intImageData, intLabelData, numberOfImages, numberOfRows, numberOfColumns);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
