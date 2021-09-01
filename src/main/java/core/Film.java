package core;

import java.awt.image.BufferedImage;

//TODO: Figure out how the generics work here
public class Film<E extends Spectrum<E>> {
    int width, height;
    private Spectrum<E>[][] values;
    public Film(int _width, int _height) {
        this.width = _width;
        this.height = _height;
        values = new Spectrum[width][height];
    }

    public void setPixel(int x, int y, E value) {
        if (x < width && y < height)
        values[x][y]=value;
    }

    public BufferedImage toRenderedImage(Spectrum.RadiometryAlgorithm alg) {
        BufferedImage ret = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (values[x][y] == null)
                System.out.println(x + " " + y);
                ret.setRGB(x,y,values[x][y].toIntRGB(alg));
            }
        }
        return ret;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
