package rendering.debug;

import core.*;
import geometry.Scene;
import sampling.Sampler;
import shading.RGBSpectrum;

public class DistanceRenderer extends SamplingRenderer<RGBSpectrum> {

    public DistanceRenderer() {
        super(new RGBSpectrum(0, 0, 0), new RGBSpectrum(1, 1, 1), null);
    }

    @Override
    public RGBSpectrum sample(Ray ray, Scene scene, Sampler sampler, int x, int y) {
        Intersection info = scene.intersect(ray);
        double d = info.didHit()?info.getDistance()/25.5:0;

        return new RGBSpectrum (d, d, d);
    }
}
