package framework.reporting;

import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;
import io.restassured.response.Response;
import com.google.gson.GsonBuilder;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.HashMap;
import java.util.Map;

public class AllureManager {

    private static final ThreadLocal<ByteArrayOutputStream> requestCapture = new ThreadLocal<>();
    private static final ThreadLocal<ByteArrayOutputStream> responseCapture = new ThreadLocal<>();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void captureRequest(String request) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            // Try to pretty print if it's JSON
            String prettifiedBody = tryPrettyPrintJson(request);
            outputStream.write(prettifiedBody.getBytes(StandardCharsets.UTF_8));
            requestCapture.set(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void captureResponse(Response response) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            // Capture status line
            String statusLine = String.format("HTTP/1.1 %s %s\n",
                    response.getStatusCode(),
                    response.getStatusLine());
            outputStream.write(statusLine.getBytes(StandardCharsets.UTF_8));

            // Capture headers
            response.getHeaders().forEach(header -> {
                try {
                    outputStream.write(String.format("%s: %s\n",
                            header.getName(),
                            header.getValue()).getBytes(StandardCharsets.UTF_8));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            // Capture body
            outputStream.write("\n".getBytes(StandardCharsets.UTF_8));
            String bodyStr = response.getBody().asString();
            String prettifiedBody = tryPrettyPrintJson(bodyStr);
            outputStream.write(prettifiedBody.getBytes(StandardCharsets.UTF_8));

            responseCapture.set(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String tryPrettyPrintJson(String input) {
        try {
            // Try to parse and pretty print as JSON
            Object json = gson.fromJson(input, Object.class);
            return gson.toJson(json);
        } catch (Exception e) {
            // If not valid JSON, return original string
            return input;
        }
    }

    public static void attachRequestResponse() {
        if (requestCapture.get() != null) {
            Allure.attachment("Request",
                    new String(requestCapture.get().toByteArray(), StandardCharsets.UTF_8));
        }

        if (responseCapture.get() != null) {
            Allure.attachment("Response",
                    new String(responseCapture.get().toByteArray(), StandardCharsets.UTF_8));
        }
    }

    public static void addStep(String name, Status status, String... details) {
        StepResult result = new StepResult()
                .setName(name)
                .setStatus(status);

        String uuid = UUID.randomUUID().toString();
        Allure.getLifecycle().startStep(uuid, result);

        for (String detail : details) {
            Allure.attachment(name + " Details", detail);
        }

        Allure.getLifecycle().stopStep(uuid);
    }

    public static void addApiInfo(String method, String endpoint, Map<String, String> headers, String body) {
        String prettyBody = tryPrettyPrintJson(body);
        addStep("API Request Details", Status.PASSED,
                String.format("Method: %s\nEndpoint: %s\nHeaders: %s\nBody: %s",
                        method, endpoint, headers, prettyBody));
    }

    public static void addEnvironmentInfo() {
        try {
            Map<String, String> environmentInfo = new HashMap<>();
            environmentInfo.put("Environment", System.getProperty("env", "QA"));
            environmentInfo.put("Base URL", System.getProperty("base.url"));
            environmentInfo.put("Test Execution Time", java.time.LocalDateTime.now().toString());
            environmentInfo.put("Test Type", "API Test");

            // Using the correct method to add environment info
            Allure.addAttachment("Environment Info", "text/plain",
                    gson.toJson(environmentInfo));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void cleanupThreadLocal() {
        requestCapture.remove();
        responseCapture.remove();
    }
}
