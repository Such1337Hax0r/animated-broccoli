package geometry;

import core.Intersectable;
import core.Intersection;
import core.Ray;
import core.Spectrum;
import org.joml.Vector2d;
import org.joml.Vector3d;
import sampling.Sampler;
import shading.Material;
import shading.lighting.LightSamplingResult;

public class Sphere<E extends Spectrum<E>> implements Intersectable<E> {
    Vector3d center;
    double radius;
    double radius2;

    public Sphere(Vector3d _center, double _radius) {
        this.center = _center;
        this.radius = _radius;
        this.radius2 = _radius * _radius;
    }

    @Override
    public Intersection<E> intersect(Ray r) {
        Vector3d oc = new Vector3d(r.getOrigin()).sub(center);
        double a = r.getDirection().dot(r.getDirection());
        double b = 2 * oc.dot(r.getDirection());
        double c = oc.dot(oc) - radius2;
        double discriminant = b * b - 4 * a * c;
        if (discriminant < 0) {
            return new Intersection<>();
        } else {
            double distance = (-b - Math.sqrt(discriminant)) / (2*a);
            if (distance > 0) {
                Vector3d hitPosition = new Vector3d(r.getDirection()).mul(distance).add(r.getOrigin());
                Vector3d hitNormal = new Vector3d(hitPosition).sub(center).div(radius);
                double phi = Math.atan2(hitNormal.z, hitNormal.x);
                double theta = Math.asin(hitNormal.y);
                Vector3d tangent = new Vector3d(-Math.sin(phi), 0, Math.cos(phi));
                double u = 1 - (phi + Math.PI) / (2 * Math.PI);
                double v = (theta + Math.PI / 2) / Math.PI;
                return new Intersection<>(distance, hitPosition, hitNormal, tangent, u, v);
            } else {
                return new Intersection<>();
            }
        }
    }

    @Override
    public Intersection<E> sample(Sampler sampler, Vector3d _startPos, Scene<E> _scene) {
        Vector3d hitPos = fromSphericalCoords(sampler.get2d(0, 1));
        Vector3d resultDirection = new Vector3d(hitPos).sub(_startPos).normalize();
        return evaluate(_startPos, resultDirection, _scene);
    }

    //TODO: Kind of hacky here, will return the resulting direction in ret.hitTangent
    @Override
    public Intersection<E> evaluate(Vector3d _startPos, Vector3d _direction, Scene<E> _scene) {
        Ray ray = new Ray(new Vector3d(_startPos), new Vector3d(_direction));
        Intersection<E> info = _scene.intersect(ray);
        Intersection<E> ret;
        if (info.didHit() && info.getHitObject().hasLight() && info.getHitObject().getLight().getCollider() == this) {
            ret = new Intersection<>(info.getDistance(), info.getHitPosition(), info.getHitNormal(), _direction, info.getU(), info.getV(), (1/area()));
        } else {
            ret = new Intersection<>();
        }
        return ret;
    }

    public double area() {
        return (4 * Math.PI * radius2);
    }

    Vector3d fromSphericalCoords(Vector2d u) {
        double z = 1 - 2 * u.x;
        double r = Math.sqrt(Math.max(0, 1 - z * z));
        double phi = 2 * Math.PI * u.y;
        return new Vector3d(r * Math.cos(phi), r * Math.sin(phi), z).mul(radius).add(center);
    }
}
