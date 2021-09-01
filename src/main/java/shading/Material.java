package shading;

import core.Spectrum;
import org.joml.Vector3d;
import shading.reflection.BxDF;
import shading.reflection.factory.BxDFFactory;
import shading.texture.Texture;

public class Material<E extends Spectrum<E>> {
    BxDFFactory<E> bxdf;

    public Material(BxDFFactory<E> _bxdf) {
        this.bxdf = _bxdf;
    }


    public BxDF<E> getBxDF(double u, double v, Vector3d p, Vector3d n, Vector3d t) {
        return bxdf.construct(u, v, p, n, t);
    }
}
