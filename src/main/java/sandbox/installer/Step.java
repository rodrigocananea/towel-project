package sandbox.installer;

public interface Step {
    void close();

    void doStep();

    void init(String... strArr);
}
