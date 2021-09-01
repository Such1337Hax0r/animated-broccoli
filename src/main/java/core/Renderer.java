package core;

import geometry.Scene;

public abstract class Renderer<E extends Spectrum<E>> {
    protected E zero;
    protected E one;

    public Renderer(E _zero, E _one) {
        this.zero = _zero;
        this.one = _one;
    }

    public abstract void render(Camera cam, Film<E> film, Scene<E> scene);
}
