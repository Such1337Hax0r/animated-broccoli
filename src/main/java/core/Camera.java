package core;

public interface Camera {
    Ray genPrimaryRay(double x, double y);

    Ray genPrimaryRayNorm(double x, double y);
}
