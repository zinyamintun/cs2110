package a1;

/** NetId: zyt2, js2788. Time spent: 6 hours, 0 minutes.
 * What I thought about this assignment:
 * The instructions were very clear and easy to follow. The logic to the assignment was clear. The difficulty level was good. */

/** An instance maintains info about the PhD of a person. */

public class PhD {
    /** Name of the person with a PhD, String length > 1. */
    private String name;
    /** Year the PhD was awarded. */
    private int year;
    /** Month the PhD was awarded. In range 1..12 with 1 being January, etc */
    private int month;
    /** The first PhD advisor of this person —null if unknown. */
    private PhD advisor1;
    /** The second advisor of this person —null if unknown or<br>
     * if the person has less than two advisors. */
    private PhD advisor2;
    /** The number of PhD advisees of this person */
    private int advisees;

    /** Constructor: an instance with name n, PhD year y, and PhD month m.<br>
     * The advisors are unknown, and there are 0 advisees.<br>
     * Precondition: n has at least 2 chars, and m is in 1..12. */
    PhD(String n, int y, int m) {
        assert n.length() > 1;
        assert m > 0 && m < 13;
        name= n;
        year= y;
        month= m;
    }

    /** Return the name of this person. */
    String name() {
        return name;
    }

    /** Return the date this person got their PhD in the form "month/<year>" <br>
     * E.g. For February 2022, return "2/2022". */
    String date() {
        return month + "/" + year;
    }

    /** Return the first advisor of this PhD (null if unknown). */
    PhD advisor1() {
        return advisor1;
    }

    /** Return the second advisor of this PhD (null if unknown or non-existent). */
    PhD advisor2() {
        return advisor2;
    }

    /** Return the number of PhD advisees of this person. */
    int advisees() {
        return advisees;
    }

    /** Add p as the first advisor of this person. <br>
     * Precondition: the first advisor is unknown and p is not null. */
    void addAdvisor1(PhD p) {
        assert advisor1 == null && p != null;
        advisor1= p;
        p.advisees= p.advisees + 1;
    }

    /** Add p as the second advisor of this PhD. <br>
     * Precondition: The first advisor is known, <br>
     * the second advisor is unknown, p is not null, <br>
     * and p is different from the first advisor. */
    void addAdvisor2(PhD p) {
        assert advisor1 != null && advisor2 == null && p != null;
        advisor2= p;
        p.advisees= p.advisees + 1;
    }

    /** Constructor: a PhD with name n, PhD year y, PhD month m, <br>
     * first advisor p1, and second advisor p2.<br>
     * Precondition: n has at least 2 chars, m is in 1..12,<br>
     * p1 and p2 are not null, and p1 and p2 are different. */
    PhD(String n, int y, int m, PhD p1, PhD p2) {
        this(n, y, m);
        assert p1 != null && p2 != null && p1 != p2;
        addAdvisor1(p1);
        addAdvisor2(p2);
    }

    /** Return value of: "p is not null and this PhD got the PhD before p" */
    boolean gotBefore(PhD p) {
        return p != null && (year < p.year || year == p.year && month < p.month);
    }

    /** Return value of: "this PhD is an intellectual sibling of p".<br>
     * *Precondition: p is not null. */
    boolean isSiblingOf(PhD p) {
        assert p != null;
        return this != p && (advisor1 != null && advisor1 == p.advisor1 ||
            advisor1 != null && advisor1 == p.advisor2 ||
            advisor2 != null && advisor2 == p.advisor1 ||
            advisor2 != null && advisor2 == p.advisor2);

    }
}
