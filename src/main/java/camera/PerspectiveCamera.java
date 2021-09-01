package camera;

import core.Camera;
import core.Ray;
import org.joml.Vector3d;

public class PerspectiveCamera implements Camera {
    double aspectRatio;
    double fov;
    int imageWidth, imageHeight;

    //_fov is in degrees
    public PerspectiveCamera(int _imageWidth, int _imageHeight, double _fov) {
        this.imageWidth = _imageWidth;
        this.imageHeight = _imageHeight;
        this.aspectRatio = _imageWidth/(double)_imageHeight;
        this.fov = Math.toRadians(_fov);
    }

    @Override
    public Ray genPrimaryRay(double x, double y) {
        return genPrimaryRayNorm((x+0.5d)/imageWidth, (y+0.5d)/imageHeight);
    }

    //Generate primary ray given coordinates from (0,1)
    @Override
    public Ray genPrimaryRayNorm(double x, double y) {
        double px = ((2*x)-1)*Math.tan(fov/2)*aspectRatio;
        double py = ((2*y)-1)*Math.tan(fov/2);
        return new Ray(new Vector3d(0), new Vector3d(px, py, -1).normalize());
    }
}


/*
float imageAspectRatio = imageWidth / (float)imageHeight; // assuming width > height
float Px = (2 * ((x + 0.5) / imageWidth) - 1) * tan(fov / 2 * M_PI / 180) * imageAspectRatio;
float Py = (1 - 2 * ((y + 0.5) / imageHeight) * tan(fov / 2 * M_PI / 180);
 */
