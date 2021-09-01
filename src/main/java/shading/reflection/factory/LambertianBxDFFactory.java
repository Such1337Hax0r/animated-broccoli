package shading.reflection.factory;

import core.Spectrum;
import org.joml.Vector3d;
import shading.reflection.BxDF;
import shading.reflection.LambertianBxDF;
import shading.texture.Texture;

public class LambertianBxDFFactory<E extends Spectrum<E>> implements BxDFFactory<E> {
    Texture<E> albedoMap;

    public LambertianBxDFFactory(Texture<E> _albedoMap) {
        this.albedoMap = _albedoMap;
    }

    @Override
    public BxDF<E> construct(double u, double v, Vector3d p, Vector3d n, Vector3d t) {
        return new LambertianBxDF<>(albedoMap.sample(u, v, p), n, t);
    }
}
