package geometry;

import core.Intersectable;
import core.Intersection;
import core.Ray;
import core.Spectrum;
import shading.Material;

public class RenderObject<E extends Spectrum<E>> {
    Material<E> material;
    Intersectable<E> collider;
    Light<E> light;

    public RenderObject(Material<E> _material, Intersectable<E> _collider) {
        this.material = _material;
        this.collider = _collider;
    }

    public RenderObject(Material<E> _material, Intersectable<E> _collider, Light<E> _light) {
        this(_material, _collider);
        this.light = _light;
        this.light.setCollider(_collider);
    }
    public Material<E> getMaterial() {
        return material;
    }

    public Intersection<E> intersect(Ray r) {
        Intersection<E> result = collider.intersect(r);
        result.setHitObject(this);
        return result;
    }

    public Light<E> getLight() {
        return light;
    }

    public boolean hasLight() {
        return light != null;
    }
}
