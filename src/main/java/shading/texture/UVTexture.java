package shading.texture;

import org.joml.Vector3d;
import shading.RGBSpectrum;

public class UVTexture implements Texture<RGBSpectrum> {
    @Override
    public RGBSpectrum sample(double u, double v, Vector3d p) {
        return new RGBSpectrum(u, v, 0);
    }
}
