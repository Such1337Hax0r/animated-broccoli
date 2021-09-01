package rendering.debug;

import core.Camera;
import core.Film;
import core.Intersectable;
import core.Renderer;
import geometry.Scene;
import shading.RGBSpectrum;

public class CoordinateRenderer extends Renderer<RGBSpectrum> {
    public CoordinateRenderer() {
        super(new RGBSpectrum(0, 0, 0), new RGBSpectrum(1, 1, 1));
    }

    @Override
    public void render(Camera cam, Film<RGBSpectrum> film, Scene<RGBSpectrum> scene) {
        for (int x = 0; x < film.getWidth(); x++) {
            for (int y = 0; y < film.getHeight(); y++) {
                film.setPixel(x,y,new RGBSpectrum(x/(double)film.getWidth(), y/(double)film.getHeight(), 0));
            }
        }
    }
}
