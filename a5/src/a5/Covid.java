package a5;

import common.Util;
import common.types.Tuple;
import common.types.Tuple5;
import io.ScannerUtils;

/** An instance represents Covid spreading and ultimately dying out among<br>
 * a limited population (or killing everyone). <br>
 * <br>
 * Each Covid is created on a Network of human and with a chosen first patient. <br>
 * Covid is runnable, but for the purposes of this project, it does not need to <br>
 * be run on a separate thread.
 *
 * @author MPatashnik, revised by gries. */
public class Covid implements Runnable {
    /** The graph on which this Covid is running. */
    private Network network;

    /** The tree representing this Covid spreading. */
    private CovidTree tree;

    /** Number of time steps this Covid took to create dt. */
    private int steps;

    /** The Covid model: Statistics that determine the spread of the disease. */
    private Statistics statistics;

    /** How many chars to print per line in the running section. */
    private static final int RUNNING_CHAR_COUNT_MAX= 50;

    /** Used in printing the run progress. */
    private int runningCharCount= 7;

    /** Constructor: a new Covid on network nw with first patient fp and disease model s. */
    public Covid(Network nw, Human fp, Statistics s) {
        steps= 0;
        network= nw;
        fp.getIll(0);
        tree= new CovidTree(fp);
        statistics= s;
    }

    /** Run the disease until no ill people remain. Print out info about running. */
    public @Override void run() {
        System.out.print("Running");
        while (network.getPeopleOfType(Human.State.ILL).size() > 0) {
            step();
        }
        System.out.println("Done.\n");
    }

    /** Perform a single step on the disease, using disease model statistics. <br>
     * First, ill people may become immune with a certain probability. <br>
     * Second, ill people become less healthy by 1, and if their health reaches 0, they die.<br>
     * Third, ill people may spread the disease to one neighbor, with a certain probability. */
    private void step() {
        var people= network.vertexSet();
        System.out.print(".");
        runningCharCount++ ;
        if (runningCharCount > RUNNING_CHAR_COUNT_MAX) {
            System.out.print("\n");
        }

        // For each ill human, spread the disease to one random neighbor with a
        // certain probability.
        for (Human p : people) {
            if (p.isIll()) {
                var n= p.randomNeighbor();
                if (n != null && n.isHealthy() && statistics.CovidSpreadsToHuman()) {
                    n.getIll(steps);
                    tree.insert(p, n);
                }
            }
        }

        // For each ill human, make them immune with a certain probability
        for (Human p : people) {
            if (p.isIll() && statistics.humanBecomesImmune()) {
                p.getImmune(steps);
            }
        }

        // For each ill human, deduct 1 from health and make dead if health becomes 0
        for (Human p : people) {
            if (p.isIll()) {
                p.reduceHealth(steps);
            }
        }

        steps= steps + 1;
    }

    /** Read in the five statistic arguments from the console. <br>
     * Return a Tuple5, with the following components: <br>
     * - size: the number of people in the network <br>
     * - maxHealth: how much health each human starts with <br>
     * - connectionProbability: probability that two people are connected in the network <br>
     * - illnessProbability: probability that an ill human spreads the illness <br>
     *** to a neighbor in one time step <br>
     * - immunizationProbability: probability that an ill human becomes immune in one time step */
    private static Tuple5<Integer, Integer, Double, Double, Double> readArgs() {
        var scanner= ScannerUtils.defaultScanner();
        int size= ScannerUtils.get(Integer.class, scanner, "Enter the size of the population: ",
            "a positive non-zero integer", (i) -> i > 0);
        int maxHealth= ScannerUtils.get(Integer.class, scanner,
            "Enter the amount of health for each human: ",
            "a positive non-zero integer", (i) -> i > 0);
        double connectionProb= ScannerUtils.get(Double.class, scanner,
            "Enter the probability of a connection: ",
            "a double in the range [0,1]", (d) -> d >= 0 && d <= 1);
        double illnessProb= ScannerUtils.get(Double.class, scanner,
            "Enter the probability of becoming ill: ",
            "a double in the range [0,1]", (d) -> d >= 0 && d <= 1);
        double immunizationProb= ScannerUtils.get(Double.class, scanner,
            "Enter the probability of becoming immune: ",
            "a double in the range [0,1]", (d) -> d >= 0 && d <= 1);
        scanner.close();
        return Tuple.of(size, maxHealth, connectionProb, illnessProb, immunizationProb);
    }

    /** Run Covid on the arguments listed in args. <br>
     * If args doesn't match the pattern below, read in arguments via the console by using
     * readArgs().
     *
     * Then, call disease.run() and create an CovidFrame showing the created CovidTree.
     *
     * args should be an array of <br>
     *
     * [size, maxHealth, connection probability, illness probability, immunization probability],
     *
     * or unused (any value). If not used, the user is prompted for input in the console. */
    public static void main(String[] args) {
        // Get arguments
        var size= 10;
        var maxHealth= 5;
        var connectionProbability= 0.7;
        var illnessProbability= 0.5;
        var immunizationProbability= 0.1;

        try {
            // Attempt to read from args array passed in
            size= Integer.parseInt(args[0]);
            maxHealth= Integer.parseInt(args[1]);
            connectionProbability= Double.parseDouble(args[2]);
            illnessProbability= Double.parseDouble(args[3]);
            immunizationProbability= Double.parseDouble(args[4]);
        } catch (Exception e) {
            // If too few or wrong type, read from scanner
            var args2= readArgs();
            size= args2._1;
            maxHealth= args2._2;
            connectionProbability= args2._3;
            illnessProbability= args2._4;
            immunizationProbability= args2._5;
        }

        System.out.println("size: " + size);
        System.out.println("maxHealth: " + maxHealth);
        System.out.println("connectionProbability: " + connectionProbability);
        System.out.println("illnessProbability: " + illnessProbability);
        System.out.println("immunizationProbability: " + immunizationProbability);

        // Set defaults and create the Network, Statistics, and Covid objects
        System.out.print("\nSetting up ");
        System.out.print(".");
        var n= new Network(size, maxHealth, connectionProbability);
        System.out.print(".");
        var s= new Statistics(illnessProbability, immunizationProbability);
        System.out.print(".");
        var d= new Covid(n, Util.randomElement(n.vertexSet()), s);
        System.out.println("Done.");

        d.run();
        System.out.println(d.tree.toStringVerbose() + "\n");
        for (Human p : d.network.getPeopleOfType(Human.State.HEALTHY)) {
            System.out.println(p);
        }
        CovidJFrame.show(d.tree, d.steps);
    }
}
