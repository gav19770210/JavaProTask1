package ru.gav19770210.javapro.task01.check;

import lombok.Builder;
import lombok.Singular;
import ru.gav19770210.javapro.task01.log.TestResult;
import ru.gav19770210.javapro.task01.log.TestStatus;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Builder
public class CheckTestClass {
    @Singular
    List<CheckTestMethod> listTestMethods = new ArrayList<>();
    @Singular
    List<CheckTestArrMethods> listTestArrMethods = new ArrayList<>();
    List<TestResult> testResults;

    public boolean checkTestMethods(Method[] methods) {
        if (methods.length == 0 || listTestMethods.isEmpty() && listTestArrMethods.isEmpty()) return true;

        String logMsg;
        boolean res = true;
        for (CheckTestMethod checkTestMethod : listTestMethods) {
            for (Method method : methods) {
                logMsg = checkTestMethod.checkMethod(method);
                if (!logMsg.isEmpty()) {
                    testResults.add(new TestResult(method, TestStatus.ERROR, new Exception(logMsg)));
                    res = false;
                }
            }
        }
        for (CheckTestArrMethods checkTestArrMethods : listTestArrMethods) {
            logMsg = checkTestArrMethods.checkArrMethods(methods);
            if (!logMsg.isEmpty()) {
                testResults.add(new TestResult(null, TestStatus.ERROR, new Exception(logMsg)));
                res = false;
            }
        }
        return res;
    }
}
