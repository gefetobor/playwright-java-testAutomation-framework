package com.testautomation.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class TestDataManager {
    private static final Logger logger = LogManager.getLogger(TestDataManager.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static Map<String, Object> testData;

    static {
        loadTestData();
    }

    private static void loadTestData() {
        try {
            InputStream inputStream = TestDataManager.class.getClassLoader()
                    .getResourceAsStream("testdata.json");
            testData = objectMapper.readValue(inputStream, new TypeReference<>() {
            });
            logger.info("Test data loaded successfully");
        } catch (Exception e) {
            logger.error("Failed to load test data: {}", e.getMessage());
            throw new RuntimeException("Failed to load test data", e);
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Map<String, String>> getTestUsers() {
        return (List<Map<String, String>>) testData.get("testUsers");
    }

    @SuppressWarnings("unchecked")
    public static List<Map<String, String>> getInvalidUsers() {
        return (List<Map<String, String>>) testData.get("invalidUsers");
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> getTestData() {
        return (Map<String, Object>) testData.get("testData");
    }

    @SuppressWarnings("unchecked")
    public static Map<String, String> getUrls() {
        Map<String, Object> testDataMap = getTestData();
        return (Map<String, String>) testDataMap.get("urls");
    }

    @SuppressWarnings("unchecked")
    public static List<String> getSearchTerms() {
        Map<String, Object> testDataMap = getTestData();
        return (List<String>) testDataMap.get("searchTerms");
    }

    public static String getUrl(String key) {
        return getUrls().get(key);
    }

    public static Map<String, String> getValidUser() {
        return getTestUsers().get(0);
    }

    public static Map<String, String> getInvalidUser(int index) {
        return getInvalidUsers().get(index);
    }
}
