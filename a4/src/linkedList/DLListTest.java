package linkedList;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class DLListTest {

    @Test
    public void testConstructor() {
        DLList<Integer> d= new DLList<>();
        assertEquals("[]", d.toString());
        assertEquals("[]", d.toStringRev());
        assertEquals(0, d.size());
    }

    @Test
    public void testAppendAndToStringRev() {
        DLList<String> dl= new DLList<>();
        // when dl is empty list
        // size is already tested in constructor
        assertEquals(null, dl.head());
        assertEquals(null, dl.tail());
        // adding first node to list
        dl.append("2110");
        assertEquals("[2110]", dl.toString());
        assertEquals("[2110]", dl.toStringRev());
        assertEquals(1, dl.size());
        // adding a new node to the list?
        dl.append("1110");
        assertEquals("[2110, 1110]", dl.toString());
        assertEquals("[1110, 2110]", dl.toStringRev());
        assertEquals(2, dl.size());
        // appending empty string
        dl.append("");
        assertEquals("[2110, 1110, ]", dl.toString());
        assertEquals("[, 1110, 2110]", dl.toStringRev());
        assertEquals(3, dl.size());
        // appending null
        dl.append(null);
        assertEquals("[2110, 1110, , null]", dl.toString());
        assertEquals("[null, , 1110, 2110]", dl.toStringRev());
        assertEquals(4, dl.size());
    }

    @Test
    public void testPrepend() {
        DLList<String> dl= new DLList<>();
        assertEquals(null, dl.head());
        assertEquals(null, dl.tail());
        // prepend first node
        dl.prepend("2110");
        assertEquals("[2110]", dl.toString());
        assertEquals("[2110]", dl.toStringRev());
        assertEquals(1, dl.size());
        // adding a new node to the list
        dl.prepend("1110");
        assertEquals("[1110, 2110]", dl.toString());
        assertEquals("[2110, 1110]", dl.toStringRev());
        assertEquals(2, dl.size());
        // prepending empty string
        dl.prepend("");
        assertEquals("[, 1110, 2110]", dl.toString());
        assertEquals("[2110, 1110, ]", dl.toStringRev());
        assertEquals(3, dl.size());
        // prepending null
        dl.prepend(null);
        assertEquals("[null, , 1110, 2110]", dl.toString());
        assertEquals("[2110, 1110, , null]", dl.toStringRev());
        assertEquals(4, dl.size());
    }

    @Test
    public void testnode() {
        DLList<Integer> dl= new DLList<>();
        dl.append(2);
        dl.prepend(1);
        dl.append(3);
        dl.append(4);
        dl.append(5);
        dl.append(6);
        assertEquals("[1, 2, 3, 4, 5, 6]", dl.toString());
        assertEquals("[6, 5, 4, 3, 2, 1]", dl.toStringRev());
        assertEquals(6, dl.size());
        // getting first node
        assertEquals(1, dl.node(0).value());
        // getting middle node where k<size/2
        assertEquals(3, dl.node(2).value());
        // getting middle node where k=size/2
        assertEquals(4, dl.node(3).value());
        // getting middle node where k>size/2
        assertEquals(5, dl.node(4).value());
        // getting last node
        assertEquals(6, dl.node(5).value());
        // testing for empty string
        DLList<String> dl2= new DLList<>();
        dl2.append("");
        assertEquals("", dl2.node(0).value());
        // testing for null
        dl2.prepend(null);
        assertEquals(null, dl2.node(0).value());
        // testing assert statement where k=size
        assertThrows(AssertionError.class, () -> dl.node(6));
        // testing assert statement where k>size
        assertThrows(AssertionError.class, () -> dl.node(100));
        // testing assert statement where k<0
        assertThrows(AssertionError.class, () -> dl.node(-1));
    }

    @Test
    public void testInsertBefore() {
        DLList<Integer> dl= new DLList<>();
        dl.append(2);
        dl.prepend(1);
        dl.append(4);
        // adding node in front of last node
        dl.insertBefore(dl.node(2), 3);
        assertEquals("[1, 2, 3, 4]", dl.toString());
        assertEquals("[4, 3, 2, 1]", dl.toStringRev());
        assertEquals(4, dl.size());
        // adding node at front
        dl.insertBefore(dl.node(0), 0);
        assertEquals("[0, 1, 2, 3, 4]", dl.toString());
        assertEquals("[4, 3, 2, 1, 0]", dl.toStringRev());
        assertEquals(5, dl.size());
        // adding node in the middle
        dl.insertBefore(dl.node(3), -1);
        assertEquals("[0, 1, 2, -1, 3, 4]", dl.toString());
        assertEquals("[4, 3, -1, 2, 1, 0]", dl.toStringRev());
        assertEquals(6, dl.size());
        // adding node before the second node
        dl.insertBefore(dl.node(1), 10);
        assertEquals("[0, 10, 1, 2, -1, 3, 4]", dl.toString());
        assertEquals("[4, 3, -1, 2, 1, 10, 0]", dl.toStringRev());
        assertEquals(7, dl.size());
        // adding node for list with size 1
        DLList<Integer> dl1= new DLList<>();
        dl1.append(2);
        System.out.println(dl1);
        dl1.insertBefore(dl1.node(0), 1);
        assertEquals("[1, 2]", dl1.toString());
        assertEquals("[2, 1]", dl1.toStringRev());
        assertEquals(2, dl1.size());
        // testing empty string
        DLList<String> dl2= new DLList<>();
        dl2.append("A");
        dl2.insertBefore(dl2.node(0), "");
        assertEquals("[, A]", dl2.toString());
        assertEquals("[A, ]", dl2.toStringRev());
        assertEquals(2, dl2.size());
        // inserting in front of empty string
        dl2.insertBefore(dl2.node(0), "");
        assertEquals("[, , A]", dl2.toString());
        assertEquals("[A, , ]", dl2.toStringRev());
        assertEquals(3, dl2.size());
        // inserting null
        dl2.insertBefore(dl2.node(0), null);
        assertEquals("[null, , , A]", dl2.toString());
        assertEquals("[A, , , null]", dl2.toStringRev());
        assertEquals(4, dl2.size());
        // testing assert for n!=null
        assertThrows(AssertionError.class, () -> dl2.insertBefore(null, "Hello"));
    }

    @Test
    public void testDelete() {
        DLList<Integer> dl= new DLList<>();
        dl.append(2);
        dl.prepend(1);
        dl.append(3);
        dl.append(4);
        assertEquals("[1, 2, 3, 4]", dl.toString());
        assertEquals("[4, 3, 2, 1]", dl.toStringRev());
        assertEquals(4, dl.size());
        // deleting middle
        dl.delete(dl.node(1));
        assertEquals("[1, 3, 4]", dl.toString());
        assertEquals("[4, 3, 1]", dl.toStringRev());
        assertEquals(3, dl.size());
        // deleting first
        dl.delete(dl.node(0));
        assertEquals("[3, 4]", dl.toString());
        assertEquals("[4, 3]", dl.toStringRev());
        assertEquals(2, dl.size());
        // deleting last node of the list
        dl.delete(dl.node(1));
        assertEquals("[3]", dl.toString());
        assertEquals("[3]", dl.toStringRev());
        assertEquals(1, dl.size());
        // deleting node when there is only one
        dl.delete(dl.node(0));
        assertEquals("[]", dl.toString());
        assertEquals("[]", dl.toStringRev());
        assertEquals(0, dl.size());
        // testing empty string
        DLList<String> dl2= new DLList<>();
        dl2.append("A");
        dl2.insertBefore(dl2.node(0), "");
        dl2.delete(dl2.node(0));
        assertEquals("[A]", dl2.toString());
        assertEquals("[A]", dl2.toStringRev());
        assertEquals(1, dl2.size());
        // testing null case: deleting null
        dl2.append(null);
        dl2.delete(dl2.node(1));
        assertEquals("[A]", dl2.toString());
        assertEquals("[A]", dl2.toStringRev());
        assertEquals(1, dl2.size());
        // testing assert statement for n=null
        assertThrows(AssertionError.class, () -> dl2.delete(null));

    }
}
