package shading.lighting;

import core.Spectrum;
import org.joml.Vector3d;

public class LightSamplingResult<E extends Spectrum<E>> {
    public E lightOutput;
    public double pdf;
    public Vector3d resultDirection;
    public LightSamplingResult(double _pdf, Vector3d _resultDirection) {
        this.pdf = _pdf;
        this.resultDirection = _resultDirection;
    }
}
