package com.playwright.framework.dataproviders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.playwright.framework.utils.JsonDataReader;
import com.playwright.framework.utils.LoggerUtils;
import org.slf4j.Logger;
import org.testng.annotations.DataProvider;

import java.util.List;

public class GenericDataProvider {
    private static final String PRACTICE_FORM_DATA_FILE = "testdata/practice-form.json";
    private static final String TEXT_FORM_DATA_FILE = "testdata/textbox-form.json";
    private static final Logger LOGGER = LoggerUtils.getLogger(GenericDataProvider.class);

    private GenericDataProvider() {
        throw new IllegalStateException("PracticeFormDataProvider must not be instantiated");
    }


    public static <T> Object[][] toTestNgData(List<T> list) {
        Object[][] data = new Object[list.size()][1];
        for (int i = 0; i < list.size(); i++) data[i][0] = list.get(i);
        return data;
    }

    public static <T> Object[][] fromJsonResource(String resourcePath, TypeReference<List<T>> typeRef) {
        LOGGER.info("Preparing TestNG data from {}", resourcePath);
        List<T> list = JsonDataReader.readListFromResource(resourcePath, typeRef);
        return toTestNgData(list);
    }

    @DataProvider(name = "textBoxFormData", parallel = false)
    public static Object[][] textBoxFormData() {
        return GenericDataProvider.fromJsonResource(
                TEXT_FORM_DATA_FILE,
                new TypeReference<List<com.playwright.framework.models.TextBoxData>>() {}
        );
    }

    @DataProvider(name = "practiceFormData", parallel = false)
    public static Object[][] practiceFormData() {
        return GenericDataProvider.fromJsonResource(
                PRACTICE_FORM_DATA_FILE,
                new TypeReference<List<com.playwright.framework.models.PracticeFormData>>() {}
        );
    }



//    @DataProvider(name = "textBoxFormData", parallel = false)
//    @Step("Load textBox form test data")
//    public static Object[][] practiceFormData() {
//        LOGGER.info("Executing DataProvider: textBoxFormData");
//        List<TextBoxData> records = JsonDataReader.readListFromResource(
//                "testdata/textbox-form.json",
//                new TypeReference<List<TextBoxData>>() {}
//        );
//
//        Object[][] data = new Object[records.size()][1];
//
//        for (int index = 0; index < records.size(); index++) {
//            data[index][0] = records.get(index);
//        }
//
//        LOGGER.info("DataProvider prepared {} practice form record(s)", records.size());
//        return data;
//    }
}
