package shading;

import core.Constants;
import core.Spectrum;
import org.joml.Vector3d;

public class RGBSpectrum implements Spectrum<RGBSpectrum> {
    Vector3d value;

    public RGBSpectrum() {
        this.value = new Vector3d().zero();
    }

    public RGBSpectrum(double r, double g, double b) {
        this.value = new Vector3d(r, g, b);
    }

    public RGBSpectrum(Vector3d _value) {
        this.value = _value;
    }

    public RGBSpectrum(RGBSpectrum _copyFrom) {
        this.value = new Vector3d(_copyFrom.value);
    }

    @Override
    public RGBSpectrum mul(RGBSpectrum s2) {
        value.mul(s2.value);
        return this;
    }

    @Override
    public RGBSpectrum mul(double scalar) {
        value.mul(scalar);
        return this;
    }

    @Override
    public RGBSpectrum add(RGBSpectrum s2) {
        value.add(s2.value);
        return this;
    }

    @Override
    public int toIntRGB(RadiometryAlgorithm alg) {
        switch (alg) {
            case ALGORITHM_MUL_ROUND:
                char a = 255;
                char r = (char)Math.min(value.x()*255, 255);
                char g = (char)Math.min(value.y()*255, 255);
                char b = (char)Math.min(value.z()*255, 255);
                return (a << 24) | (r << 16 ) | (g<<8) | b;

            default:
                return 0;
        }
    }

    @Override
    public RGBSpectrum clone() {
        return new RGBSpectrum(this);
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean isZero() {
        return Math.abs(value.lengthSquared()) < Constants.BIAS;
    }

    @Override
    public double maxComponent() {
        return value.maxComponent();
    }
}
