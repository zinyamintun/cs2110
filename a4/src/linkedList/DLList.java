package linkedList;
/*  Name(s): Amanda Sun, Zin Yamin Tun
 * Netid(s): js2788, zyt2
 * What I thought about this assignment:
 *We thought this assignment is a good practice to learn about linked list and
 *how they work. It also gives and in-depth experience on the structure of linked
 *list.
 */

/** An instance is a doubly linked list. */
public class DLList<E> {
    /** Replace "-1" by the time you spent on A3 in hours.<br>
     * Example: for 3 hours 15 minutes, use 3.25<br>
     * Example: for 4 hours 30 minutes, use 4.50<br>
     * Example: for 5 hours, use 5 or 5.0 */
    public static double timeSpent= 5.0;

    /** Number of values in the list. */
    private int size;
    /** First and last nodes of the linked list (null if size is 0) */
    private Node head, tail;

    /** Constructor: an empty linked list. Do not change this. */
    public DLList() {}

    /** = the number of values in this list. <br>
     * This function takes constant time. */
    public int size() {
        return size;
    }

    /** = the first node of the list (null if the list is empty). */
    public Node head() {
        return head;
    }

    /** = the last node of the list (null if the list is empty). */
    public Node tail() {
        return tail;
    }

    /** Return the value in node n of this list. <br>
     * Precondition: n is a node of this list; it may not be null. */
    public E value(Node n) {
        assert n != null;
        return n.val;
    }

    /** Return a representation of this list: its values, with<br>
     * "[" at the beginning, "]" at the end, and adjacent ones separated by ", " . <br>
     * Takes time proportional to the length of this list.<br>
     * E.g. for the list containing 4 7 8 in that order, the result it "[4, 7, 8]". <br>
     * E.g. for the list containing two empty strings, the result is "[, ]" */
    @Override
    public String toString() {
        var res= new StringBuilder("[");
        var n= head;
        // inv: res contains values of nodes before node n (all of them if n = null),
        // with ", " after each (except for the last value)
        while (n != null) {
            res.append(n.val);
            n= n.next;
            if (n != null) res.append(", ");
        }

        return res + "]";
    }

    /** Return a representation of this list: its values in reverse, with<br>
     * "[" at the beginning, "]" at the end, and adjacent ones separated by ", " . <br>
     * Note that toStringRev is the reverse of toString. <br>
     * Takes time proportional to the length of this list. <br>
     * E.g. for the list containing 4 7 8 in that order, the result is "[8, 7, 4]". <br>
     * E.g. for the list containing two empty strings, the result is "[, ]". */
    public String toStringRev() { // Note:
        // TODO 1. Look at toString to see how that was written.
        // Use the same scheme. Extreme case to watch out for:
        // E is String and values are the empty string.
        // You can't test this fully until #2, append, is written.
        var res= new StringBuilder("[");
        var n= tail;
        while (n != null) {
            res.append(n.val);
            n= n.prev;
            if (n != null) res.append(", ");
        }
        return res + "]";
    }

    /** Add v to the end of this list. <br>
     * This operation takes constant time.<br>
     * E.g. if the list is [8, 7, 4], append(2) changes this list to [8, 7, 4, 2]. */
    public void append(E v) {
        // TODO 2. After writing append, test append and toStringRev
        // thoroughly before starting on the next. These two must be correct
        // in order to be able to write and test all the others.
        Node nv= new Node(null, v, null);
        if (size != 0) {
            // creating v as new node
            nv.prev= tail;
            // putting v as last node's next
            tail.next= nv;
            // change the last node as new node
            tail= nv;

        } else {
            head= nv;
            tail= nv;

        }
        size+= 1;
    }

    /** Insert v at the beginning of the list. <br>
     * This operation takes constant time.<br>
     * E.g. if the list is [8, 7, 4], prepend(2) changes this list to [2, 8, 7, 4]. */
    public void prepend(E v) {
        // TODO 3. Write and test prepend thoroughly before moving on to TODO 4
        Node nv= new Node(null, v, null);
        if (size != 0) {
            nv.next= head;
            head.prev= nv;
            head= nv;
        } else {
            head= nv;
            tail= nv;
        }
        size+= 1;
    }

    /** = node number k. <br>
     * Precondition: 0 <= k < size of the list. <br>
     * Note: If k is 0, return first node; if k = 1, return second node, ... */
    public Node node(int k) {
        // TODO 4. This method should take time proportional to min(k, size/2).
        // For example, if k < size/2, search from the beginning of the
        // list, otherwise search from the end of the list. If k = size/2,
        // search from either end; it doesn't matter.
        assert k >= 0 && k < size;
        if (k < size / 2) {
            Node nv1= head;
            for (int n= 0; n < k; n++ ) {
                nv1= nv1.next;
            }
            return nv1;
        } else {
            Node nv2= tail;
            for (int n= size - 1; n > k; n-- ) {
                nv2= nv2.prev;
            }
            return nv2;
        }
    }

    /** Insert value v in a new node after node n. <br>
     * This operation takes constant time. <br>
     * Precondition: n must be a node of this list; it may not be null.<br>
     * E.g. if the list is [3, 8, 2] and n points to the node with 8 in it, <br>
     * and v is 1, the list is changed to [3, 1, 8, 2] */
    public void insertBefore(Node n, E v) {
        // TODO 5. Make sure this method takes constant time.
        assert n != null;
        Node nv= new Node(null, v, n);
        if (n == head) {
            head.prev= nv;
            head= nv;
        } else {
            nv.prev= n.prev;
            n.prev.next= nv;
            n.prev= nv;
        }
        size+= 1;
    }

    /** Remove node n from this list. <br>
     * This operation must take constant time. <br>
     * Precondition: n must be a node of this list; it may not be null. <br>
     * E.g. if the list is [3, 8, 2] and n points to the node with 3 in it, <br>
     * the list is changed to [8, 2] */
    public void delete(Node n) {
        // TODO 6. Make sure this method takes constant time.
        assert n != null;
        if (size == 1) {
            head= null;
            tail= null;
        } else if (n == head) {
            n.next.prev= null;
            head= n.next;
        } else if (n == tail) {
            n.prev.next= null;
            tail= n.prev;
        } else {
            n.prev.next= n.next;
            n.next.prev= n.prev;
        }
        size-= 1;
    }

    /*********************/

    /** An instance is a node of this list. */
    class Node {
        /** The value in the node */
        private E val;
        /** Previous node on list (null if this is first node) and<br>
         * Next node on list (null if this is last node). */
        private Node prev, next;

        /** Constructor: an instance with previous node p (can be null), value v,<br>
         * and next node s (can be null). */
        Node(Node p, E v, Node s) {
            prev= p;
            val= v;
            next= s;
        }

        /** = the previous node (null if this is the first node of the list). */
        Node prev() {
            return prev;
        }

        /** = the value of this node. */
        E value() {
            return val;
        }

        /** = the next node in this list (null if this is the last node of this list). */
        Node next() {
            return next;
        }
    }

}
