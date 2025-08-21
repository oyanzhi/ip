import java.lang.Math;

public final class ConsoleDisplayLine {
    static final int LENGTH = 50;
    static final String CHAR = "-";

    public static void printHorizontalLine(int spacing) {
        int minimumLineLength = Math.max(spacing, LENGTH);

        System.out.println();

        for (int i = 0; i < spacing; i++) {
            System.out.print(" ");
        }

        for (int j = 0; j < minimumLineLength; j++) {
            System.out.print(CHAR);
        }

        System.out.println();
    }


}