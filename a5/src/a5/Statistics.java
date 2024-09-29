package a5;

/** An instance contains the probabilities that a human gets Covid<br>
 * and a human becomes immune in a time step.
 *
 * @author Mshnik, revised by Gries. */
public class Statistics {
    /** Probability of contagion. In range [0, 1]. */
    private double contagionChance;

    /** Probability of immunization. In range [0, 1]. */
    private double immunizationChance;

    /** Constructor: an instance with contagion probability cp and <br>
     * immunization probability ip. <br>
     * Precondition: 0 <= cp, ip <= 1. */
    public Statistics(double cp, double ip) {
        contagionChance= cp;
        immunizationChance= ip;
    }

    /** = (new random number) < (the probability of contagion). */
    public boolean CovidSpreadsToHuman() {
        return Math.random() < contagionChance;
    }

    /** = (new random number) < (the probability of becoming immune). */
    public boolean humanBecomesImmune() {
        return Math.random() < immunizationChance;
    }
}
