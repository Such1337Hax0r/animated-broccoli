package rendering;

import core.*;
import geometry.Light;
import geometry.RenderObject;
import geometry.Scene;
import org.joml.Vector3d;
import sampling.Sampler;
import shading.lighting.LightSamplingResult;
import shading.reflection.BxDF;
import shading.reflection.BxDFResult;
import shading.Material;

import java.util.EnumSet;
import java.util.Iterator;
/*
 * This class implements a passable path tracer
 * In theory, this should be a decent rendering algorithm implementation
 * This renderer is path tracing with:
 * - Russian roulette path termination
 * - Multiple importance sampling
 * - Direct light sampling
 */

//TODO: Multiple importance sampling, direct light sampling, russian roulette path termination
public class SmartPathTracerRenderer<E extends Spectrum<E>> extends SamplingRenderer<E> {
    final int maxDepth, samplesPerPixel;
    boolean sampleOneLightIfTrueOrSampleAllLightsIfFalse;

    public SmartPathTracerRenderer(int _maxDepth, int _samplesPerPixel, Sampler _sampler, boolean _sampleOneLightIfTrueOrSampleAllLightsIfFalse, E _zero, E _one) {
        super(_zero, _one, _sampler);
        this.maxDepth = _maxDepth;
        this.samplesPerPixel = _samplesPerPixel;
        this.sampleOneLightIfTrueOrSampleAllLightsIfFalse = _sampleOneLightIfTrueOrSampleAllLightsIfFalse;
    }

    @Override
    public E sample(Ray ray, Scene<E> scene, Sampler sampler, int x, int y) {
        E ret = zero.clone();
        for (int i = 0; i < samplesPerPixel; i++) {
            ret.add(tracePath(ray, scene, sampler));
        }
        ret.mul(1/(double)samplesPerPixel);
        return ret;
    }

    enum BounceType {
        SPECULAR,
        DIFFUSE
    }

    E tracePath(Ray ray, Scene<E> scene, Sampler sampler) {
        Ray currRay = ray;
        E ret = zero.clone();
        E throughput = one.clone();
        int depth = 0;
        BounceType type = BounceType.SPECULAR;
        while (true) {
            Intersection<E> info = scene.intersect(currRay);
            if (!info.didHit()) break;

            RenderObject<E> hitObject = info.getHitObject();
            Material<E> material = hitObject.getMaterial();
            BxDF<E> bxdf = material.getBxDF(info.getU(), info.getV(), info.getHitPosition(), info.getHitNormal(), info.getHitTangent());
            BxDFResult<E> result = bxdf.sampleF(new Vector3d(currRay.getDirection()).mul(-1), sampler.get2d(0, 1));

            double cosTheta = result.resultDirection.dot(info.getHitNormal());

            if (type == BounceType.SPECULAR && info.getHitObject().hasLight()) {
                ret.add(info.getHitObject().getLight().getEmittance(info.getU(), info.getV(), info.getHitPosition()).clone().mul(throughput));
            }
            if (!bxdf.matchesFlag(BxDF.BxDFType.SPECULAR)){
                E li = estimateDirectLighting(scene, info.getHitNormal(), info.getHitPosition(), result.resultDirection, sampler, hitObject.getLight(), bxdf);
                //System.out.println(li);
                //if (li.isZero()) System.out.println("Bad");
                ret.add(li.mul(throughput));
            }

            throughput.mul(result.spectrum).mul(cosTheta/result.pdf);

            if (depth > maxDepth) {
                double p = throughput.maxComponent();
                if (sampler.get1d(0, 1) > p) {
                    break;
                }

                throughput.mul(1 / p);
            }

            depth++;
            currRay = new Ray(new Vector3d(info.getHitPosition()), result.resultDirection);
            if (bxdf.matchesFlags(EnumSet.of(BxDF.BxDFType.SPECULAR))) {
                type = BounceType.SPECULAR;
            } else {
                type = BounceType.DIFFUSE;
            }

            //return material.getEmittance(info.getU(), info.getV(), info.getHitPosition()).clone().add(incomingLight.mul(result.spectrum).mul(cosTheta / result.pdf)).mul(throughput);
        }

        return ret;
    }

    E estimateDirectLighting(Scene<E> scene, Vector3d hitNormal, Vector3d hitPosition, Vector3d wo, Sampler sampler, Light<E> currHitObj, BxDF<E> bxdf) {
        if (sampleOneLightIfTrueOrSampleAllLightsIfFalse) {
            return sampleOneLight(scene, hitNormal, hitPosition, wo, sampler, currHitObj, bxdf);
        } else {
            return   sampleLights(scene, hitNormal, hitPosition, wo, sampler, currHitObj, bxdf);
        }
    }

    E sampleOneLight(Scene<E> scene, Vector3d hitNormal, Vector3d hitPosition, Vector3d wo, Sampler sampler, Light<E> currHitObj, BxDF<E> bxdf) {
        int numLights = scene.numLights();
        if (numLights == 0 || (numLights == 1 && currHitObj != null)) {
            return zero.clone();
        }
        Light<E> light;
        do {
            light = scene.getLight(sampler.get1i(0, numLights-1));
        } while (light == currHitObj);
        return directLight(scene, hitNormal, hitPosition, wo, light, sampler, bxdf).mul(numLights);
    }

    E sampleLights(Scene<E> scene, Vector3d hitNormal, Vector3d hitPosition, Vector3d wo, Sampler sampler, Light<E> currHitObj, BxDF<E> bxdf) {
        E ret = zero.clone();
        for (Iterator<Light<E>>  iter = scene.getLightIterator(); iter.hasNext(); ) {
            Light<E> light = iter.next();
            if (light == currHitObj) {
                continue;
            }
            ret.add(directLight(scene, hitNormal, hitPosition, wo, light, sampler, bxdf));
        }
        return ret;
    }

    E directLight(Scene<E> scene, Vector3d hitNormal, Vector3d hitPosition, Vector3d wo, Light<E> light, Sampler sampler, BxDF<E> bxdf) {
        E ret = zero.clone();
        LightSamplingResult<E> result = light.sample(sampler, hitPosition, scene);
        if (result.pdf != 0) {
            BxDFResult<E> bxdfResult = bxdf.f(result.resultDirection, wo);
            double cosTheta = hitNormal.dot(result.resultDirection);
            if (cosTheta/result.pdf > 1) System.out.println(result.pdf);
            E li = result.lightOutput.clone().mul(bxdfResult.spectrum).mul(cosTheta/result.pdf);
            //System.out.println(li);
            ret.add(li);
        }

        return ret;
    }


/*    E directLight(Scene<E> scene, Vector3d hitPosition, Vector3d wo, Light<E> light, Sampler sampler, BxDF<E> bxdf) {
        E ret = zero.clone();
        //TODO: Figure out light sampling, replace "null" here with a BxDFResult from sampling the light
        //TODO: Light sampling result will have light output in "result.spectrum" rather than the reflection coefficient from evaluating the bxdf
        LightSamplingResult<E> result = light.sample(sampler, hitPosition, scene);

        /*
        //Check if this sample is occluded
        Intersection<E> info = scene.intersect(new Ray(new Vector3d(hitPosition), result.resultDirection));
        if (!info.didHit() || (info.didHit() && info.getHitObject().getLight() != light)) return zero.clone();

        if (result.pdf != 0 && !result.lightOutput.isZero()) {
            BxDFResult<E> f = bxdf.f(result.resultDirection, wo);
            if (f.pdf != 0 && !f.spectrum.isZero()) {
                double weight = Constants.balanceHeuristic(1, result.pdf, 1, f.pdf);
                //TODO: Check to make sure this shouldn't really be: "ret.add(result.lightOutput.clone().mul(f.spectrum).mul(weight));" instead
                ret.add(result.lightOutput.clone().mul(f.spectrum).mul(weight /* /result.pdf));
                //System.out.println(weight / result.pdf);
            }
        }

        BxDFResult<E> scatteringResult = bxdf.sampleF(wo, sampler.get2d(0, 1));
        if (scatteringResult.pdf != 0 && !scatteringResult.spectrum.isZero()) {
            //TODO: Figure out light sampling: lightPdf needs to be the probability of picking the current result.resultDirection by sampling the light
            LightSamplingResult<E> lightSamplingResult = light.evaluate(hitPosition, scatteringResult.resultDirection, scene);
            if (lightSamplingResult.pdf == 0) {
                return ret;
            }

            /*
            //Check if this sample is occluded
            info = scene.intersect(new Ray(new Vector3d(hitPosition), scatteringResult.resultDirection));
            if (!info.didHit() || (info.didHit() && info.getHitObject().getLight() != light)) return ret;


            double weight = Constants.balanceHeuristic(1, scatteringResult.pdf, 1, lightSamplingResult.pdf);
            //TODO: Check to make sure this shouldn't really be: "ret.add(lightSamplingResult.lightOutput.clone().mul(scatteringResult.spectrum).mul(weight))" instead
            ret.add(lightSamplingResult.lightOutput.clone().mul(scatteringResult.spectrum).mul(weight /* /scatteringResult.pdf));
        }
        return ret;
    }
 */

/*    E tracePath(Ray ray, Intersectable<E> scene, int depth) {
        if (depth > maxDepth) {
            double p =
            return zero.clone();
        }

        Intersection<E> info = scene.intersect(ray);
        if (!info.didHit()) return zero.clone();

        Material<E> material = info.getHitMaterial();

        Vector3d newDir = new Vector3d(ray.getDirection().z, ray.getDirection().x, ray.getDirection().y);

        Vector3d tangent = new Vector3d(info.getHitNormal()).cross(newDir);
        //Vector3d tangent = new Vector3d(info.getHitNormal()).cross(ray.getDirection());

        BxDFResult<E> result = material.getBxDF(info.getU(), info.getV(), info.getHitPosition(), info.getHitNormal(), tangent).sampleF(new Vector3d(ray.getDirection()).mul(-1), Math.random(), Math.random());

        Ray newRay = new Ray(new Vector3d(info.getHitPosition()), result.resultDirection);
        E incomingLight = tracePath(newRay, scene, depth+1);

        double cosTheta = result.resultDirection.dot(info.getHitNormal());

        return material.getEmittance(info.getU(), info.getV(), info.getHitPosition()).clone().add(incomingLight.mul(result.spectrum).mul(cosTheta / result.pdf)).mul(throughput);
    }*/
}