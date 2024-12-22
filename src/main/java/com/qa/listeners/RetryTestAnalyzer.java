package com.qa.listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryTestAnalyzer implements IRetryAnalyzer {
    private int retryCount = 0;

    @Override
    public boolean retry(ITestResult result) {
        int failureCountLimit = 1;
        if (retryCount < failureCountLimit) {
            retryCount++;
            return true;
        }
        return false;
    }
}
