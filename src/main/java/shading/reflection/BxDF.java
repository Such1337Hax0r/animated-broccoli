package shading.reflection;

import core.Spectrum;
import org.joml.Vector2d;
import org.joml.Vector3d;

import java.util.EnumSet;

public abstract class BxDF<E extends Spectrum<E>> {
    public enum BxDFType {
        REFLECTION,
        TRANSMISSION,
        DIFFUSE,
        GLOSSY,
        SPECULAR;

        public static final EnumSet<BxDFType> ALL = EnumSet.of(REFLECTION, TRANSMISSION, DIFFUSE, GLOSSY, SPECULAR);
    }

    EnumSet<BxDFType> flags;

    public BxDF (EnumSet<BxDFType> _flags) {
        this.flags = _flags;
    }

    public abstract BxDFResult<E> f(Vector3d wi, Vector3d wo);
    public abstract BxDFResult<E> sampleF(Vector3d wo, Vector2d u);

    public boolean matchesFlags(EnumSet<BxDFType> query) {
        return flags.containsAll(query);
    }
    public boolean matchesFlag(BxDFType query) {
        return flags.contains(query);
    }
}
