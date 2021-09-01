package core;

import geometry.Scene;
import sampling.Sampler;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public abstract class SamplingRenderer<E extends Spectrum<E>> extends Renderer<E> {
    public static final int TASK_SIZE = 16;
    Sampler sampler;

    public SamplingRenderer(E _zero, E _one, Sampler _sampler) {
        super(_zero, _one);
        this.sampler = _sampler;
    }

    //TODO: Implement some kind of parallelism here
    //TODO: Figure out the generics here as well
    @Override
    public void render(Camera cam, Film<E> film, Scene<E> scene) {
        ArrayList<Future<ArrayList<E>>> futures = new ArrayList<>();
        int numTasksX = (int)((film.getWidth()/(double)TASK_SIZE)+1);
        int numTasksY = (int)((film.getHeight()/(double)TASK_SIZE)+1);
        ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (int x = 0; x < numTasksX; x++) {
            for (int y = 0; y < numTasksY; y++) {
                int finalX = x;
                int finalY = y;
                futures.add(pool.submit(() -> renderFrame(finalX * TASK_SIZE, finalY * TASK_SIZE, TASK_SIZE, TASK_SIZE, cam, scene)));
            }
        }
        for (int taskX = 0; taskX < numTasksX; taskX++) {
            for (int taskY = 0; taskY < numTasksY; taskY++) {
                int futuresIdx = taskX*numTasksY + taskY;
                try {
                    ArrayList<E> samples = futures.get(futuresIdx).get();
                    for (int sampleX = 0; sampleX < TASK_SIZE; sampleX++) {
                        for (int sampleY = 0; sampleY < TASK_SIZE; sampleY++) {
                            int samplesIdx = sampleX*TASK_SIZE + sampleY;
                            E sample = samples.get(samplesIdx);
                            film.setPixel(taskX*TASK_SIZE+sampleX, taskY*TASK_SIZE+sampleY, sample);
                        }
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
            pool.shutdown();
        }
    }

    ArrayList<E> renderFrame(int startX, int startY, int width, int height, Camera cam, Scene<E> scene) {
        ArrayList<E> ret = new ArrayList<>(width*height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Ray ray = cam.genPrimaryRay(startX+x+Math.random()-0.5,startY+y+Math.random()-0.5);
                ret.add(sample(ray, scene, sampler, startX+x, startY+y));
            }
        }
        return ret;
    }

    public abstract E sample(Ray ray, Scene<E> scene, Sampler sampler, int x, int y);
}
