package shading.texture;

import core.Spectrum;
import org.joml.Vector3d;

public class ConstantTexture<E extends Spectrum<E>> implements Texture<E> {
    E color;
    public ConstantTexture(E _color) {
        this.color = _color;
    }

    @Override
    public E sample(double u, double v, Vector3d p) {
        return color;
    }
}
