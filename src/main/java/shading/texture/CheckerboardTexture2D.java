package shading.texture;

import core.Spectrum;
import org.joml.Vector3d;

public class CheckerboardTexture2D<E extends Spectrum<E>> implements Texture<E> {
    Texture<E> even, odd;
    double width;

    public CheckerboardTexture2D(Texture<E> _even, Texture<E> _odd, double _width) {
        this.even = _even;
        this.odd = _odd;
        this.width = _width;
    }

    @Override
    public E sample(double u, double v, Vector3d p) {
        double u2 = Math.floor(u * width);
        double v2 = Math.floor(v * width);
        if (((u2 + v2) % 2) == 0) {
            return odd.sample(u, v, p);
        } else {
            return even.sample(u, v, p);
        }
    }
}