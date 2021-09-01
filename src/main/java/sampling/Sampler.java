package sampling;

import org.joml.Vector2d;
import org.joml.Vector2i;

public interface Sampler {
    public double get1d(double min, double max);
    public Vector2d get2d(double min, double max);
    public int get1i(int min, int max);
    public Vector2i get2i(int min, int max);
}
