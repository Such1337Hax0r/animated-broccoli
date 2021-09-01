package rendering.debug;

import core.*;
import geometry.Scene;
import org.joml.Vector2d;
import org.joml.Vector3d;
import shading.Material;
import shading.RGBSpectrum;
import shading.reflection.BxDFResult;

public class ResultDirectionRenderer extends Renderer<RGBSpectrum> {

    public ResultDirectionRenderer() {
        super(new RGBSpectrum(0, 0, 0), new RGBSpectrum(1, 1, 1));
    }

    @Override
    public void render(Camera cam, Film<RGBSpectrum> film, Scene<RGBSpectrum> scene) {
        for (int x = 0; x < film.getWidth(); x++) {
            for (int y = 0; y < film.getHeight(); y++) {
                Ray ray = cam.genPrimaryRay(x,y);
                Intersection<RGBSpectrum> info = scene.intersect(ray);

                double r = 0;
                double g = 0;
                double b = 0;

                if (info.didHit()) {
                    Material<RGBSpectrum> material = info.getHitObject().getMaterial();

                    Vector3d newDirection = new Vector3d();

                    Vector3d newDir = new Vector3d(ray.getDirection().z, ray.getDirection().x, ray.getDirection().y);

                    Vector3d tangent = new Vector3d(info.getHitNormal()).cross(newDir);

                    BxDFResult<RGBSpectrum> result = material.getBxDF(info.getU(), info.getV(), info.getHitPosition(), info.getHitNormal(), tangent).sampleF(new Vector3d(ray.getDirection()).mul(-1), new Vector2d(Math.random(), Math.random()));
                    r = (result.resultDirection.x+1)/2;
                    g = (result.resultDirection.y+1)/2;
                    b = (result.resultDirection.z+1)/2;
                }

                film.setPixel(x,y,new RGBSpectrum(r, g, b));
            }
        }
    }
}