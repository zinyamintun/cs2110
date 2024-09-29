package a1;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PhDTest {

    @Test
    void testConstructor1() {
        PhD p1= new PhD("Leon", 2000, 12);
        assertEquals("Leon", p1.name());
        assertEquals("12/2000", p1.date());
        assertEquals(null, p1.advisor1());
        assertEquals(null, p1.advisor2());
        assertEquals(0, p1.advisees());

    }

    @Test
    void testMutators() {
        PhD zin= new PhD("Zin", 2024, 9);
        PhD amanda= new PhD("Amanda", 2025, 9);
        PhD max= new PhD("Max", 2017, 8);
        max.addAdvisor1(amanda);
        max.addAdvisor2(zin);
        assertEquals(amanda, max.advisor1());
        assertEquals(zin, max.advisor2());
        assertEquals(1, amanda.advisees());
        assertEquals(1, zin.advisees());
        assertEquals(0, max.advisees());
    }

    @Test
    void testConstructor2() {
        PhD p1= new PhD("Leon", 2000, 12);
        PhD p2= new PhD("Mike", 1995, 5);
        PhD p3= new PhD("Rayleigh", 1667, 3, p1, p2);
        assertEquals("Rayleigh", p3.name());
        assertEquals("3/1667", p3.date());
        assertEquals(p1, p3.advisor1());
        assertEquals(p2, p3.advisor2());
        assertEquals(0, p3.advisees());
        assertEquals(1, p1.advisees());
        assertEquals(1, p2.advisees());
    }

    @Test
    void testgotBefore() {
        PhD Jan2022= new PhD("Luffy", 2022, 1);
        PhD Feb2022= new PhD("Coco", 2022, 2);
        PhD Jan2021= new PhD("Zoro", 2021, 1);
        PhD July2020= new PhD("Ace", 2020, 7);
        PhD Jan2022same= new PhD("Chopper", 2022, 1);
        assertEquals(true, Jan2022.gotBefore(Feb2022));// same year but diff month
        assertEquals(true, Jan2021.gotBefore(Jan2022));// same month but diff year
        assertEquals(false, Jan2022same.gotBefore(Jan2022));// same month and year
        assertEquals(true, July2020.gotBefore(Jan2022));// diff year & diff month
        assertEquals(false, Jan2022.gotBefore(null));// when p is null
        assertEquals(false, Jan2022.gotBefore(Jan2022));// comparing with same object
    }

    @Test
    void testisSiblingOf() {
        PhD p1= new PhD("Rayleigh", 2002, 11);
        PhD p2= new PhD("Brook", 2000, 6);
        p2.addAdvisor1(p1);
        PhD p3= new PhD("Pikachu", 2001, 3, p1, p2);
        PhD p4= new PhD("Mickey", 2030, 3, p3, p1);
        PhD p5= new PhD("Sanji", 2032, 3, p3, p4);
        PhD p6= new PhD("Nami", 2322, 2);
        PhD p7= new PhD("Jackson", 2322, 3);
        p7.addAdvisor1(p4);
        PhD p8= new PhD("Alice", 1998, 2, p1, p2);
        assertEquals(false, p2.isSiblingOf(p1));// p1 has null advisors and p2 has 1 advisor
        assertEquals(true, p3.isSiblingOf(p2));// both has same first advisor but one of them has
                                               // second advisor as null
        assertEquals(true, p3.isSiblingOf(p4));// p3 second advisor is same as p4 first advisor
        assertEquals(false, p3.isSiblingOf(p5));// both advisors are different
        assertEquals(false, p1.isSiblingOf(p6));// both don't have any advisor
        assertEquals(false, p2.isSiblingOf(p7));// both have only 1 advisor
        assertEquals(false, p7.isSiblingOf(p7));// same person
        assertEquals(true, p3.isSiblingOf(p8));// both advisors are same
        assertEquals(true, p4.isSiblingOf(p3));// p3 second advisor is same as p4 first
                                               // advisor
        assertEquals(true, p4.isSiblingOf(p5));// have 2 advisors and first one is same
        assertEquals(true, p3.isSiblingOf(p8));// have 2 advisors and second one is same
    }

    @Test
    void testassert() {
        PhD p1= new PhD("Rayleigh", 2002, 11);
        PhD p2= new PhD("Brook", 2000, 6);
        p2.addAdvisor1(p1);
        PhD p3= new PhD("Pikachu", 2001, 3, p1, p2);
        PhD p4= new PhD("Mickey", 2030, 3, p3, p1);
        assertThrows(AssertionError.class, () -> new PhD("", 2002, 1));// name length is 0
        assertThrows(AssertionError.class, () -> new PhD("a", 2001, 3));// name length is 1
        assertThrows(AssertionError.class, () -> new PhD("Chopper", 2001, 13));// month > 12
        assertThrows(AssertionError.class, () -> new PhD("Chopper", 2001, -1));// month < 1

        assertThrows(AssertionError.class, () -> p1.addAdvisor1(null));// p is null
        assertThrows(AssertionError.class, () -> p3.addAdvisor1(p1));// adding same advisor to
                                                                     // advisor 1
        assertThrows(AssertionError.class, () -> p2.addAdvisor1(p3));// add advisor 1 when advisor1
                                                                     // exist
        assertThrows(AssertionError.class, () -> p1.addAdvisor2(p3));// add advisor 2 when no
                                                                     // advisors
        assertThrows(AssertionError.class, () -> p3.addAdvisor1(p4));// add advisor 2 when both
                                                                     // advisors exist
        assertThrows(AssertionError.class, () -> p3.addAdvisor1(null));// advisor2 is null
        assertThrows(AssertionError.class, () -> p3.addAdvisor1(p2));// adding same advisor2 to
                                                                     // advisor 2
        assertThrows(AssertionError.class, () -> p3.addAdvisor1(p2));// adding advisor2 as advisor1

        assertThrows(AssertionError.class, () -> p3.addAdvisor2(p1));// adding advisor1 as advisor2

        assertThrows(AssertionError.class, () -> new PhD("", 2002, 1, p1, p2));// name length is 0
        assertThrows(AssertionError.class, () -> new PhD("a", 2002, 1, p1, p2)); // name length is 1
        assertThrows(AssertionError.class, () -> new PhD("Nemo", 2002, 200, p1, p2));// month >12
        assertThrows(AssertionError.class, () -> new PhD("Nemo", 2002, 0, p1, p2)); // month < 1
        assertThrows(AssertionError.class, () -> new PhD("Nemo", 2002, 1, p1, null));// advisor2 is
                                                                                     // null
        assertThrows(AssertionError.class, () -> new PhD("Nemo", 2002, 1, null, p2)); // advisor1 is
                                                                                      // null
        assertThrows(AssertionError.class, () -> new PhD("Nemo", 2002, 1, null, null)); // both
                                                                                        // advisors
                                                                                        // null
        assertThrows(AssertionError.class, () -> new PhD("Nemo", 2002, 1, p1, p1));// same advisor

        assertThrows(AssertionError.class, () -> p1.isSiblingOf(null)); // p is null
    }
}
