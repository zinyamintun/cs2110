package a5;

import java.util.Set;

import common.Util;
import common.types.Tuple1;

/** A instance represents a human and their health.
 *
 * @author Mshnik, revised by gries */
public final class Human extends Tuple1<String> {

    /** The possible Covid-related states of a human. */
    public enum State {  // The names indicate the state.
        HEALTHY,
        ILL,
        DEAD,
        IMMUNE
    }

    /** The network to this human belongs. */
    private final Network graph;

    /** Amount of health this human has. >= 0.<br>
     * 0 means dead, >0 means alive */
    private int health;

    /** State of this human. */
    private State state;

    /** Time step in which this human became ill (-1 if never been ill). */
    private int stepGotIll= -1;

    /** Time step in which this human became immune (-1 if not immune). */
    private int stepGotImmune= -1;

    /** Time step in which this human died (-1 if not dead). */
    private int stepDied= -1;

    /** Constructor: a healthy HUman with name n and health h, added to graph g. <br>
     * Precondition: The new human is not in g, and their name is distinct from the name of <br>
     * any other human in g. */
    public Human(String n, int h, Network g) {
        super(n);
        health= h;
        state= State.HEALTHY;
        graph= g;
        graph.addVertex(this);
    }

    /** Return a representation of this human. */
    public @Override String toString() {
        return super.toString() + " - " + state;
    }

    /** Return the name of this human. */
    public String name() {
        return _1;
    }

    /** Make this human ill during step currentStep. <br>
     * Throw a RuntimeException if this human is not HEALTHY. */
    public void getIll(int currentStep) {
        if (state != State.HEALTHY) {
            throw new RuntimeException(state + " human can't become ill");
        }
        state= State.ILL;
        stepGotIll= currentStep;
    }

    /** Make this human immune during step currentStep. <br>
     * Throw a RuntimeException if this human is immune or dead. */
    public void getImmune(int currentStep) {
        if (state == State.IMMUNE || state == State.DEAD) {
            throw new RuntimeException(state + " human can't become immune");
        }
        state= State.IMMUNE;
        stepGotImmune= currentStep;
    }

    /** Decrement the health of this human in step currentStep. <br>
     * If its health becomes 0, the human dies. <br>
     * Throw a RuntimeException if this human is not ill. */
    public void reduceHealth(int currentStep) {
        if (state != State.ILL) { throw new RuntimeException(state + " human can't lose health"); }
        health-- ;
        if (health == 0) {
            state= State.DEAD;
            stepDied= currentStep;
        }
    }

    /** = the state of this human. */
    public State state() {
        return state;
    }

    /** = "This human is alive". */
    public boolean isAlive() {
        return state != State.DEAD;
    }

    /** = "This human is dead". */
    public boolean isDead() {
        return !isAlive();
    }

    /** = "This human is healthy. */
    public boolean isHealthy() {
        return state == State.HEALTHY;
    }

    /** = "This human is immune". */
    public boolean isImmune() {
        return state == State.IMMUNE;
    }

    /** = "This human is ill". */
    public boolean isIll() {
        return state == State.ILL;
    }

    /** = the time step in which this human got ill" (-1 if never been ill). */
    public int frameGotIll() {
        return stepGotIll;
    }

    /** = the time step in which this human got immune" (-1 if not immune). */
    public int frameGotImmune() {
        return stepGotImmune;
    }

    /** = the time step in which this human died" (-1 if not dead). */
    public int frameDied() {
        return stepDied;
    }

    /** = the neighbors of this human. */
    public Set<Human> neighbors() {
        return graph.neighborsOf(this);
    }

    /** = a random neighbor of this human */
    public Human randomNeighbor() {
        return Util.randomElement(graph.neighborsOf(this));
    }
}
