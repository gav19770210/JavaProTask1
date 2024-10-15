package ru.gav19770210.javapro.task01;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.gav19770210.javapro.task01.annotation.AfterSuite;
import ru.gav19770210.javapro.task01.annotation.BeforeSuite;
import ru.gav19770210.javapro.task01.annotation.CsvSource;
import ru.gav19770210.javapro.task01.log.TestResult;
import ru.gav19770210.javapro.task01.log.TestStatus;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class TestRunnerTests {
    @DisplayName("Check BeforeSuite no test")
    @Test
    public void checkBeforeSuiteNoTest() {
        List<TestResult> testResults = TestRunner.runTests(TestClassBeforeSuiteNoTest.class);
        testResults.forEach(System.out::println);
        Assertions.assertEquals(0, testResults.stream().filter(r -> r.getTestStatus() == TestStatus.PASSED).count());
        Assertions.assertEquals(0, testResults.stream().filter(r -> r.getTestStatus() == TestStatus.ERROR).count());
        Assertions.assertTrue(testResults.stream().anyMatch(r -> r.getTestStatus() == TestStatus.SKIPPED));
    }

    @DisplayName("Check BeforeSuite not static")
    @Test
    public void checkBeforeSuiteNonStatic() {
        List<TestResult> testResults = TestRunner.runTests(TestClassBeforeSuiteNonStatic.class);
        testResults.forEach(r -> new PrintTestResult().accept(r));
        Assertions.assertEquals(testResults.size(), testResults.stream().filter(r -> r.getTestStatus() == TestStatus.ERROR).count());
    }

    @DisplayName("Check BeforeSuite count")
    @Test
    public void checkBeforeSuiteCount() {
        List<TestResult> testResults = TestRunner.runTests(TestClassBeforeSuiteCount.class);
        testResults.forEach(r -> new PrintTestResult().accept(r));
        Assertions.assertEquals(0, testResults.stream().filter(r -> r.getTestStatus() == TestStatus.PASSED).count());
        Assertions.assertTrue(testResults.stream().anyMatch(r -> r.getTestStatus() == TestStatus.ERROR));
    }

    @DisplayName("Check AfterSuite no test")
    @Test
    public void checkAfterSuiteNoTest() {
        List<TestResult> testResults = TestRunner.runTests(TestClassAfterSuiteNoTest.class);
        testResults.forEach(System.out::println);
        Assertions.assertEquals(0, testResults.stream().filter(r -> r.getTestStatus() == TestStatus.PASSED).count());
        Assertions.assertEquals(0, testResults.stream().filter(r -> r.getTestStatus() == TestStatus.ERROR).count());
        Assertions.assertTrue(testResults.stream().anyMatch(r -> r.getTestStatus() == TestStatus.SKIPPED));
    }

    @DisplayName("Check AfterSuite not static")
    @Test
    public void checkAfterSuiteNonStatic() {
        List<TestResult> testResults = TestRunner.runTests(TestClassAfterSuiteNonStatic.class);
        testResults.forEach(r -> new PrintTestResult().accept(r));
        Assertions.assertEquals(testResults.size(), testResults.stream().filter(r -> r.getTestStatus() == TestStatus.ERROR).count());
    }

    @DisplayName("Check AfterSuite count")
    @Test
    public void checkAfterSuiteCount() {
        List<TestResult> testResults = TestRunner.runTests(TestClassAfterSuiteCount.class);
        testResults.forEach(r -> new PrintTestResult().accept(r));
        Assertions.assertEquals(0, testResults.stream().filter(r -> r.getTestStatus() == TestStatus.PASSED).count());
        Assertions.assertTrue(testResults.stream().anyMatch(r -> r.getTestStatus() == TestStatus.ERROR));
    }

    @DisplayName("Check Test static")
    @Test
    public void checkTestStatic() {
        List<TestResult> testResults = TestRunner.runTests(TestClassTestStatic.class);
        testResults.forEach(r -> new PrintTestResult().accept(r));
        Assertions.assertEquals(testResults.size(), testResults.stream().filter(r -> r.getTestStatus() == TestStatus.ERROR).count());
    }

    @DisplayName("Check Test priority error")
    @Test
    public void checkTestPriorityError() {
        List<TestResult> testResults = TestRunner.runTests(TestClassTestPriorityError.class);
        testResults.forEach(r -> new PrintTestResult().accept(r));
        Assertions.assertEquals(testResults.size(), testResults.stream().filter(r -> r.getTestStatus() == TestStatus.ERROR).count());
    }

    @DisplayName("Check Test pass")
    @Test
    public void checkTestPass() {
        List<TestResult> testResults = TestRunner.runTests(TestClassTestPass.class);
        testResults.forEach(System.out::println);
        Assertions.assertEquals(testResults.size(), testResults.stream().filter(r -> r.getTestStatus() == TestStatus.PASSED).count());
    }

    @DisplayName("Check Test priority pass")
    @Test
    public void checkTestPriorityPass() {
        List<TestResult> testResults = TestRunner.runTests(TestClassTestPriorityPass.class);
        testResults.forEach(System.out::println);
        Assertions.assertEquals(testResults.size(), testResults.stream().filter(r -> r.getTestStatus() == TestStatus.PASSED).count());
        // Получение массива приоритетов выполненных методов
        int[] arrPriority = testResults.stream().mapToInt(r -> r.getMethod().getAnnotation(ru.gav19770210.javapro.task01.annotation.Test.class).priority()).toArray();
        // Проверка что массив приоритетов сортирован
        if (arrPriority.length > 1)
            Assertions.assertTrue(IntStream.range(0, arrPriority.length - 1).noneMatch(i -> arrPriority[i] > arrPriority[i + 1]));
    }

    @DisplayName("Check Test error")
    @Test
    public void checkTestError() {
        List<TestResult> testResults = TestRunner.runTests(TestClassTestError.class);
        testResults.forEach(r -> new PrintTestResult().accept(r));
        Assertions.assertEquals(testResults.size(), testResults.stream().filter(r -> r.getTestStatus() == TestStatus.ERROR).count());
    }

    @DisplayName("Check Test CsvSource pass")
    @Test
    public void checkTestCsvSource() {
        List<TestResult> testResults = TestRunner.runTests(TestClassTestCsvSource.class);
        testResults.forEach(System.out::println);
        Assertions.assertEquals(testResults.size(), testResults.stream().filter(r -> r.getTestStatus() == TestStatus.PASSED).count());
    }

    @DisplayName("Check BeforeSuite error")
    @Test
    public void checkBeforeSuiteError() {
        List<TestResult> testResults = TestRunner.runTests(TestClassBeforeSuiteError.class);
        testResults.forEach(r -> new PrintTestResult().accept(r));
        Assertions.assertEquals(testResults.size(), testResults.stream().filter(r -> r.getTestStatus() == TestStatus.ERROR).count());
    }

    @DisplayName("Check AfterSuite error")
    @Test
    public void checkAfterSuiteError() {
        List<TestResult> testResults = TestRunner.runTests(TestClassAfterSuiteError.class);
        testResults.forEach(r -> new PrintTestResult().accept(r));
        Assertions.assertEquals(testResults.size() - 1, testResults.stream().filter(r -> r.getTestStatus() == TestStatus.PASSED).count());
        Assertions.assertEquals(1, testResults.stream().filter(r -> r.getTestStatus() == TestStatus.ERROR).count());
    }

    @DisplayName("Check BeforeSuite & Test & AfterSuite pass")
    @Test
    public void checkBeforeSuiteTestAfterSuitePass() {
        List<TestResult> testResults = TestRunner.runTests(TestClassBeforeSuiteTestAfterSuitePass.class);
        testResults.forEach(System.out::println);
        Assertions.assertEquals(testResults.size(), testResults.stream().filter(r -> r.getTestStatus() == TestStatus.PASSED).count());
    }
}

class PrintTestResult implements Consumer<TestResult> {
    @Override
    public final void accept(TestResult o) {
        System.out.println(o);
        if (Objects.nonNull(o.getMessage())) {
            o.getMessage().printStackTrace(System.out);
        }
    }
}

class TestClassBeforeSuiteNoTest {
    @BeforeSuite
    public static void methBeforeSuite() {
        System.out.println("run methBeforeSuite");
    }
}

class TestClassBeforeSuiteNonStatic {
    @BeforeSuite
    public void methBeforeSuite() {
        System.out.println("run methBeforeSuite");
    }
}

class TestClassBeforeSuiteCount {
    @BeforeSuite
    public static void methBeforeSuite() {
        System.out.println("run methBeforeSuite");
    }

    @BeforeSuite
    public void methBeforeSuite2() {
        System.out.println("run methBeforeSuite2");
    }
}

class TestClassAfterSuiteNoTest {
    @AfterSuite
    public static void methAfterSuite() {
        System.out.println("run methAfterSuite");
    }
}

class TestClassAfterSuiteNonStatic {
    @AfterSuite
    public void methAfterSuite() {
        System.out.println("run methAfterSuite");
    }
}

class TestClassAfterSuiteCount {
    @AfterSuite
    public static void methAfterSuite() {
        System.out.println("run methAfterSuite");
    }

    @AfterSuite
    public void methAfterSuite2() {
        System.out.println("run methAfterSuite2");
    }
}

class TestClassTestStatic {
    @ru.gav19770210.javapro.task01.annotation.Test
    public static void methTest() {
        System.out.println("run methTest");
    }
}

class TestClassTestPriorityError {
    @ru.gav19770210.javapro.task01.annotation.Test(priority = 0)
    public void methTest() {
        System.out.println("run methTest");
    }

    @ru.gav19770210.javapro.task01.annotation.Test(priority = 11)
    public void methTest2() {
        System.out.println("run methTest2");
    }
}

class TestClassTestPass {
    @ru.gav19770210.javapro.task01.annotation.Test
    public void methTest() {
        System.out.println("run methTest");
    }
}

class TestClassTestPriorityPass {
    @ru.gav19770210.javapro.task01.annotation.Test
    public void methTest() {
        System.out.println("run methTest");
    }

    @ru.gav19770210.javapro.task01.annotation.Test(priority = 1)
    public void methTest1() {
        System.out.println("run methTest1");
    }

    @ru.gav19770210.javapro.task01.annotation.Test(priority = 8)
    public void methTest2() {
        System.out.println("run methTest2");
    }
}

class TestClassTestError {
    @ru.gav19770210.javapro.task01.annotation.Test
    public void methTest() {
        throw new Error("error methTest");
    }
}

class TestClassTestCsvSource {
    @ru.gav19770210.javapro.task01.annotation.Test
    @CsvSource("Java, true, 1, 2, 10, 20, 1.23f, 2.34, q")
    public void methTest(String str, boolean bool, byte pb, short ps, int pi, long pl, float pf, double pd, char pc) {
        if (!str.equals("Java")) throw new Error("param String is wrong");
        if (!bool) throw new Error("param boolean is wrong");
        if (pb != 1) throw new Error("param byte is wrong");
        if (ps != 2) throw new Error("param short is wrong");
        if (pi != 10) throw new Error("param int is wrong");
        if (pl != 20) throw new Error("param long is wrong");
        if (Float.compare(pf, 1.23f) != 0) throw new Error("param float is wrong");
        if (Double.compare(pd, 2.34) != 0) throw new Error("param double is wrong");
        if (pc != 'q') throw new Error("param char is wrong");
        System.out.println("run methTest");
    }
}

class TestClassBeforeSuiteError {
    @BeforeSuite
    public static void methBeforeSuite() {
        throw new Error("error methBeforeSuite");
    }

    @AfterSuite
    public static void methAfterSuite() {
        System.out.println("run methAfterSuite");
    }

    @ru.gav19770210.javapro.task01.annotation.Test
    public void methTest() {
        System.out.println("run methTest");
    }
}

class TestClassAfterSuiteError {
    @BeforeSuite
    public static void methBeforeSuite() {
        System.out.println("run methBeforeSuite");
    }

    @AfterSuite
    public static void methAfterSuite() {
        throw new Error("error methAfterSuite");
    }

    @ru.gav19770210.javapro.task01.annotation.Test
    public void methTest() {
        System.out.println("run methTest");
    }
}

class TestClassBeforeSuiteTestAfterSuitePass {
    @BeforeSuite
    public static void methBeforeSuite() {
        System.out.println("run methBeforeSuite");
    }

    @AfterSuite
    public static void methAfterSuite() {
        System.out.println("run methAfterSuite");
    }

    @ru.gav19770210.javapro.task01.annotation.Test
    public void methTest() {
        System.out.println("run methTest");
    }
}
