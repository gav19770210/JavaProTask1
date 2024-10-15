package ru.gav19770210.javapro.task01.log;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.lang.reflect.Method;

@AllArgsConstructor
@Getter
@ToString
public class TestResult {
    private Method method;
    private TestStatus testStatus;
    private Throwable message;
}
