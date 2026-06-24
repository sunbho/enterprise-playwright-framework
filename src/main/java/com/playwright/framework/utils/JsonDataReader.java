package com.playwright.framework.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.playwright.framework.models.PracticeFormData;
import com.playwright.framework.models.TextBoxData;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Reads typed JSON test data from classpath resources.
 */
public final class JsonDataReader {

    private static final Logger LOGGER = LoggerUtils.getLogger(JsonDataReader.class);
    private static final String PRACTICE_FORM_DATA_FILE = "testdata/practice-form.json";
    private static final String TEXT_FORM_DATA_FILE = "testdata/textbox-form.json";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private JsonDataReader() {
        throw new IllegalStateException("JsonDataReader must not be instantiated");
    }

    /**
     * Reads practice form test data from {@code src/test/resources/testdata/practice-form.json}.
     *
     * @return list of practice form records
     */
//    public static List<PracticeFormData> readPracticeFormData() {
//        LOGGER.info("Loading JSON test data from {}", PRACTICE_FORM_DATA_FILE);
//
//        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//        try (InputStream inputStream = classLoader.getResourceAsStream(PRACTICE_FORM_DATA_FILE)) {
//            if (inputStream == null) {
//                throw new IllegalStateException(
//                        "Test data file not found on classpath: " + PRACTICE_FORM_DATA_FILE);
//            }
//
//            List<PracticeFormData> data = OBJECT_MAPPER.readValue(
//                    inputStream, new TypeReference<List<PracticeFormData>>() {
//                    });
//            LOGGER.info("Loaded {} practice form test record(s)", data.size());
//            return data;
//        } catch (IOException exception) {
//            LOGGER.error("Unable to read JSON test data from {}", PRACTICE_FORM_DATA_FILE, exception);
//            throw new IllegalStateException(
//                    "Unable to read JSON test data from " + PRACTICE_FORM_DATA_FILE, exception);
//        }
//    }
//
//    public static List<TextBoxData> readTextBoxData() {
//        LOGGER.info("Loading JSON test data from {}", TEXT_FORM_DATA_FILE);
//
//        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//        try (InputStream inputStream = classLoader.getResourceAsStream(TEXT_FORM_DATA_FILE)) {
//            if (inputStream == null) {
//                throw new IllegalStateException(
//                        "Test data file not found on classpath: " + TEXT_FORM_DATA_FILE);
//            }
//
//            List<TextBoxData> data = OBJECT_MAPPER.readValue(
//                    inputStream, new TypeReference<List<TextBoxData>>() {
//                    });
//            LOGGER.info("Loaded {} practice form test record(s)", data.size());
//            return data;
//        } catch (IOException exception) {
//            LOGGER.error("Unable to read JSON test data from {}", TEXT_FORM_DATA_FILE, exception);
//            throw new IllegalStateException(
//                    "Unable to read JSON test data from " + TEXT_FORM_DATA_FILE, exception);
//        }
//    }

    public static <T> List<T> readListFromResource(String resourcePath, TypeReference<List<T>> typeRef) {
        LOGGER.info("Loading JSON test data from {}", resourcePath);

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new IllegalStateException("Test data file not found on classpath: " + resourcePath);
            }

            List<T> data = OBJECT_MAPPER.readValue(inputStream, typeRef);
            LOGGER.info("Loaded {} record(s) from {}", data.size(), resourcePath);
            return data;
        } catch (IOException exception) {
            LOGGER.error("Unable to read JSON test data from {}", resourcePath, exception);
            throw new IllegalStateException("Unable to read JSON test data from " + resourcePath, exception);
        }
    }
}
