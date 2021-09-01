package shading.reflection;

import core.Constants;
import core.Spectrum;
import org.joml.Matrix3d;
import org.joml.Vector2d;
import org.joml.Vector3d;

import java.util.EnumSet;

public class LambertianBxDF<E extends Spectrum<E>> extends BxDF<E> {
    E reflectedLight;
    E reflectedLightInvPi;
    Vector3d t;
    Vector3d b;
    Vector3d n;
    Matrix3d tangentSpaceToWorldSpace;

    public LambertianBxDF (E _reflectedLight, Vector3d _n, Vector3d _t) {
        super(EnumSet.of(BxDFType.REFLECTION, BxDFType.DIFFUSE));
        this.reflectedLight = _reflectedLight;
        this.reflectedLightInvPi = _reflectedLight.clone().mul(1/Math.PI);
        this.n = _n;
        this.t = _t;
        this.b = new Vector3d(t).cross(n).normalize();
        this.tangentSpaceToWorldSpace = new Matrix3d(t, b, n);
    }

    @Override
    public BxDFResult<E> f(Vector3d wi, Vector3d wo) {
        BxDFResult<E> result = new BxDFResult<>();
        result.spectrum = reflectedLightInvPi;
        result.resultDirection = wi;
        result.pdf=1/(2*Math.PI);
        return result;
    }

    @Override
    public BxDFResult<E> sampleF(Vector3d wo, Vector2d u) {
        BxDFResult<E> result = f(Constants.cosineWeightedHemisphere(u.x, u.y, tangentSpaceToWorldSpace), wo);
        result.pdf = result.resultDirection.dot(n)/Math.PI;
        return result;
    }
}
