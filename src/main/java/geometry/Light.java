package geometry;

import core.Constants;
import core.Intersectable;
import core.Intersection;
import core.Spectrum;
import org.joml.Vector3d;
import sampling.Sampler;
import shading.lighting.LightSamplingResult;
import shading.texture.Texture;

public class Light<E extends Spectrum<E>> {
    Intersectable<E> collider;
    Texture<E> emittanceMap;

    public Light(Texture<E> _emittanceMap) {
        this.emittanceMap = _emittanceMap;
    }

    public E getEmittance(double u, double v, Vector3d p) {
        return emittanceMap.sample(u, v, p);
    }

    public LightSamplingResult<E> evaluate(Vector3d startPos, Vector3d direction, Scene<E> scene) {
        Intersection<E> info;
        LightSamplingResult<E> result;
        if (collider != null) {
            info = collider.evaluate(startPos, direction, scene);
            if (info.didHit()) {
                result = new LightSamplingResult<>(
                        startPos.distanceSquared(info.getHitPosition())/(Math.abs(info.getHitNormal().dot(new Vector3d(info.getHitTangent()).mul(-1))) * collider.area()),
                        info.getHitTangent()
                );
                result.lightOutput = getEmittance(info.getU(), info.getV(), info.getHitPosition());
            } else {
                result = new LightSamplingResult<>(0, null);
            }
        } else {
            result = new LightSamplingResult<>(0, null);
        }
        return result;
    }

    public LightSamplingResult<E> sample(Sampler sampler, Vector3d startPos, Scene<E> scene) {
        Intersection<E> info;
        LightSamplingResult<E> result;
        if (collider != null) {
            info = collider.sample(sampler, startPos, scene);
            if (info.didHit()) {
                if (info.getHitNormal().lengthSquared() > 1 + Constants.BIAS) {
                    //System.out.println(info.getHitTangent().lengthSquared());
                }
                result = new LightSamplingResult<>(
                        info.getPdf(),
                        //startPos.distanceSquared(info.getHitPosition())/(Math.abs(info.getHitNormal().dot(new Vector3d(info.getHitTangent()).mul(-1))) * collider.area()),
                        info.getHitTangent()
                        );
                if (info.getPdf() > 1) System.out.println(startPos + " " + info.getHitPosition() + " " + info.getHitNormal() + " " + info.getHitTangent() + " " + collider.area());
                result.lightOutput = getEmittance(info.getU(), info.getV(), info.getHitPosition());
            } else {
                result = new LightSamplingResult<>(0, null);
            }
        } else {
            result = new LightSamplingResult<>(0, null);
        }
        return result;
    }

    public void setCollider(Intersectable<E> collider) {
        this.collider = collider;
    }

    public Intersectable<E> getCollider() {
        return collider;
    }
}
