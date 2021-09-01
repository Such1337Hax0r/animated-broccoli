package rendering.debug;

import core.*;
import geometry.Scene;
import shading.RGBSpectrum;

public class HitCoordinateRenderer extends Renderer<RGBSpectrum> {

    public HitCoordinateRenderer() {
        super(new RGBSpectrum(0, 0, 0), new RGBSpectrum(1, 1, 1));
    }

    @Override
    public void render(Camera cam, Film<RGBSpectrum> film, Scene<RGBSpectrum> scene) {
        for (int x = 0; x < film.getWidth(); x++) {
            for (int y = 0; y < film.getHeight(); y++) {
                Ray ray = cam.genPrimaryRay(x,y);
                Intersection<RGBSpectrum> info = scene.intersect(ray);
                //double r = info.didHit()?(x/(double)film.getWidth()):0;
                //double g = info.didHit()?(y/(double)film.getHeight()):0;
                //double b = 0;
                //double r = info.didHit()?(Math.abs(info.getHitNormal().x)):0;
                double r = 0;
                //double g = info.didHit()?(Math.abs(info.getHitNormal().y)):0;
                double g = (info.didHit() && info.getHitPosition().z > -15) ? 1 : 0;
                //double b = info.didHit()?(Math.abs(info.getHitNormal().z)):0;
                if (info.didHit() && info.getHitNormal().z < 0) {
                    System.out.println(info.getHitNormal() + " " + info.getHitPosition());
                }
                double b = (info.didHit() && info.getHitPosition().z < -15) ? 1 : 0;

                film.setPixel(x,y,new RGBSpectrum(r, g, b));
            }
        }
    }
}