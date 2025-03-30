package files;

/**
 * Ein Record, in dem die Daten aus der Bild- und Labeldatei gespeichert werden.
 * @param imageData die Bilddaten
 * @param labelData die Labeldaten
 * @param numberOfItems die Anzahl der Daten
 * @param numberOfRows die Anzahl der Bildpixelreihen
 * @param numberOfColumns die Anzhahl der Bildpixelspalten
 */
public record DataSet(int[] imageData, int[] labelData, int numberOfItems, int numberOfRows, int numberOfColumns) {}
