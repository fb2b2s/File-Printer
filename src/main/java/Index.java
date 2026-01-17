public enum Index {
    FIRST(0),
    SECOND(1),
    THIRD(2),
    FOURTH(3);

    private final int idx;

    Index(int idx) {
        this.idx = idx;
    }

    public int getIdx() {
        return this.idx;
    }
}
