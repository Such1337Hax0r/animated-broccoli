package core;

public interface Spectrum<E extends Spectrum<E>> {
    E mul(E s2);
    E mul(double scalar);
    E add(E s2);

    enum RadiometryAlgorithm {
        ALGORITHM_MUL_ROUND
    }

    int toIntRGB(RadiometryAlgorithm alg);

    E clone();

    String toString();

    boolean isZero();

    double maxComponent();
}
