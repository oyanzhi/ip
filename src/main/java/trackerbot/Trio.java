package trackerbot;

/**
 * A class used to represent an object of 3 varying data types
 * @param <U>
 * @param <V>
 * @param <W>
 */
public class Trio<U, V, W> {
    private final U head;
    private final V body;
    private final W tail;

    /**
     * Creates a Trio storing the different data
     * @param head Data to be stored in the head
     * @param body Data to be stored in the body
     * @param tail Data to be stored in the tail
     */
    public Trio(U head, V body, W tail) {
        this.head = head;
        this.body = body;
        this.tail = tail;
    }

    /**
     * @return The data in the head
     */
    public U getHead() {
        return this.head;
    }

    /**
     * @return The data in the body
     */
    public V getBody() {
        return this.body;
    }

    /**
     * @return The data in the tail
     */
    public W getTail() {
        return this.tail;
    }

}
