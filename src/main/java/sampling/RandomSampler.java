package sampling;

import org.joml.Vector2d;
import org.joml.Vector2i;

public class RandomSampler implements Sampler {
    @Override
    public double get1d(double min, double max) {
        double range = max-min;
        return min+(Math.random()*range);
    }

    @Override
    public Vector2d get2d(double min, double max) {
        return new Vector2d(get1d(min, max), get1d(min, max));
    }

    @Override
    public int get1i(int min, int max) {
        int range = max-min;
        return (int)(min+(Math.random()*range));
    }

    @Override
    public Vector2i get2i(int min, int max) {
        return new Vector2i(get1i(min, max), get1i(min, max));
    }
}
