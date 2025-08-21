public final class ConsoleDisplayLine {
    static final int LENGTH = 50;
    static final String CHAR = "-";

    public static void printHorizontalLine() {
        System.out.println();

        for (int i = 0; i < LENGTH; ++i) {
            System.out.print(CHAR);
        }

        System.out.println();
    }
}