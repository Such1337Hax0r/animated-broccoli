package shading.texture;

import core.Spectrum;
import org.joml.Vector3d;

public interface Texture<E extends Spectrum<E>> {
    E sample(double u, double v, Vector3d p);
}
