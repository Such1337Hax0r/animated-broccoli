package geometry;

import core.Intersectable;
import core.Intersection;
import core.Ray;
import core.Spectrum;
import org.joml.Vector3d;
import sampling.Sampler;
import shading.lighting.LightSamplingResult;

import java.util.ArrayList;
import java.util.Iterator;

public class Scene<E extends Spectrum<E>> implements Intersectable<E> {
    ArrayList<RenderObject<E>> objects;
    ArrayList<Light<E>> lights;

    public Scene() {
        objects = new ArrayList<>();
        lights = new ArrayList<>();
    }

    public void addObject(RenderObject<E> obj) {
        objects.add(obj);
        if (obj.hasLight()) {
            lights.add(obj.getLight());
        }
    }

    @Override
    public Intersection<E> intersect(Ray r) {
        Intersection<E> ret = new Intersection<>();
        for (RenderObject<E> obj : objects) {
            Intersection<E> info = obj.intersect(r);
            if (info.didHit() && info.getDistance() < ret.getDistance()) {
                ret = info;
            }
        }
        return ret;
    }

    public int numLights() {
        return lights.size();
    }

    public Iterator<Light<E>> getLightIterator() {
        return lights.iterator();
    }

    public Light<E> getLight(int idx) {
        return lights.get(idx);
    }


    //Here to satisfy the interface, never actually implemented or called (hopefully?)
    @Override
    public Intersection<E> sample(Sampler sampler, Vector3d _startPos, Scene<E> _scene) {
        return null;
    }

    @Override
    public Intersection<E> evaluate(Vector3d _startPos, Vector3d _direction, Scene<E> _scene) {
        return null;
    }

    @Override
    public double area() {
        return -1;
    }
}
