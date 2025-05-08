package app.BackEnd.Algorithms.preprocessing.utilization;

/**
 * This class defined a Pair of two elements
 */
public class Pair<P,D> {
    private final P first;
    private final D second;

    public Pair(P first, D second) {
        this.first = first;
        this.second = second;
    }

    public P getFirst() {
        return first;
    }

    public D getSecond() {
        return second;
    }
}
