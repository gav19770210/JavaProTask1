package ru.gav19770210.javapro.task01;

import ru.gav19770210.javapro.task01.annotation.*;

public class TestClass {
    @BeforeSuite
    private static void methBeforeSuite() {
        System.out.println("run methBeforeSuite");
    }

    @AfterSuite
    private static void methAfterSuite() {
        System.out.println("run methAfterSuite");
    }

    //@Test
    private static void methTest2() {
        System.out.println("run methTest2");
    }

    //@BeforeSuite
    public void methBeforeSuite2() {
        System.out.println("run methBeforeSuite2");
    }

    //@AfterSuite
    public void methAfterSuite2() {
        System.out.println("run methAfterSuite2");
    }

    @BeforeTest
    void methBeforeTest() {
        System.out.println("run methBeforeTest");
    }

    @AfterTest
    void methAfterTest() {
        System.out.println("run methAfterTest");
    }

    //@BeforeTest
    public void methBeforeTest2() {
        System.out.println("run methBeforeTest2");
    }

    //@AfterTest
    public void methAfterTest2() {
        System.out.println("run methAfterTest2");
    }

    @Test
    public void methTest() {
        System.out.println("run methTest");
    }

    @Test(priority = 1)
    public void methTest1() {
        System.out.println("run methTest1");
    }

    @Test(priority = 9)
    @CsvSource("Java, true, 1, 2, 10, 20, 1.23f, 2.34, q")
    private void methTest9(String str, boolean bool, byte pb, short ps, int pi, long pl, float pf, double pd, char pc) {
        if (!str.equals("Java")) throw new Error("param String is wrong");
        if (!bool) throw new Error("param boolean is wrong");
        if (pb != 1) throw new Error("param byte is wrong");
        if (ps != 2) throw new Error("param short is wrong");
        if (pi != 10) throw new Error("param int is wrong");
        if (pl != 20) throw new Error("param long is wrong");
        if (Float.compare(pf, 1.23f) != 0) throw new Error("param float is wrong");
        if (Double.compare(pd, 2.34) != 0) throw new Error("param double is wrong");
        if (pc != 'q') throw new Error("param char is wrong");
        System.out.println("run methTest9");
    }
}
