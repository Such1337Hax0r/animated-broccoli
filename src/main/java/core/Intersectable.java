package core;

import geometry.Scene;
import org.joml.Vector3d;
import sampling.Sampler;
import shading.lighting.LightSamplingResult;

public interface Intersectable<E extends Spectrum<E>> {
    Intersection<E> intersect(Ray r);
    Intersection<E> sample(Sampler sampler, Vector3d _startPos, Scene<E> _scene);
    Intersection<E> evaluate(Vector3d _startPos, Vector3d _direction, Scene<E> _scene);
    double area();
}
