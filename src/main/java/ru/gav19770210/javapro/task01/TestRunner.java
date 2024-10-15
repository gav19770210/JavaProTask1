package ru.gav19770210.javapro.task01;

import ru.gav19770210.javapro.task01.annotation.*;
import ru.gav19770210.javapro.task01.check.CheckTestClass;
import ru.gav19770210.javapro.task01.log.TestResult;
import ru.gav19770210.javapro.task01.log.TestStatus;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

public class TestRunner {
    public static List<TestResult> runTests(Class<?> c) {
        /*
         * Список результатов выполнения
         */
        List<TestResult> testResults = new ArrayList<>();
        /*
         * Проверки методов класса
         */
        CheckTestClass checkTestClass = CheckTestClass.builder()
                .testResults(testResults)
                .listTestMethod(method -> method.isAnnotationPresent(BeforeSuite.class) && !Modifier.isStatic(method.getModifiers())
                        ? "Аннотацию @BeforeSuite можно добавлять только на статические методы" : "")
                .listTestMethod(method -> method.isAnnotationPresent(AfterSuite.class) && !Modifier.isStatic(method.getModifiers())
                        ? "Аннотацию @AfterSuite можно добавлять только на статические методы" : "")
                .listTestMethod(method -> method.isAnnotationPresent(Test.class) && Modifier.isStatic(method.getModifiers())
                        ? "Аннотацию @Test нельзя добавлять на статические методы" : "")
                .listTestMethod(method -> method.isAnnotationPresent(Test.class)
                        && (method.getAnnotation(Test.class).priority() < Test.MIN_PRIORITY
                        || method.getAnnotation(Test.class).priority() > Test.MAX_PRIORITY)
                        ? "Значение параметра priority аннотации @Test должно быть в пределах от 1 до 10 включительно" : "")
                .listTestArrMethod(methods -> Arrays.stream(methods).filter(method -> method.isAnnotationPresent(BeforeSuite.class)).count() > 1
                        ? "Методов с аннотацией @BeforeSuite не может быть более одного" : "")
                .listTestArrMethod(methods -> Arrays.stream(methods).filter(method -> method.isAnnotationPresent(AfterSuite.class)).count() > 1
                        ? "Методов с аннотацией @AfterSuite не может быть более одного" : "")
                .listTestArrMethod(methods -> Arrays.stream(methods).filter(method -> method.isAnnotationPresent(BeforeTest.class)).count() > 1
                        ? "Методов с аннотацией @BeforeTest не может быть более одного" : "")
                .listTestArrMethod(methods -> Arrays.stream(methods).filter(method -> method.isAnnotationPresent(AfterTest.class)).count() > 1
                        ? "Методов с аннотацией @AfterTest не может быть более одного" : "")
                .build();

        Method[] methods = c.getDeclaredMethods();
        /*
         * Если проверки прошли не все, то завершение
         */
        if (!checkTestClass.checkTestMethods(methods)) {
            return testResults;
        }

        List<Method> listTestMethods = Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(Test.class))
                .sorted(Comparator.comparingInt(m -> m.getAnnotation(Test.class).priority()))
                .collect(Collectors.toList());

        /*
         * Если нет методов для тестирования, то завершение
         */
        if (listTestMethods.isEmpty()) {
            testResults.add(new TestResult(null, TestStatus.SKIPPED, new Exception("Нет методов для тестирования")));
            return testResults;
        }

        /*
         * Создание экземпляра класса
         */
        Object objectTest;
        try {
            objectTest = c.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            testResults.add(new TestResult(null, TestStatus.ERROR, e));
            return testResults;
        }

        /*
         * Выполнение методов тестирования
         */
        Optional<Method> methodBeforeSuite = Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(BeforeSuite.class))
                .findFirst();

        if (methodBeforeSuite.isPresent()) {
            if (!runMethod(methodBeforeSuite.get(), objectTest, testResults)) {
                return testResults;
            }
        }

        Optional<Method> methodBeforeTest = Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(BeforeTest.class))
                .findFirst();

        Optional<Method> methodAfterTest = Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(AfterTest.class))
                .findFirst();

        boolean runBeforeTestSuccess;

        for (Method methTest : listTestMethods) {
            runBeforeTestSuccess = true;
            if (methodBeforeTest.isPresent()) {
                runBeforeTestSuccess = runMethod(methodBeforeTest.get(), objectTest, testResults);
            }
            if (runBeforeTestSuccess) {
                runMethod(methTest, objectTest, testResults);
            }
            methodAfterTest.ifPresent(method -> runMethod(method, objectTest, testResults));
        }

        Optional<Method> methodAfterSuite = Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(AfterSuite.class))
                .findFirst();

        methodAfterSuite.ifPresent(method -> runMethod(method, objectTest, testResults));

        return testResults;
    }

    private static boolean runMethod(Method method, Object object, List<TestResult> testResults) {
        try {
            if (method.trySetAccessible()) {
                method.invoke(object, getMethodArgs(method));
            }
            testResults.add(new TestResult(method, TestStatus.PASSED, null));
            return true;
        } catch (Exception e) {
            testResults.add(new TestResult(method, TestStatus.ERROR, e));
            return false;
        }
    }

    private static Object[] getMethodArgs(Method method) {
        List<Object> listParamValues = new ArrayList<>();
        if (method.getParameterCount() > 0 && method.isAnnotationPresent(CsvSource.class)) {
            String strParamValues = method.getAnnotation(CsvSource.class).value();
            if (!strParamValues.isEmpty()) {
                Class<?>[] classParamTypes = method.getParameterTypes();
                String[] arrStrParamValues = strParamValues.split(",");
                if (classParamTypes.length == arrStrParamValues.length) {
                    Class<?> classParamType;
                    String strParamValue;
                    for (int i = 0; i < classParamTypes.length; i++) {
                        classParamType = classParamTypes[i];
                        strParamValue = arrStrParamValues[i].trim();
                        listParamValues.add(convertStringToObject(strParamValue, classParamType));
                    }
                }
            }
        }
        return listParamValues.toArray();
    }

    private static Object convertStringToObject(String value, Class<?> toClass) {
        if (toClass.isPrimitive()) {
            if (boolean.class.isAssignableFrom(toClass)) return Boolean.valueOf(value);
            if (byte.class.isAssignableFrom(toClass)) return Byte.valueOf(value);
            if (short.class.isAssignableFrom(toClass)) return Short.valueOf(value);
            if (int.class.isAssignableFrom(toClass)) return Integer.valueOf(value);
            if (long.class.isAssignableFrom(toClass)) return Long.valueOf(value);
            if (float.class.isAssignableFrom(toClass)) return Float.valueOf(value);
            if (double.class.isAssignableFrom(toClass)) return Double.valueOf(value);
            if (char.class.isAssignableFrom(toClass)) return Character.valueOf(value.charAt(0));
        }
        return toClass.cast(value);
    }
}
