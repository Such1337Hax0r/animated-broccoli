package rendering;

import core.*;
import geometry.RenderObject;
import geometry.Scene;
import org.joml.Vector3d;
import sampling.Sampler;
import shading.reflection.BxDFResult;
import shading.Material;
/*
 * This class implements a bare minimum path tracing algorithm
 * This is basically useless for any _real_ rendering workloads
 * But this is useful to generate ground-truth images for comparing other algorithms, as well as as a worst-case comparison
 */

public class NaivePathTracerRenderer<E extends Spectrum<E>> extends SamplingRenderer<E> {
    final int maxDepth, samplesPerPixel;

    public NaivePathTracerRenderer(int _maxDepth, int _samplesPerPixel, Sampler _sampler, E _zero, E _one) {
        super(_zero, _one, _sampler);
        this.maxDepth = _maxDepth;
        this.samplesPerPixel = _samplesPerPixel;
    }

    @Override
    public E sample(Ray ray, Scene<E> scene, Sampler sampler, int x, int y) {
        E ret = zero.clone();
        for (int i = 0; i < samplesPerPixel; i++) {
            ret.add(tracePath(ray, scene));
        }
        ret.mul(1/(double)samplesPerPixel);
        return ret;
    }

    E tracePath(Ray ray, Intersectable<E> scene) {
        Ray currRay = ray;
        E ret = zero.clone();
        E throughput = one.clone();
        int depth = 0;
        while (true) {
            if (depth >= maxDepth) break;
            Intersection<E> info = scene.intersect(currRay);
            if (!info.didHit()) break;

            RenderObject<E> hitObject = info.getHitObject();

            Material<E> material = hitObject.getMaterial();

            Vector3d newDirection = randomVectorInHemisphere(info.getHitNormal());

            BxDFResult<E> result = material.getBxDF(info.getU(), info.getV(), info.getHitPosition(), info.getHitNormal(), info.getHitTangent()).f(newDirection, new Vector3d(currRay.getDirection()).mul(-1));

            double cosTheta = result.resultDirection.dot(info.getHitNormal());

            if (info.getHitObject().hasLight())
            ret.add(info.getHitObject().getLight().getEmittance(info.getU(), info.getV(), info.getHitPosition()).clone().mul(throughput));

            throughput.mul(result.spectrum).mul(cosTheta/result.pdf);










            depth++;
            currRay = new Ray(new Vector3d(info.getHitPosition()), result.resultDirection);

            //return material.getEmittance(info.getU(), info.getV(), info.getHitPosition()).clone().add(incomingLight.mul(result.spectrum).mul(cosTheta / result.pdf));
        }

        return ret;
    }


    /*E tracePath(Ray ray, Intersectable<E> scene, int depth, int x, int y) {
        if (depth >= maxDepth) return zero.clone();

        Intersection<E> info = scene.intersect(ray);
        if (!info.didHit()) return zero.clone();

        Material<E> material = info.getHitMaterial();

        Vector3d newDirection = randomVectorInHemisphere(info.getHitNormal());

        Vector3d newDir = new Vector3d(ray.getDirection().z, ray.getDirection().x, ray.getDirection().y);

        Vector3d tangent = new Vector3d(info.getHitNormal()).cross(newDir);

        BxDFResult<E> result = material.getBxDF(info.getU(), info.getV(), info.getHitPosition(), info.getHitNormal(), tangent).f(newDirection, new Vector3d(ray.getDirection()).mul(-1));

        Ray newRay = new Ray(new Vector3d(info.getHitPosition()), result.resultDirection);
        E incomingLight = tracePath(newRay, scene, depth+1, x, y);

        double cosTheta = info.getHitNormal().dot(result.resultDirection);

        //System.out.println(cosTheta/result.pdf);

        return material.getEmittance(info.getU(), info.getV(), info.getHitPosition()).clone().add(incomingLight.mul(result.spectrum).mul(cosTheta / result.pdf));
    }*/


    Vector3d randomVectorInHemisphere(Vector3d normal) {
        double u = Math.random();
        double v = Math.random();

        double theta = 2 * Math.PI * u;
        double phi = Math.acos(2 * v - 1);

        double cosTheta = Math.cos(theta);
        double sinTheta = Math.sin(theta);

        double cosPhi = Math.cos(phi);
        double sinPhi = Math.sin(phi);

        Vector3d ret = new Vector3d(cosTheta * sinPhi, sinTheta * sinPhi, cosPhi);

        if (ret.dot(normal) <= 0) {
            ret.mul(-1);
        }

        return ret;
    }

    /*
    inline TVector3 UniformSampleHemisphere (const TVector3& N)
{
    // Uniform point on sphere
    // from http://mathworld.wolfram.com/SpherePointPicking.html
    float u = RandomFloat();
    float v = RandomFloat();

    float theta = 2.0f * c_pi * u;
    float phi = acos(2.0f * v - 1.0f);

    float cosTheta = cos(theta);
    float sinTheta = sin(theta);
    float cosPhi = cos(phi);
    float sinPhi = sin(phi);

    TVector3 dir;
    dir[0] = cosTheta * sinPhi;
    dir[1] = sinTheta * sinPhi;
    dir[2] = cosPhi;

    // if our vector is facing the wrong way vs the normal, flip it!
    if (Dot(dir, N) <= 0.0f)
        dir *= -1.0f;

    return dir;
}
     */
}
