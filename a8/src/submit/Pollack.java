package submit;

import java.util.HashSet;
import java.util.List;

import graph.FindState;
import graph.Finder;
import graph.FleeState;
import graph.Node;
import graph.NodeStatus;

/** A solution with find-the-Orb optimized and flee getting out as fast as possible. */
public class Pollack extends Finder {

    /** Get to the orb in as few steps as possible. <br>
     * Once you get there, you must return from the function in order to pick it up. <br>
     * If you continue to move after finding the orb rather than returning, it will not count.<br>
     * If you return from this function while not standing on top of the orb, it will count as <br>
     * a failure.
     *
     * There is no limit to how many steps you can take, but you will receive<br>
     * a score bonus multiplier for finding the orb in fewer steps.
     *
     * At every step, you know only your current tile's ID and the ID of all<br>
     * open neighbor tiles, as well as the distance to the orb at each of <br>
     * these tiles (ignoring walls and obstacles).
     *
     * In order to get information about the current state, use functions<br>
     * currentLoc(), neighbors(), and distanceToOrb() in FindState.<br>
     * You know you are standing on the orb when distanceToOrb() is 0.
     *
     * Use function moveTo(long id) in FindState to move to a neighboring<br>
     * tile by its ID. Doing this will change state to reflect your new position.
     *
     * A suggested first implementation that will always find the orb, but <br>
     * likely won't receive a large bonus multiplier, is a depth-first search. <br>
     * Some modification is necessary to make the search better, in general. */

    /** Set of the nodes' id that have been visited */
    private HashSet<Object> visit= new HashSet<>();

    @Override
    public void find(FindState state) {
        // TODO 1: Walk to the orb
        // check whether or not find orb
        if (fasterWalk(state)) {}
    }

    /** Return true if the walker found the orb otherwise return false <br>
     * Basic dfsWalk method by traversing every unvisited node until orb is found. <br>
     * The walker is standing on Node with id u given by FindState state <br>
     * Visit every node reachable along paths of unvisited nodes from node u or<br>
     * until the walker find the orb End with the walker standing on Node u<br>
     * Precondition: Node with id u is unvisited. */
    private boolean dfsWalk(FindState state) {
        long u= state.currentLoc();
        visit.add(u);
        if (state.distanceToOrb() == 0) { return true; }
        for (NodeStatus w : state.neighbors()) {
            long w_id= w.getId();
            if (!visit.contains(w_id)) {
                state.moveTo(w_id);
                if (dfsWalk(state)) return true;
                state.moveTo(u);
            }
        }
        return false;
    }

    /** Return true if the walker found the orb otherwise return false <br>
     * Optimized walk that minimizes number of traversing nodes through polling a <br>
     * min-heap containing all neighbors and their distance to orb to achieve higher bonus. <br>
     * The walker is standing on Node with id u given by FindState state <br>
     * If a node not visited, add to the visit HashSet <br>
     * Create a min-heap giving each element priority based on the distance to <br>
     * the orb and visit each node in min-heap according to the priority <br>
     * Precondition: Node with id u is unvisited. */
    private boolean fasterWalk(FindState state) {
        long u= state.currentLoc();
        visit.add(u);
        if (state.distanceToOrb() == 0) { return true; }
        Heap pathHeap= heapId(state);
        while (pathHeap.size() > 0) {
            NodeStatus w= (NodeStatus) pathHeap.poll();
            long w_id= w.getId();
            if (!visit.contains(w_id)) {
                state.moveTo(w_id);
                if (fasterWalk(state)) return true;
                state.moveTo(u);
            }
        }
        return false;
    }

    /** Return a min-heap with each element being the neighbors of the current <br>
     * node and priority based on each neighbor's distance to the orb. */
    private Heap heapId(FindState state) {
        var h= new Heap<NodeStatus>(true);
        for (NodeStatus w : state.neighbors()) {
            h.insert(w, w.getDistanceToTarget());
        }
        return h;
    }

    /** Get out the cavern before the ceiling collapses, trying to collect as <br>
     * much gold as possible along the way. Your solution must ALWAYS get out <br>
     * before steps runs out, and this should be prioritized above collecting gold.
     *
     * You now have access to the entire underlying graph, which can be accessed <br>
     * through FleeState state. <br>
     * currentNode() and exit() will return Node objects of interest, and <br>
     * allsNodes() will return a collection of all nodes on the graph.
     *
     * Note that the cavern will collapse in the number of steps given by <br>
     * stepsLeft(), and for each step this number is decremented by the <br>
     * weight of the edge taken. <br>
     * Use stepsLeft() to get the steps still remaining, and <br>
     * moveTo() to move to a destination node adjacent to your current node.
     *
     * You must return from this function while standing at the exit. <br>
     * Failing to do so before steps runs out or returning from the wrong <br>
     * location will be considered a failed run.
     *
     * You will always have enough steps to flee using the shortest path from the <br>
     * starting position to the exit, although this will not collect much gold. <br>
     * For this reason, using Dijkstra's to plot the shortest path to the exit <br>
     * is a good starting solution
     *
     * Here's another hint. Whatever you do you will need to traverse a given path. It makes sense
     * to write a method to do this, perhaps with this specification:
     *
     * // Traverse the nodes in moveOut sequentially, starting at the node<br>
     * // pertaining to state <br>
     * // public void moveAlong(FleeState state, List<Node> moveOut) */
    @Override
    public void flee(FleeState state) {
        // TODO 2. Get out of the cavern in time, picking up as much gold as possible.
        if (optimizeFlee(state)) {}
    }

    /** Return true if the walker is able to get out before the steps runs out <br>
     * Basic flee method that exit as soon as possible with shorestPath regardless <br>
     * of amount of gold collected. <br>
     * The walker is standing on Node begin given by FleeState state <br>
     * Find the exit while there are steps left. <br>
     * Collects the gold along the way and reduce the steps by the weight of the path <br>
     * Precondition: Walker has enough steps to move from current node to exit <br>
     * using shortestPath. */
    public boolean basicFlee(FleeState state) {
        Node begin= state.currentNode();
        Node end= state.exit();
        List<Node> sPath= Path.shortestPath(begin, end);
        for (Node next : sPath) {
            if (begin != next) {
                state.moveTo(next);
                if (next == end) return state.stepsLeft() >= 0;
            }
        }
        return false;
    }

    /** Return true if the walker is able to get out before the steps runs out <br>
     * Optimized flee that collects as much gold as possible before exiting. <br>
     * The walker is standing on Node begin given by FleeState state <br>
     * Find the exit while there are steps left. <br>
     * Collects the gold along the way and reduce the steps by the weight of the path<br>
     * Create a max-heap with all traversable nodes from state, priority being <br>
     * the ratio of gold amount and distance to the node. <br>
     * Go to each node in max-heap if is able to come back within range of stepsLeft <br>
     * Otherwise, get out using shortestPath. <br>
     * Precondition: Walker has enough steps to move from current node to exit<br>
     * using shortestPath. */
    public boolean optimizeFlee(FleeState state) {
        Node end= state.exit();
        Heap<Node> max_h= new Heap<>(false);
        max_h= maxheap(state);
        while (max_h.size > 0) {
            max_h= new Heap<>(false);
            max_h= maxheap(state);
            Node nextNode= max_h.poll();
            double totdist= distTo(state.currentNode(), nextNode) + distTo(nextNode, state.exit());
            while (totdist > state.stepsLeft() && max_h.size > 0) {
                nextNode= max_h.poll();
            }
            if (totdist < state.stepsLeft()) moveToNode(state, nextNode);
        }
        if (state.currentNode() != end) moveToNode(state, end);
        return state.stepsLeft() >= 0;
    }

    /** Create a shortestPath given current state and end node. <br>
     * Move from current to the end node by looping through shortestPath. */
    private void moveToNode(FleeState state, Node end) {
        Node begin= state.currentNode();
        List<Node> path= Path.shortestPath(begin, end);
        for (Node next : path) {
            if (state.currentNode() != next) {
                state.moveTo(next);
            }
        }
    }

    /** Return distance of shortestPath between begin and end nodes. */
    private double distTo(Node begin, Node end) {
        List path= Path.shortestPath(begin, end);
        return Path.pathSum(path);
    }

    /** Return max-heap with all traversable nodes from state, priority being <br>
     * the ratio of gold amount and distance to the node. */
    private Heap<Node> maxheap(FleeState state) {
        Heap<Node> h= new Heap<>(false);
        double d;
        for (Node next : state.allNodes()) {
            d= distTo(state.currentNode(), next);
            if (d != 0 && distTo(next, state.exit()) != 0) {
                h.insert(next, next.getTile().gold() / d);
            }
        }
        h.insert(state.exit(), 0);
        return h;
    }

}
