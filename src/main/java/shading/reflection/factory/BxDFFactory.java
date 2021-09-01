package shading.reflection.factory;

import core.Spectrum;
import org.joml.Vector3d;
import shading.reflection.BxDF;

public interface BxDFFactory<E extends Spectrum<E>> {
    BxDF<E> construct(double u, double v, Vector3d p, Vector3d n, Vector3d t);
}
