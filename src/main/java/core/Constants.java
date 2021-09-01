package core;

import org.joml.Matrix3d;
import org.joml.Vector3d;

public class Constants {
    public static final double BIAS = 0.00001;

    public static Vector3d cosineWeightedHemisphere(double u0, double u1, Matrix3d tangentSpaceToWorldSpace) {
        double r = Math.sqrt(u0);
        double theta = 2 * Math.PI * u1;

        double x = r * Math.cos(theta);
        double y = r * Math.sin(theta);



        return tangentSpaceToWorldSpace.transform(new Vector3d(x, y, Math.sqrt(Math.max(0, 1 - x * x - y * y)))).normalize();
    }
    public static double balanceHeuristic(int nf, double fpdf, int ng, double gpdf) {
        return (nf * fpdf) / ((nf * fpdf) + (ng * gpdf));
    }
    public static double powerHeuristic(int nf, double fpdf, int ng, double gpdf) {
        double f = nf * fpdf;
        double g = ng * gpdf;
        return (f * f) / ((f * f) + (g * g));
    }
}
