package a5;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

/** @author david gries */
public class CovidTreeTest {
    /* DISCUSSION OF TESTING
     * Testing with trees is HARDER than testing in A1, A2, or even A3, with circular linked lists.
     *
     * We have provided some methods to help you test your methods that manipulate trees.
     *
     *1. TESTING METHOD SIZE. To do test size adequately, you have to create a
     * tree with lots of nodes and see whether size() returns the right value.
     *
     * METHOD makeTree1 CREATES A "LARGE" TREE!  USE IT TO TEST METHOD size()!
     *
     *
     * 2. TESTING METHOD CONTAINS.
     * Look directly below at the static fields. There is an array of type Human
     * and individual variables HumanA, ..., HumanL. Look below those declarations
     * at method setup. It has the annotation  @BeforeClass, which means that
     * it will be called before methods that have @Test before them are called.
     * Method setup initializes the fields just mentioned. Note this:
     *
     *     HumanA has name "A"
     *     HumanB has name "B"
     *     ...
     *     HumanL has name "L"
     *
     * Further, array humans contains the values (HumanA, HumanB, ..., HumanL).
     * You can use these in testing. For example, look at method makeTree1.
     * Its specification shows you the tree it constructs. For example, after
     * executing
     *
     *      CovidTree ct= makeTree1();
     *
     * you can test whether HumanA and HumanL are in it using
     *
     *      assertEquals(true, ct.contains(HumanA));
     *      assertEquals(false, ct.contains(HumanC));
     * */

    private static Network n;
    private static Human[] humans;
    private static Human humanA;
    private static Human humanB;
    private static Human humanC;
    private static Human humanD;
    private static Human humanE;
    private static Human humanF;
    private static Human humanG;
    private static Human humanH;
    private static Human humanI;
    private static Human humanJ;
    private static Human humanK;
    private static Human humanL;

    /** */
    @BeforeClass
    public static void setup() {
        n= new Network();
        humans= new Human[] { new Human("A", 0, n), new Human("B", 0, n),
                new Human("C", 0, n), new Human("D", 0, n),
                new Human("E", 0, n), new Human("F", 0, n),
                new Human("G", 0, n), new Human("H", 0, n),
                new Human("I", 0, n), new Human("J", 0, n),
                new Human("K", 0, n), new Human("L", 0, n)
        };
        humanA= humans[0];
        humanB= humans[1];
        humanC= humans[2];
        humanD= humans[3];
        humanE= humans[4];
        humanF= humans[5];
        humanG= humans[6];
        humanH= humans[7];
        humanI= humans[8];
        humanJ= humans[9];
        humanK= humans[10];
        humanL= humans[11];
    }

    /* TESTING METHOD DEPTH.
     * Take a look at the tree produced by method make1. We have:
     *    humanA is at depth 0
     *    humanB is at depth 1
     *    humanC is at depth 1
     *    humanD is at depth 2  and so on.
     *
     * Array people already contains humanA, humanB, humanC, ..., humanL
     * What if you constructed an array
     *
     *    var depths= new int[]{0, 1, 1, 2, ...}
     *
     * that contained the depth of each human in array people (including those
     * that are not in it)? You could then write a loop in method testContains
     * to test ALL of those people:
     *
     *    for (int k= 0; k < people.length; k= k+1) {
     *        assertEquals(depths[k], a suitable call on depth, for you to do);
     *    }
     *
     * This is the work that has to go on to do adequate testing
     */

    /** * */
    @Test
    public void testBuiltInGetters() {
        var st= new CovidTree(humanB);
        assertEquals("B", toStringBrief(st));
    }

    /** Create a CovidTree with structure A[B[D E F[G[H[I] J]]] C] <br>
     * This is the tree
     *
     * <pre>
     *            A
     *          /   \
     *         B     C
     *       / | \
     *      D  E  F
     *            |
     *            G
     *            | \
     *            H  J
     *            |
     *            I
     * </pre>
     */
    private CovidTree makeTree1() {
        var dt= new CovidTree(humanA); // A
        dt.insert(humanA, humanB); // A, B
        dt.insert(humanA, humanC); // A, C
        dt.insert(humanB, humanD); // B, D
        dt.insert(humanB, humanE); // B, E
        dt.insert(humanB, humanF); // B, F
        dt.insert(humanF, humanG); // F, G
        dt.insert(humanG, humanH); // G, H
        dt.insert(humanH, humanI); // H, I
        dt.insert(humanG, humanJ); // G, J
        return new CovidTree(dt);
    }

    /** test a call on makeTree1(). */
    @Test
    public void testMakeTree1() {
        var dt= makeTree1();
        assertEquals("A[B[D E F[G[H[I] J]]] C]", toStringBrief(dt));
    }

    /** */
    @Test
    public void test1Insert() {
        var st= new CovidTree(humanB);

        // Test insert in the root
        var dtC= st.insert(humanB, humanC);
        assertEquals("B[C]", toStringBrief(st)); // test tree
        assertEquals(humanC, dtC.human()); // test return value

        // Test insert in the non root
        var dtF= st.insert(humanC, humanF);
        assertEquals("B[C[F]]", toStringBrief(st));
        assertEquals(humanF, dtF.human());

        // Test insert in same root (same level)
        var dtH= st.insert(humanB, humanH);
        assertEquals("B[C[F] H]", toStringBrief(st));
        assertEquals(humanH, dtH.human());

        // Testing different tree
        var st1= new CovidTree(humanI);
        var dtE= st1.insert(humanI, humanE);
        assertEquals("I[E]", toStringBrief(st1));
        assertEquals(humanE, dtE.human());

        // Test insert for when p is null
        assertThrows(IllegalArgumentException.class, () -> st.insert(null, humanC));
        // Test insert for when c is null
        assertThrows(IllegalArgumentException.class, () -> st.insert(humanB, null));
        // Test insert for when p doesn't have Covid (not part of CovidTree)
        assertThrows(IllegalArgumentException.class, () -> st.insert(humanA, humanC));
        // Test insert for when c already have Covid
        var st2= new CovidTree(humanC);
        assertThrows(IllegalArgumentException.class, () -> st2.insert(humanB, humanC));
        // YOU SHOULD WRITE MORE TEST CASES HERE. We supply only one test case.

    }

    /** */
    @Test
    public void test2size() {
        /* We provide ONE test case. YOU WRITE MORE.
         * At this point, look at line 13 (about) for a discussion of making
         * a tree with which to test size. */

        var st= new CovidTree(humanC);
        assertEquals(1, st.size());

        // adding another human in CovidTree
        st.insert(humanC, humanB);
        assertEquals(2, st.size());

        // adding another human at same level in CovidTree
        st.insert(humanC, humanD);
        assertEquals(3, st.size());

        // adding 3rd children at same level
        st.insert(humanC, humanE);
        assertEquals(4, st.size());

        // children have children
        st.insert(humanB, humanG);
        assertEquals(5, st.size());

        st.insert(humanC, humanI);
        assertEquals(6, st.size());

        st.insert(humanC, humanL);
        assertEquals(7, st.size());

    }

    /** */
    @Test
    public void test3contains() {
        /* We give you ONE test case. You have to put more in. Look at
         * about line 24 for a discussion of how to do this. You will learn
         * a lot about how to prepare for testing complicated data structures.
         */
        var st= new CovidTree(humanC);
        assertEquals(true, st.contains(humanC));

        // Test for human not in the list and child size is 0
        assertEquals(false, st.contains(humanD));

        // Test for CovidTree with several children on same line
        st.insert(humanC, humanB);
        st.insert(humanC, humanD);
        assertEquals(true, st.contains(humanC));
        assertEquals(true, st.contains(humanB));
        assertEquals(true, st.contains(humanD));
        assertEquals(false, st.contains(humanE));

        // Test for CovidTree with depth more than 1
        st.insert(humanC, humanE);
        st.insert(humanB, humanG);
        st.insert(humanC, humanI);
        st.insert(humanC, humanL);
        assertEquals(true, st.contains(humanC));
        assertEquals(true, st.contains(humanB));
        assertEquals(true, st.contains(humanD));
        assertEquals(true, st.contains(humanE));
        assertEquals(true, st.contains(humanG));
        assertEquals(true, st.contains(humanI));
        assertEquals(true, st.contains(humanL));
        assertEquals(false, st.contains(humanF));

    }

    /** */
    @Test
    public void test4depth() {
        /* We give you ONE test case. You have to put more in. Look at
         * about line 90 for a discussion of how to do this. You will learn
         * a lot about how to prepare for testing complicated data structures.
         */
        var st= new CovidTree(humanB);
        st.insert(humanB, humanC);
        st.insert(humanC, humanD);
        assertEquals(0, st.depth(humanB));
        assertEquals(-1, st.depth(humanA));

        var st1= new CovidTree(humanA);
        st1.insert(humanA, humanB);
        st1.insert(humanA, humanC);
        st1.insert(humanB, humanD);
        st1.insert(humanD, humanE);
        st1.insert(humanC, humanF);
        st1.insert(humanE, humanG);
        st1.insert(humanD, humanH);
        var humans= new Human[] { humanA, humanB, humanC, humanD, humanE, humanF, humanG, humanH };
        var depths= new int[] { 0, 1, 1, 2, 3, 2, 4, 3 };
        for (int k= 0; k < humans.length; k++ ) {
            assertEquals(depths[k], st1.depth(humans[k]));
        }
    }

    /** */
    @Test
    public void test5WidthAtDepth() {
        // We give you ONE test case. You write more.
        var st= new CovidTree(humanB);
        assertEquals(1, st.widthAtDepth(0));

        var st1= new CovidTree(humanA);
        st1.insert(humanA, humanB);
        st1.insert(humanA, humanC);
        st1.insert(humanB, humanD);
        st1.insert(humanD, humanE);
        st1.insert(humanC, humanF);
        st1.insert(humanE, humanG);
        st1.insert(humanD, humanH);
        assertEquals(1, st1.widthAtDepth(0));
        assertEquals(2, st1.widthAtDepth(1));
        assertEquals(2, st1.widthAtDepth(2));
        assertEquals(2, st1.widthAtDepth(3));
        assertEquals(1, st1.widthAtDepth(4));
    }

    @SuppressWarnings("javadoc")
    @Test
    public void test6CovidRouteTo() {
        /* The one testcase we give shows you how method getNames() is
         * used to make testing a bit easier.
         * Use it in developing more testcases. Using method makeTree1 can help.*/
        var st= new CovidTree(humanB);
        var route= st.CovidRouteTo(humanB);
        assertEquals("[B]", getNames(route));

        var st1= new CovidTree(humanA);
        st1.insert(humanA, humanB);
        st1.insert(humanA, humanC);
        st1.insert(humanB, humanD);
        st1.insert(humanD, humanG);
        st1.insert(humanC, humanE);
        st1.insert(humanC, humanF);
        var route1= st1.CovidRouteTo(humanB);
        assertEquals("[A, B]", getNames(route1));
        var route2= st1.CovidRouteTo(humanC);
        assertEquals("[A, C]", getNames(route2));
        var route3= st1.CovidRouteTo(humanE);
        assertEquals("[A, C, E]", getNames(route3));
        // test from human B to human C route
        // they both appear in A but from different route
        var route4= st1.node(humanB).CovidRouteTo(humanC);
        assertEquals(null, route4);
        var route5= st1.CovidRouteTo(humanA);
        assertEquals("[A]", getNames(route5));
        var route6= st1.CovidRouteTo(humanL);
        assertEquals(null, route6);
        var route7= st1.node(humanB).CovidRouteTo(humanD);
        assertEquals("[B, D]", getNames(route7));

    }

    /** Return the names of Humans in sp, separated by ", " and delimited by [ ]. <br>
     * Precondition: No name is the empty string. */
    private String getNames(List<Human> sp) {
        var res= "[";
        for (Human p : sp) {
            if (res.length() > 1) res= res + ", ";
            res= res + p.name();
        }
        return res + "]";
    }

    /** */
    @Test
    public void test7commonAncestor() {
        var st= new CovidTree(humanB);
        st.insert(humanB, humanC);
        var p= st.commonAncestor(humanC, humanC);
        assertEquals(humanC, p);

        // make tree test
        var t1= makeTree1();
        assertEquals(humanB, t1.commonAncestor(humanB, humanD));
        assertEquals(humanG, t1.commonAncestor(humanI, humanJ));
        assertEquals(humanA, t1.commonAncestor(humanC, humanE));
        assertEquals(humanA, t1.commonAncestor(humanB, humanA));
        assertEquals(humanA, t1.commonAncestor(humanB, humanC));
        assertEquals(humanA, t1.commonAncestor(humanA, humanC));
        assertEquals(humanB, t1.commonAncestor(humanI, humanD));
        assertEquals(humanA, t1.commonAncestor(humanD, humanC));
        assertEquals(null, t1.commonAncestor(humanI, humanL));
        assertEquals(null, t1.commonAncestor(humanI, null));

        // Write more test cases. You can use the tree that makeTree(1) returns.

    }

    /** This is what makeTree1() produces
     *
     * <pre>
     *            A
     *          /   \
     *         B     C
     *       / | \
     *      D  E  F
     *            |
     *            G
     *            | \
     *            H  J
     *            |
     *            I
     * </pre>
     */

    /** */
    @Test
    public void test8equals() {
        // We give you one test case. You write more.
        // Using makeTree1() and makeTree2() can help.
        var treeB1= new CovidTree(humanB);
        var treeB2= new CovidTree(humanB);
        assertEquals(true, treeB1.equals(treeB1));
        assertEquals(true, treeB1.equals(treeB2));

        treeB1.insert(humanB, humanC);
        assertEquals(false, treeB1.equals(treeB2));
        treeB2.insert(humanB, humanC);
        assertEquals(true, treeB1.equals(treeB2));

        var treeB3= new CovidTree(humanD);
        assertEquals(false, treeB1.equals(treeB3));
        treeB3.insert(humanD, humanC);
        assertEquals(false, treeB1.equals(treeB3));

        var treeA1= makeTree1();
        var treeA2= makeTree2();
        var treeA3= makeTree1();
        assertEquals(false, treeA1.equals(treeA2));
        assertEquals(true, treeA1.equals(treeA3));
    }

    /* Make a tree like makeTree1 except that use humanK instead of humanH*/
    private CovidTree makeTree2() {
        var dt= new CovidTree(humanA); // A
        dt.insert(humanA, humanB); // A, B
        dt.insert(humanA, humanC); // A, C
        dt.insert(humanB, humanD); // B, D
        dt.insert(humanB, humanE); // B, E
        dt.insert(humanB, humanF); // B, F
        dt.insert(humanF, humanG); // F, G
        dt.insert(humanG, humanK); // G, K
        dt.insert(humanK, humanI); // K, I
        dt.insert(humanG, humanJ); // G, J
        return new CovidTree(dt);
    }

    // ===================================
    // ==================================

    /** Return a representation of this tree. This representation is: <br>
     * (1) the name of the Human at the root, followed by <br>
     * (2) the representations of the children <br>
     * . (in alphabetical order of the children's names). <br>
     * . There are two cases concerning the children.
     *
     * . No children? Their representation is the empty string. <br>
     * . Children? Their representation is the representation of each child, <br>
     * . with a blank between adjacent ones and delimited by "[" and "]". <br>
     * <br>
     * Examples: One-node tree: "A" <br>
     * root A with children B, C, D: "A[B C D]" <br>
     * root A with children B, C, D and B has a child F: "A[B[F] C D]" */
    public static String toStringBrief(CovidTree t) {
        var res= t.human().name();

        var childs= t.copyOfChildren().toArray();
        if (childs.length == 0) return res;
        res= res + "[";
        selectionSort1(childs);

        for (var k= 0; k < childs.length; k= k + 1) {
            if (k > 0) res= res + " ";
            res= res + toStringBrief((CovidTree) childs[k]);
        }
        return res + "]";
    }

    /** Sort b --put its elements in ascending order. <br>
     * Sort on the name of the Human at the root of each CovidTree.<br>
     * Throw a cast-class exception if b's elements are not CovidTree */
    public static void selectionSort1(Object[] b) {
        var j= 0;
        // {inv P: b[0..j-1] is sorted and b[0..j-1] <= b[j..]}
        // 0---------------j--------------- b.length
        // inv : b | sorted, <= | >= |
        // --------------------------------
        while (j != b.length) {
            // Put into p the index of smallest element in b[j..]
            var p= j;
            for (var i= j + 1; i != b.length; i++ ) {
                var bi= ((CovidTree) b[i]).human().name();
                var bp= ((CovidTree) b[p]).human().name();
                if (bi.compareTo(bp) < 0) {
                    p= i;

                }
            }
            // Swap b[j] and b[p]
            var t= b[j];
            b[j]= b[p];
            b[p]= t;
            j= j + 1;
        }
    }

}
