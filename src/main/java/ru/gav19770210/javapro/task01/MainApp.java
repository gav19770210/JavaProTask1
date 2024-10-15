package ru.gav19770210.javapro.task01;

import ru.gav19770210.javapro.task01.log.TestResult;

import java.util.List;

public class MainApp {
    public static void main(String[] args) {
        List<TestResult> testResults = TestRunner.runTests(TestClass.class);
        testResults.forEach(System.out::println);
    }
}
