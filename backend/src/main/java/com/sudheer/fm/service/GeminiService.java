package com.sudheer.fm.service;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private static final String GEMINI_URL =
            "https://generativelanguage.googleapis.com/v1/models/gemini-2.5-flash:generateContent?key=";

    private final OkHttpClient client = new OkHttpClient();

    public String summarizeText(String text) throws IOException {
        return callGemini("Summarize in simple student-friendly words:\n" + text);
    }

    public String explainSimply(String text) throws IOException {
        return callGemini("Explain this topic in very simple words with examples:\n" + text);
    }

    private String callGemini(String prompt) throws IOException {

        String jsonBody = """
        {
          "contents": [{
            "parts": [{
              "text": "%s"
            }]
          }]
        }
        """.formatted(prompt.replace("\"", "\\\""));

        Request request = new Request.Builder()
                .url(GEMINI_URL + apiKey)
                .post(RequestBody.create(
                        jsonBody,
                        MediaType.parse("application/json")
                ))
                .build();

        try (Response response = client.newCall(request).execute()) {

            if (!response.isSuccessful() || response.body() == null) {
                throw new IOException("Gemini API failed: HTTP " + response.code());
            }

            return extractText(response.body().string());
        }
    }

    private String extractText(String json) {
        String marker = "\"text\": \"";
        int start = json.indexOf(marker);
        if (start == -1) return "No response from AI";

        start += marker.length();
        int end = json.indexOf("\"", start);
        return json.substring(start, end)
                .replace("\\n", "\n")
                .replace("\\\"", "\"");
    }
}
