package core;

import org.joml.Vector3d;

public class Ray {
    Vector3d origin;
    Vector3d direction;

    public Ray(Vector3d _origin, Vector3d _direction) {
        this.origin = _origin;
        this.direction = _direction;
    }


    public Vector3d getOrigin() {
        return origin;
    }

    public Vector3d getDirection() {
        return direction;
    }
}
