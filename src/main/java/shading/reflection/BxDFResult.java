package shading.reflection;

import core.Spectrum;
import org.joml.Vector3d;

import java.security.PublicKey;

public class BxDFResult<E extends Spectrum<E>> {
    public E spectrum;
    public Vector3d resultDirection;
    public double pdf;
}
