package shading.texture;

import core.Spectrum;
import org.joml.Vector3d;

public class CheckerboardTexture3D<E extends Spectrum<E>> implements Texture<E> {
    Texture<E> even, odd;

    public CheckerboardTexture3D(Texture<E> _even, Texture<E> _odd) {
        this.even = _even;
        this.odd = _odd;
    }

    @Override
    public E sample(double u, double v, Vector3d p) {
        double sines = Math.sin(10 * p.x) * Math.sin(10 * p.y) * Math.sin(10 * p.z);
        if (sines < 0) {
            return odd.sample(u, v, p);
        } else {
            return even.sample(u, v, p);
        }
    }
}
