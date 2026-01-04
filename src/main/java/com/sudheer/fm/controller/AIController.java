package com.sudheer.fm.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ai")
public class AIController {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    //    @PostMapping(
//            value = "/summarize",
//            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE
//    )
//    public ResponseEntity<Map<String, String>> summarize(
//            @RequestParam("file") MultipartFile file
//    ) {
//
//        try {
//            /* ================= 1. EXTRACT PDF TEXT ================= */
//            PDDocument doc = PDDocument.load(file.getInputStream());
//            PDFTextStripper stripper = new PDFTextStripper();
//            String text = stripper.getText(doc);
//            doc.close();
//
//            if (text.length() > 12000) {
//                text = text.substring(0, 12000); // Gemini safety limit
//            }
//
//            /* ================= 2. GEMINI PAYLOAD ================= */
//            Map<String, Object> payload = Map.of(
//                    "contents", List.of(
//                            Map.of(
//                                    "parts", List.of(
//                                            Map.of("text", "Summarize this PDF clearly:\n\n" + text)
//                                    )
//                            )
//                    )
//            );
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//
//            HttpEntity<Map<String, Object>> entity =
//                    new HttpEntity<>(payload, headers);
//
//            String url =
//                    "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key="
//                            + apiKey;
//
//
//            /* ================= 3. CALL GEMINI ================= */
//            ResponseEntity<String> response =
//                    restTemplate.postForEntity(url, entity, String.class);
//
//            /* ================= 4. PARSE RESPONSE ================= */
//            String body = response.getBody();
//
//            JsonNode root = mapper.readTree(body);
//
//            String summary =
//                    root.path("candidates")
//                            .path(0)
//                            .path("content")
//                            .path("parts")
//                            .path(0)
//                            .path("text")
//                            .asText("No summary generated");
//
//            /* ================= 5. RETURN CLEAN JSON ================= */
//            return ResponseEntity.ok(
//                    Map.of("summary", summary)
//            );
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Map.of("error", e.getMessage()));
//        }
//    }
//}
    @PostMapping(
            value = "/summarize",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Map<String, String>> summarize(
            @RequestParam("file") MultipartFile file) {

        try {
            PDDocument doc = PDDocument.load(file.getInputStream());
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(doc);
            doc.close();

            if (text.length() > 12000) {
                text = text.substring(0, 12000);
            }

            Map<String, Object> payload = Map.of(
                    "contents", List.of(
                            Map.of("parts", List.of(
                                    Map.of("text", "Summarize this PDF clearly:\n\n" + text)
                            ))
                    )
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity =
                    new HttpEntity<>(payload, headers);

            String url =
                    "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key="
                            + apiKey;


            ResponseEntity<String> response =
                    restTemplate.postForEntity(url, entity, String.class);

            String body = response.getBody();
            System.out.println("🔴 Gemini raw response:\n" + body);

            JsonNode root = mapper.readTree(body);

            String summary = root.path("candidates")
                    .path(0)
                    .path("content")
                    .path("parts")
                    .path(0)
                    .path("text")
                    .asText("No summary generated");

            return ResponseEntity.ok(Map.of("summary", summary));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}