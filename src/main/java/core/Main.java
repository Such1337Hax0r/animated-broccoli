package core;

import camera.PerspectiveCamera;
import geometry.Light;
import geometry.RenderObject;
import geometry.Scene;
import geometry.Sphere;
import org.joml.Vector3d;
import rendering.NaivePathTracerRenderer;
import rendering.SmartPathTracerRenderer;
import sampling.RandomSampler;
import sampling.Sampler;
import shading.Material;
import shading.RGBSpectrum;
import shading.reflection.factory.LambertianBxDFFactory;
import shading.texture.CheckerboardTexture2D;
import shading.texture.ConstantTexture;
import shading.texture.RGBImageTexture;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class Main {

    public static final int WIDTH = 1920;
    public static final int HEIGHT = 1080;
    public static final int MAX_DEPTH = 5;
    public static final int SAMPLES_PER_PIXEL = 50;

    public static void main(String[] Args) throws IOException {
        Camera camera = new PerspectiveCamera(WIDTH, HEIGHT, 60);

        RGBImageTexture tex = new RGBImageTexture(ImageIO.read(Main.class.getResourceAsStream("/world-map.gif")));

        Scene<RGBSpectrum> scene = new Scene<>();



        CheckerboardTexture2D<RGBSpectrum> checkerboardTexture = new CheckerboardTexture2D<>(tex, new ConstantTexture<>(new RGBSpectrum(0.9, 0, 0)), 10);
        LambertianBxDFFactory<RGBSpectrum> bxdf = new LambertianBxDFFactory<>(checkerboardTexture);
        Material<RGBSpectrum> material1 = new Material<>(bxdf);
        Sphere<RGBSpectrum> sphere = new Sphere<>(new Vector3d(0,0,-15), 5);
        scene.addObject(new RenderObject<>(material1, sphere));

        LambertianBxDFFactory<RGBSpectrum> bxdf2 = new LambertianBxDFFactory<>(new ConstantTexture<>(new RGBSpectrum(0, 0, 0)));
        Material<RGBSpectrum> material2 = new Material<>(bxdf2);
        Sphere<RGBSpectrum> sphere2 = new Sphere<>(new Vector3d(0, -20, -15), 15);
        Light<RGBSpectrum> light = new Light<>(new ConstantTexture<>(new RGBSpectrum(1, 1, 1)));
        scene.addObject(new RenderObject<>(material2, sphere2, light));

        Film<RGBSpectrum> naiveFilm = new Film<>(WIDTH, HEIGHT);
        Film<RGBSpectrum> smartFilm = new Film<>(WIDTH, HEIGHT);
        //Film<RGBSpectrum> debugFilm = new Film<>(WIDTH, HEIGHT);

        Sampler sampler = new RandomSampler();

        Renderer<RGBSpectrum> naive = new NaivePathTracerRenderer<>(MAX_DEPTH, SAMPLES_PER_PIXEL, sampler,                                                     new RGBSpectrum(), new RGBSpectrum(1, 1, 1));
        Renderer<RGBSpectrum> smart = new SmartPathTracerRenderer<>(MAX_DEPTH, SAMPLES_PER_PIXEL, sampler, false, new RGBSpectrum(), new RGBSpectrum(1, 1, 1));
        //Renderer<RGBSpectrum> debug = new CoordinateRenderer();
        //Renderer<RGBSpectrum> debug = new HitCoordinateRenderer();
        //Renderer<RGBSpectrum> debug = new CosThetaRenderer();
        //Renderer<RGBSpectrum> debug = new ResultDirectionRenderer();

        //smart.render(camera, smartFilm, scene);
        //naive.render(camera, naiveFilm, scene);
        //debug.render(camera, debugFilm, scene);
        ArrayList<Test> tests = new ArrayList<>();
        //tests.add(new Test(camera, scene, naive, "naivept", WIDTH, HEIGHT));
        tests.add(new Test(camera, scene, smart, "smartpt", WIDTH, HEIGHT));


        for (Test t : tests) {
            System.out.println("Beginning test: " + t.testName + " at " + new Date());
            long startTime = System.currentTimeMillis();
            t.run();
            long endTime = System.currentTimeMillis();
            System.out.println("Finished test: " + t.testName + " at " + new Date() + ". The test took: " + (endTime-startTime) + " milliseconds");
        }
        /*try {
            ImageIO.write(naiveFilm.toRenderedImage(Spectrum.RadiometryAlgorithm.ALGORITHM_MUL_ROUND), "png", new File("output_naive.png"));
            ImageIO.write(smartFilm.toRenderedImage(Spectrum.RadiometryAlgorithm.ALGORITHM_MUL_ROUND), "png", new File("output_smart.png"));
            //ImageIO.write(debugFilm.toRenderedImage(Spectrum.RadiometryAlgorithm.ALGORITHM_MUL_ROUND), "png", new File("output_debug.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
    static class Test {
        Camera cam;
        Scene<RGBSpectrum> scene;
        Renderer<RGBSpectrum> renderer;
        Film<RGBSpectrum> film;
        String testName;

        public Test(Camera _cam, Scene<RGBSpectrum> _scene, Renderer<RGBSpectrum> _renderer, String _testName, int width, int height) {
            this.cam = _cam;
            this.scene = _scene;
            this.renderer = _renderer;
            this.film = new Film<>(width, height);
            this.testName = _testName;
        }

        public void run() {
            renderer.render(cam, film, scene);
            try {
                ImageIO.write(film.toRenderedImage(Spectrum.RadiometryAlgorithm.ALGORITHM_MUL_ROUND), "png", new File("output_" + testName + ".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
