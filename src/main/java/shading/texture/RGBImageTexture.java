package shading.texture;

import org.joml.Vector3d;
import shading.RGBSpectrum;

import java.awt.image.BufferedImage;

public class RGBImageTexture implements Texture<RGBSpectrum> {
    BufferedImage image;

    public RGBImageTexture(BufferedImage _image) {
        this.image = _image;
    }

    @Override
    public RGBSpectrum sample(double u, double v, Vector3d p) {
        double x = (u*image.getWidth());
        double y = (v*image.getHeight());
        int x0 = (int)x;
        int y0 = (int)y;
        if (x0+1 < image.getWidth() && y0+1 < image.getHeight()) {
            double xWeight = x - x0;
            double yWeight = y - y0;
            int color00 = image.getRGB(x0, y0);
            int b00 = (color00) & 0xFF;
            int g00 = (color00 >> 8) & 0xFF;
            int r00 = (color00 >> 16) & 0xFF;
            int color01 = image.getRGB(x0, y0 + 1);
            int b01 = (color01) & 0xFF;
            int g01 = (color01 >> 8) & 0xFF;
            int r01 = (color01 >> 16) & 0xFF;
            int color10 = image.getRGB(x0 + 1, y0);
            int b10 = (color10) & 0xFF;
            int g10 = (color10 >> 8) & 0xFF;
            int r10 = (color10 >> 16) & 0xFF;
            int color11 = image.getRGB(x0 + 1, y0 + 1);
            int b11 = (color11) & 0xFF;
            int g11 = (color11 >> 8) & 0xFF;
            int r11 = (color11 >> 16) & 0xFF;
            double ba = (((double)b00) * (1 - xWeight)) + (((double)b10) * (xWeight));
            double bb = (((double)b01) * (1 - xWeight)) + (((double)b11) * (xWeight));
            double ga = (((double)g00) * (1 - xWeight)) + (((double)g10) * (xWeight));
            double gb = (((double)g01) * (1 - xWeight)) + (((double)g11) * (xWeight));
            double ra = (((double)r00) * (1 - xWeight)) + (((double)r10) * (xWeight));
            double rb = (((double)r01) * (1 - xWeight)) + (((double)r11) * (xWeight));
            double b = ba * (1 - yWeight) + bb * (yWeight);
            double g = ga * (1 - yWeight) + gb * (yWeight);
            double r = ra * (1 - yWeight) + rb * (yWeight);
            return new RGBSpectrum(r / 255, g / 255, b / 255);
        } else {
            int color = image.getRGB(x0, y0);
            int b = (color      ) & 0xFF;
            int g = (color >> 8 ) & 0xFF;
            int r = (color >> 16) & 0xFF;
            return new RGBSpectrum(r/(double)255, g/(double)255, b/(double)255);
        }
    }

}
