package a5;
/* NetId(s): zyt2, js2788

 * Name(s): Amanda Sun, Zin Yamin Tun
 * What I thought about this assignment:
 * This assignment is a good practice to visit and review recursion in different ways.
 * It was also good practice for us to see two common recursion algorithms with trees
 * as well as exploring these two topics together.
 */

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/** Important notes 1. Remember that a variable declared as: CovidTree ct; <br>
 * can be viewed in two ways. <br>
 *
 * (1) It is a node of a tree <br>
 * (2) It is the whole tree! More specifically, the root of the tree.
 *
 * At any point, we may talk about "node ct" or "tree ct" or "the tree with root ct". <br>
 * It all depends on what is important at this point.
 *
 * 2. For this particular class and assignment, we also use the following terminology. <br>
 * Let p be a Human. When we talk of
 *
 * . . . . "CovidTree p",
 *
 * we mean the CovidTree node that has p in field human. <br>
 * This saves a lot of verbiage; it shortens explanations and specs. <br>
 * It's OK to use this abbreviation because, in this project, no human <br>
 * can appear more than once in a CovidTree.
 *
 * An instance of CovidTree represents the spread of Covid among a Network of people. <br>
 * In this model, each human can "catch" Covid from only a single human. <br>
 * The root of the CovidTree is the human who first got Covid. <br>
 * From there, each human in the CovidTree is the child of the human who gave <br>
 * them Covid. For example, for the tree:
 *
 * <pre>
 *       A
 *      / \
 *     B   C
 *        / \
 *       D   E
 * </pre>
 *
 * Human A originally got Covid, B and C caught Covid from A, and<br>
 * D and E caught Covid from C.
 *
 * Important note: Each human in the Covid tree is unique. <br>
 * Check whether two Humans are the same by calling method equals. */
public class CovidTree {

    /** Replace "-1" by the time you spent on A5 in hours.<br>
     * Example: for 3 hours 15 minutes, use 3.25<br>
     * Example: for 4 hours 30 minutes, use 4.50<br>
     * Example: for 5 hours, use 5 or 5.0 */
    public static double timeSpent= 5.0;

    /** The String to be used as a separator in toString() */
    public static final String SEPARATOR= " - ";

    /** The String that marks the start of children in toString() */
    public static final String START_CHILDREN_DELIMITER= "[";

    /** The String that divides children in toString() */
    public static final String DELIMITER= ", ";

    /** The String that marks the end of children in toString() */
    public static final String END_CHILDREN_DELIMITER= "]";

    /** The String that is the space increment in toStringVerbose() */
    public static final String VERBOSE_SPACE_INCREMENT= "\t";

    /** The Human at the root of this CovidTree. <br>
     * i.e. the human in this node of this CovidTree. <br>
     * This field is never null.<br>
     * This is the Covid ancestor of everyone in this CovidTree, that is,<br>
     * the human who got ill first and indirectly caused everyone in it to get ill. <br>
     * All nodes of a CovidTree have different Humans in them. There are no duplicates */
    private Human human;

    /** The children of this CovidTree node. <br>
     * Each element of this set got Covid from the human at this node. <br>
     * It is the empty set if this node is a leaf. For each child,<br>
     * its field human is not null. */
    private Set<CovidTree> children;

    /** Constructor: a new CovidTree with root Human p and no children. <br>
     * Throw an IllegalArgumentException if p is null. */
    public CovidTree(Human p) throws IllegalArgumentException {
        if (p == null)
            throw new IllegalArgumentException("Can't construct CovidTree without a Human");
        human= p;
        children= new HashSet<>();
    }

    /** Constructor: a new CovidTree that is a copy of tree t. <br>
     * Tree t and its copy have no node in common (but nodes can share a Human). <br>
     * Throw an IllegalArgumentException if t is null. */
    public CovidTree(CovidTree t) throws IllegalArgumentException {
        if (t == null)
            throw new IllegalArgumentException("Can't construct copy of null CovidTree");
        human= t.human;
        children= new HashSet<>();

        for (CovidTree dt : t.children) {
            children.add(new CovidTree(dt));
        }
    }

    /** = the human at the root of this CovidTree. */
    public Human human() {
        return human;
    }

    /** = the number of direct children of this CovidTree. */
    public int childrenSize() {
        return children.size();
    }

    /** = the CovidTree that has p in its root. <br>
     * (null if p is not in this tree). */
    public CovidTree node(Human p) {
        if (human == p) return this; // Base case

        // Recursive case - Return the node with human p if a child contains it.
        for (CovidTree dt : children) {
            var node= dt.node(p);
            if (node != null) return node;
        }

        return null; // p is not in the tree
    }

    /** Give p a new child, a node with Human c. <br>
     * Return the newly created node. <br>
     * Throw an IllegalArgumentException if:<br>
     * -- p or c is null, or<br>
     * -- p is not in this CovidTree, or <br>
     * -- c is already in this CovidTree. */
    public CovidTree insert(Human p, Human c) throws IllegalArgumentException {
        // TODO 1
        // This method should not be recursive.
        // Use method node(), above, and use no methods that are below.
        // DO NOT traverse the tree twice looking for the same node
        // --don't duplicate work.
        CovidTree np= node(p);
        if (p == null || c == null || np == null || node(c) != null) {
            throw new IllegalArgumentException();
        }
        CovidTree nc= new CovidTree(c);
        np.children.add(nc);
        return nc;
    }

    /** = the number of nodes in this CovidTree. <br>
     * Note: If this is a leaf, the size is 1 (just the root) */
    public int size() {
        // TODO 2. This method must be recursive.
        // State whether this is a searching or a counting method: counting
        if (children.size() == 0) return 1;
        int sum= 1;
        for (CovidTree h : children) {
            sum+= h.size();
        }
        return sum;
    }

    /** = "this Covid tree contains a node with human p." */
    public boolean contains(Human p) {
        // TODO 3
        // Note: This may be the first method you write that may test whether two
        // Humans r and p are equal. For all such tests, always use function equals:
        //
        // r.equals(p)
        //
        // If you write this recursively,
        // state whether this is a searching or a counting method: searching
        //
        // Recursive definition: This CovidTree contains p iff the human in this
        // CovidTree node is p or if one of this node's children contains p.
        if (human == p) return true;
        for (CovidTree h : children) {
            if (h.contains(p)) { return true; }
        }
        return false;
    }

    /** = the depth at which p occurs in this CovidTree, or <br>
     * -1 if p is not in the CovidTree. <br>
     * Note: depth(root) is 0. <br>
     * If p is a child of this CovidTree node, then depth(p) is 1. etc. <br>
     * Note: if p is null, it is obviously NOT in the tree, but DO NOT<br>
     * put in a special test for it; it would be unnecessary case analysis. */
    public int depth(Human p) {
        // TODO 4
        // Note: Do NOT call function contains(p) to find out whether p is in
        // the tree. Do not have unnecessary calls that traverse the tree.
        // Study the two tutorials on processing recursive calls and use the
        // correct pattern here.
        // State whether this is a searching or a counting method: searching

        // Here is recursive insight:
        // ... Let child c of the root contain p.
        // ... Then the depth of p in the root = (1 + depth of p in c)
        if (human == p) return 0;
        for (CovidTree c : children) {
            int dep= c.depth(p);
            if (dep >= 0) return 1 + dep;
        }
        return -1;
    }

    /** Return the width of this tree at depth d <br>
     * (i.e. the number of nodes that occur at depth d).<br>
     * Throw an IllegalArgumentException if d < 0.<br>
     * Thus, for the following tree:<br>
     *
     * <pre>
      * Depth  level:
     *  0         A
     *           / \
     *  1       B   C
     *         /   / \
     *  2     D   E   F
     *             \
     *  3           G
     * </pre>
     *
     * A.widthAtDepth(0) = 1, A.widthAtDepth(1) = 2,<br>
     * A.widthAtDepth(2) = 3, A.widthAtDepth(3) = 1, A.widthAtDepth(4) = 0.<br>
     * C.widthAtDepth(0) = 1, C.widthAtDepth(1) = 2. */
    public int widthAtDepth(int d) throws IllegalArgumentException {
        // TODO 5
        // It may help to read Ed Discussions note #709, Stepwise refinement, when
        // developing widthAtDepth. Do not have calls that unnecessarily traverse
        // the tree, causing the tree to be traversed more than once.

        // State whether this is a searching or a counting method: counting

        // Use this recursive definition:
        // ..... If d = 0, the answer is 1.
        // ..... If d > 0, the answer is: sum of widths of the children at depth d-1.
        if (d < 0) throw new IllegalArgumentException();
        if (d == 0) return 1;
        int sum= 0;
        for (CovidTree c : children) {
            sum+= c.widthAtDepth(d - 1);
        }
        return sum;
    }

    /** Return the route Covid took to get from "here" (the root of <br>
     * this CovidTree) to descendent c. If there is no such route, return null.<br>
     * For example, for this tree:<br>
     *
     * <pre>
     * Depth:
     *    0           A
     *               / \
     *    1         B   C
     *             /   / \
     *    2       D   E   F
     *             \
     *    3         G
     * </pre>
     *
     * A.CovidRouteTo(E) returns the list [A,C,E].<br>
     * A.CovidRouteTo(A) returns the list [A]. <br>
     * A.CovidRouteTo(X) returns null.<br>
     * B.CovidRouteTo(C) returns null. <br>
     * B.CovidRouteTo(D) returns the list [B,D] */
    public List<Human> CovidRouteTo(Human c) {
        // TODO 6
        // 0. Using getParent will give you 0 for this method. It is too inefficient,
        // requiring possibly many traversals of the tree.
        //
        // 1. The method must return a List<Human> object. But List is an interface, so
        // use something that implements it. LinkedList<Human> is preferred to ArrayList<Human>,
        // because prepend (or its equivalent) may have to be used.

        // 2. The ONLY case in which this method must create a list object (using
        // a new expression new ArrayList<>() or new LinkedList<>()) is the base
        // case, when c is the root. Do NOT create a new list object using a new-expression
        // in the recursive case.

        // 3. If c is null, there is no route to it, so null should be returned. But do NOT
        // put in a special test for this case; it is unnecessary case analysis.

        // 4. Base Case: The root of this CovidTree is c; i.e. the Route is just [c].

        // State whether this is a searching or a counting method: searching
        if (c == human) {
            LinkedList<Human> ll= new LinkedList<>();
            ll.addFirst(c);
            return ll;
        }
        for (CovidTree ch : children) {
            List<Human> v= ch.CovidRouteTo(c);
            if (v != null) {
                v.add(0, human);
                return v;
            }
        }
        return null;
    }

    /** If either child1 or child2 is null or is not in this CovidTree, return null.<br>
     * Otherwise, return the human at the root of the smallest subtree of this<br>
     * CovidTree that contains child1 and child2.<br>
     *
     * Examples. For the following tree (which does not contain H):
     *
     * <pre>
     * Depth:
     *    0      A
     *          / \
     *    1    B   C
     *        /   / \
     *    2  D   E   F
     *        \
     *    3    G
     * </pre>
     *
     * A.commonAncestor(B, A) is A<br>
     * A.commonAncestor(B, B) is B<br>
     * A.commonAncestor(B, C) is A<br>
     * A.commonAncestor(A, C) is A<br>
     * A.commonAncestor(E, F) is C<br>
     * A.commonAncestor(G, F) is A<br>
     * B.commonAncestor(B, E) is null<br>
     * B.commonAncestor(B, A) is null<br>
     * B.commonAncestor(D, F) is null<br>
     * B.commonAncestor(D, H) is null<br>
     * A.commonAncestor(null, C) is null */
    public Human commonAncestor(Human child1, Human child2) {
        // TODO 7
        /* Using getParent will give you 0 for this method. Its use over and over
         * is inefficient.
         *
         * Instead, find the Covid routes l1 and l2 to the two children.
         * If both are not null, then two things are known:
         * (1) l1[0] = l2[0]
         * (2) the answer l1[i] is the largest i such that l1[0..i] = l2[0..i].
         * If this is not clear, draw an example.
         * The answer, then, can be found using a loop. No recursion is needed.
         *
         * You have a problem of writing this loop efficiently. You can't use a foreach loop
         * on both lists simultaneously. The simplest thing to do is to use List's
         * function toArray and work with the array representations of the lists. */
        List<Human> l1= CovidRouteTo(child1);
        List<Human> l2= CovidRouteTo(child2);
        if (l1 == null || l2 == null) return null;
        var h1= l1.toArray();
        var h2= l2.toArray();
        for (int k= 0; k < h1.length && k < h2.length; k++ ) {
            if (h1[0] == h2[0]) {
                if (h1[k] == h2[k] && (k == h1.length - 1 || k == h2.length - 1)) {
                    return (Human) h1[k];
                }
                if (h1[k] != h2[k]) { return (Human) h1[k - 1]; }
            }
        }
        return null;
    }

    /** Return true iff this is equal to ob.<br>
     * 1. If this and ob are not of the same class, they are not equal, so return false.<br>
     * 2. Two CovidTrees are equal if<br>
     * -- (1) they have the same Human in their root object (use ==) AND<br>
     * -- (2) their children sets are the same size AND<br>
     * -- (3) their children sets are equal.<br>
     * ------ Let the two children sets be s1 and s2. <br>
     * ------ Since their sizes are equal, this requires:<br>
     * -------- for every CovidTree t1 in s1 there is a CovidTree<br>
     * -------- t2 in s2 for which t1.equals(t2) is true.<br>
     *
     * -- Otherwise the two CovidTrees are not equal.<br>
     * Do not use any of the toString functions to write equals(). <br>
     * Do not use Set's functions equals, contains, or containsAll. */
    @Override
    public boolean equals(Object ob) {
        // TODO 8
        // The specification outlines what must be done in detail. Two points to discuss:
        // (1) Stay away from nested ifs as much as possible! Instead, as soon as it is
        // determined that a property for equality is not met (or is met), return false (or true)!
        // So the structure could be:

        // if (property 1 not met) return false;
        // if (property 2 not met) return false;
        // etc.

        // (2) The difficult part is testing this:
        // ... for every CovidTree t1 in s1, there is a CovidTree t2
        // ... in s2 for which t1.equals(t2) is true.
        //
        // We suggest you write a helper method with this specification:
        // /** Return true iff t1 equals some member of s2 */
        // private boolean isInSet(CovidTree t1, Set<CovidTree> s2)
        //
        // Note that this method is going to call equals, like this: s2.equals(...).
        // That is a call to the method you are trying to write! We have
        // mutual recursion: equals calls help, which calls equals, which call help ...
        // But when thinking about what a call does, USE THE SPECIFICATION to understand
        // what it does.
        //
        // Hint about checking whether each child of one tree equals SOME
        // tree of the other tree's children.
        // First, you have to check them all until you find an equal one (or
        // return false if you don't.)
        // Second, A child of one tree cannot equal more than one child of
        // tree because the names of Human's are all unique; there are no duplicates.
        if (ob == null || ob.getClass() != getClass()) return false;
        CovidTree ct= (CovidTree) ob;
        if (human != ct.human) return false;
        if (childrenSize() != ct.childrenSize()) return false;
        for (CovidTree ch : children) {
            if (!isInSet(ch, ct.children)) return false;
        }
        return true;
    }

    /** Return true iff t is in s. */
    public static boolean isInSet(CovidTree t, Set<CovidTree> s) {
        // You don't have to write this method if you don't want to use it.
        for (CovidTree ch : s) {
            if (t.equals(ch)) return true;
        }
        return false;
    }

    /* ========================================================================
     * ========================================================================
     * ========================================================================
     * Do not use the methods written below. They are used to calculate data
     * for the GUI and in the grading program. Feel free to read/study them. */

    /** = a COPY of the set of children of this CovidTree. */
    public Set<CovidTree> copyOfChildren() {
        return new HashSet<>(children);
    }

    /** Return the maximum depth of this CovidTree, <br>
     * i.e. the longest path from the root to a leaf.<br>
     * Example. If this CovidTree is a leaf, return 0. */
    public int maxDepth() {
        var maxDepth= 0;
        for (CovidTree dt : children) {
            maxDepth= Math.max(maxDepth, dt.maxDepth() + 1);
        }
        return maxDepth;
    }

    /** Return the immediate parent of c (null if c is not in this CovidTree).<br>
     * Thus, for the following tree:
     *
     * <pre>
     * Depth:
     *    0      A
     *          / \
     *    1    B   C
     *        /   / \
     *    2  D   E   F
     *        \
     *    3    G
     * </pre>
     *
     * A.getParent(E) returns C.<br>
     * C.getParent(E) returns C.<br>
     * A.getParent(B) returns A.<br>
     * E.getParent(F) returns null. */
    public Human getParent(Human c) {
        // Base case
        for (CovidTree dt : children) {
            if (dt.human == c) return human;
        }

        // Recursive case - ask children to look
        for (CovidTree dt : children) {
            var parent= dt.getParent(c);
            if (parent != null) return parent;
        }

        return null; // Not found
    }

    /** Return the maximum width of all the widths in this tree, <br>
     * i.e. the maximum value that could be returned from widthAtDepth for this tree. */
    public int maxWidth() {
        return maxWidthImplementationTwo(this);
    }

    /** Simple implementation of maxWidth. <br>
     * Relies on widthAtDepth. <br>
     * Takes time proportional to the square of the size of the t. */
    static int maxWidthImplementationOne(CovidTree t) {
        var width= 0;
        var depth= t.maxDepth();
        for (var i= 0; i <= depth; i++ ) {
            width= Math.max(width, t.widthAtDepth(i));
        }
        return width;
    }

    /** Better implementation of maxWidth. Caches results in an array. <br>
     * Takes time proportional to the size of t. */
    static int maxWidthImplementationTwo(CovidTree t) {
        // For each integer d, 0 <= d <= maximum depth of t, store in
        // widths[d] the number of nodes at depth d in t.
        // The calculation is done by calling recursive procedure addToWidths.
        var widths= new int[t.maxDepth() + 1];   // initially, contains 0's
        t.addToWidths(0, widths);

        var max= 0;
        for (int width : widths) {
            max= Math.max(max, width);
        }
        return max;
    }

    /** For each node of this CovidTree, which is at some depth d in this CovidTree,<br>
     * add 1 to widths[depth + d]. */
    private void addToWidths(int depth, int[] widths) {
        widths[depth]++ ;        // the root of this CovidTree is at depth d = 0
        for (CovidTree dt : children) {
            dt.addToWidths(depth + 1, widths);
        }
    }

    /** Better implementation of maxWidth. Caches results in a HashMap. <br>
     * Takes time proportional to the size of t. */
    static int maxWidthImplementationThree(CovidTree t) {
        // For each possible depth d >= 0 in tree t, widthMap will contain the
        // entry (d, number of nodes at depth d in t). The calculation is
        // done using recursive procedure addToWidthMap.

        // For each integer d, 0 <= d <= maximum depth of t, add to
        // widthMap an entry <d, 0>.
        var widthMap= new HashMap<Integer, Integer>();
        for (var d= 0; d <= t.maxDepth() + 1; d++ ) {
            widthMap.put(d, 0);
        }

        t.addToWidthMap(0, widthMap);

        var max= 0;
        for (Integer w : widthMap.values()) {
            max= Math.max(max, w);
        }
        return max;
    }

    /** For each node of this CovidTree, which is at some depth d in this CovidTree,<br>
     * add 1 to the value part of entry <depth + d, ...> of widthMap. */
    private void addToWidthMap(int depth, HashMap<Integer, Integer> widthMap) {
        widthMap.put(depth, widthMap.get(depth) + 1);  // the root is at depth d = 0
        for (CovidTree dt : children) {
            dt.addToWidthMap(depth + 1, widthMap);
        }
    }

    /** Return a (single line) String representation of this CovidTree.<br>
     * If this CovidTree has no children (it is a leaf), return the root's substring.<br>
     * Otherwise, return<br>
     * ... root's substring + SEPARATOR + START_CHILDREN_DELIMITER + each child's<br>
     * ... toString, separated by DELIMITER, followed by END_CHILD_DELIMITER.<br>
     *
     * Make sure there is not an extra DELIMITER following the last child.<br>
     *
     * Finally, make sure to use the static final fields declared at the top of<br>
     * CovidTree.java.<br>
     *
     * Thus, for the following tree:
     *
     * <pre>
     * Depth level:
     *   0         A
     *            / \
     *   1        B  C
     *           /  / \
     *   2      D  E   F
     *           \
     *   3        G
     *
     * A.toString() should print:
     * (A) - HEALTHY - [(C) - HEALTHY - [(F) - HEALTHY, (E) - HEALTHY - [(G) - HEALTHY]], (B) - HEALTHY - [(D) - HEALTHY]]
     *
     * C.toString() should print:
     * (C) - HEALTHY - [(F) - HEALTHY, (E) - HEALTHY - [(G) - HEALTHY]]
     * </pre>
     */
    @Override
    public String toString() {
        if (children.isEmpty()) return human.toString();
        var s= human.toString() + SEPARATOR + START_CHILDREN_DELIMITER;
        for (CovidTree dt : children) {
            s= s + dt.toString() + DELIMITER;
        }
        return s.substring(0, s.length() - 2) + END_CHILDREN_DELIMITER;
    }

    /** Return a verbose (multi-line) string representing this CovidTree. */
    public String toStringVerbose() {
        return toStringVerbose(0);
    }

    /** Return a verbose (multi-line) string representing this CovidTree.<br>
     * Each human in the tree is on its own line, with indentation representing<br>
     * what each human is a child of.<br>
     * indent is the the amount of indentation to put before this line.<br>
     * Should increase on recursive calls to children to create the above pattern.<br>
     * Thus, for the following tree:
     *
     * <pre>
     * Depth level:
     *   0         A
     *            / \
     *   1       B   C
     *          /   / \
     *   2     D   E   F
     *          \
     *   3       G
     *
     * A.toStringVerbose(0) should return:
     * (A) - HEALTHY
     * (C) - HEALTHY
     * (F) - HEALTHY
     * (E) - HEALTHY
     * (G) - HEALTHY
     * (B) - HEALTHY
     * (D) - HEALTHY
     * </pre>
     *
     * Make sure to use VERBOSE_SPACE_INCREMENT for indentation. */
    private String toStringVerbose(int indent) {
        var s= "";
        for (var i= 0; i < indent; i++ ) {
            s= s + VERBOSE_SPACE_INCREMENT;
        }
        s= s + human.toString();

        if (children.isEmpty()) return s;

        for (CovidTree dt : children) {
            s= s + "\n" + dt.toStringVerbose(indent + 1);
        }
        return s;
    }

}
