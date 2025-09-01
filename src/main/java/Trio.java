public class Trio<U, V, W> {
    private final U head;
    private final V body;
    private final W tail;

    public Trio(U head, V body, W tail) {
        this.head = head;
        this.body = body;
        this.tail = tail;
    }

    public U getHead() {
        return this.head;
    }

    public V getBody() {
        return this.body;
    }

    public W getTail() {
        return this.tail;
    }

}
