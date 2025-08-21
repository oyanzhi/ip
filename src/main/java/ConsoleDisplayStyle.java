import java.lang.Math;

public final class ConsoleDisplayStyle {
    static final int MINIMUM_LENGTH = 50;
    static final String CHAR = "-";

    public static void printHorizontalLine(int spacing, int length) {
        int minimumLineLength = Math.max(length, MINIMUM_LENGTH);

        System.out.println();

        for (int i = 0; i < spacing; i++) {
            System.out.print(" ");
        }

        for (int j = 0; j < minimumLineLength; j++) {
            System.out.print(CHAR);
        }

        System.out.println();
    }

    public static void printIndentation(int spacing) {
        for (int i = 0; i < spacing; i++) {
            System.out.print(" ");
        }
    }

    public static void printBasicStyling(int spacing, int length, String defaultText) {
        printHorizontalLine(spacing, length);
        printIndentation(spacing);
        System.out.println(defaultText);
        printHorizontalLine(spacing, length);
    }


}