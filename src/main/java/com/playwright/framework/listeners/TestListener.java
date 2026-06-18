package com.playwright.framework.listeners;

import com.microsoft.playwright.Page;
import com.playwright.framework.factory.PlaywrightFactory;
import com.playwright.framework.utils.AllureUtils;
import com.playwright.framework.utils.LoggerUtils;
import org.slf4j.Logger;
import org.testng.ITestListener;
import org.testng.ITestContext;
import org.testng.ITestResult;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Duration;
import java.util.concurrent.atomic.LongAdder;

/**
 * Logs TestNG lifecycle events and captures browser state on test failure.
 */
public final class TestListener implements ITestListener {

    private static final Logger LOGGER = LoggerUtils.getLogger(TestListener.class);
    private final LongAdder passed = new LongAdder();
    private final LongAdder failed = new LongAdder();
    private final LongAdder skipped = new LongAdder();

    @Override
    public void onStart(ITestContext context) {
        LOGGER.info("====================================");
        LOGGER.info("Execution Started");
        LOGGER.info("====================================");
        LOGGER.info("Test context: {}", context.getName());
        AllureUtils.attachEnvironmentInfo();
    }

    @Override
    public void onFinish(ITestContext context) {
        LOGGER.info("====================================");
        LOGGER.info("Execution Finished");
        LOGGER.info("====================================");
        LOGGER.info("Total Passed: {}", passed.sum());
        LOGGER.info("Total Failed: {}", failed.sum());
        LOGGER.info("Total Skipped: {}", skipped.sum());
    }

    @Override
    public void onTestStart(ITestResult result) {
        LOGGER.info("Test Name: {} | Status: STARTED", result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        passed.increment();
        logResult(result, "PASSED");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        failed.increment();
        logResult(result, "FAILED");
        Throwable failure = result.getThrowable();
        LOGGER.error("Failure reason for {}: {}", result.getName(),
                failure == null ? "Unknown" : failure.getMessage(), failure);

        if (failure != null) {
            StringWriter stackTrace = new StringWriter();
            failure.printStackTrace(new PrintWriter(stackTrace));
            AllureUtils.attachText("Stack Trace", stackTrace.toString());
        }

        try {
            Page page = PlaywrightFactory.getPage();
            byte[] screenshot = AllureUtils.attachScreenshot(page, "Failure Screenshot");
            var screenshotPath = AllureUtils.saveScreenshot(screenshot, result.getName());
            AllureUtils.attachPageSource(page, "Page Source");
            LOGGER.error("Failure screenshot captured at: {}", screenshotPath.toAbsolutePath());
        } catch (RuntimeException exception) {
            LOGGER.error(
                    "Unable to collect failure artifacts for test: {}", result.getName(), exception);
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        skipped.increment();
        logResult(result, "SKIPPED");
        if (result.getThrowable() != null) {
            LOGGER.warn("Skip reason for {}: {}", result.getName(),
                    result.getThrowable().getMessage());
        }
    }

    private void logResult(ITestResult result, String status) {
        long duration = Math.max(0L, result.getEndMillis() - result.getStartMillis());
        LOGGER.info(
                "Test Name: {} | Status: {} | Duration: {} ms ({})",
                result.getName(),
                status,
                duration,
                Duration.ofMillis(duration));
    }
}
