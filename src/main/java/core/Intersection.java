package core;

import geometry.RenderObject;
import org.joml.Vector3d;
import shading.Material;

public class Intersection<E extends Spectrum<E>> {
    boolean didHit;
    double distance;
    Vector3d hitPosition;
    Vector3d hitNormal, hitTangent;
    RenderObject<E> hitObject;
    double u, v;
    double pdf;

    //Constructor for misses
    public Intersection() {
        this.didHit = false;
        this.distance = Double.MAX_VALUE;
        this.hitPosition = null;
        this.hitNormal = null;
        this.hitObject = null;
        this.u = -1;
        this.v = -1;
        this.pdf = 0;
    }

    public Intersection(double _distance, Vector3d _hitPosition, Vector3d _hitNormal, Vector3d _hitTangent, double _u, double _v) {
        this.didHit = true;
        this.distance = _distance;
        this.hitNormal = _hitNormal;
        this.hitPosition = new Vector3d(_hitNormal).mul(Constants.BIAS).add(_hitPosition);
        this.hitTangent = _hitTangent;
        this.u = _u;
        this.v = _v;
    }

    public Intersection(double _distance, Vector3d _hitPosition, Vector3d _hitNormal, Vector3d _hitTangent, double _u, double _v, double _pdf) {
        this(_distance, _hitPosition, _hitNormal, _hitTangent, _u, _v);
        this.pdf = _pdf;
    }

    public boolean didHit() {
        return didHit;
    }

    public double getDistance() {
        return distance;
    }

    public Vector3d getHitPosition() {
        return hitPosition;
    }

    public Vector3d getHitNormal() {
        return hitNormal;
    }

    public Vector3d getHitTangent() {
        return hitTangent;
    }

    public RenderObject<E> getHitObject() {
        return hitObject;
    }

    public void setHitObject(RenderObject<E> hitObject) {
        this.hitObject = hitObject;
    }

    public double getU() {
        return u;
    }

    public double getV() {
        return v;
    }

    public double getPdf() {
        return pdf;
    }

    public void setPdf(double pdf) {
        this.pdf = pdf;
    }
}
