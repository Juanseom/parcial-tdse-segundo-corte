package edu.eci.tdse.parcial;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ProxyService {

    private final List<String> backendUrls;
    private final AtomicInteger index = new AtomicInteger(0);

    public ProxyService(@Value("${BACKEND_URLS:}") String backendUrlsEnv,
                        @Value("${server.port:8080}") String serverPort) {
        this.backendUrls = parseUrls(backendUrlsEnv, serverPort);
    }

    public ResponseEntity<String> delegateGet(long value) {
        return delegate("GET", value);
    }

    public ResponseEntity<String> delegatePost(long value) {
        return delegate("POST", value);
    }

    private ResponseEntity<String> delegate(String method, long value) {
        String backendBaseUrl = nextBackendUrl();
        String fullUrl = backendBaseUrl + "?value=" + value;

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(fullUrl).openConnection();
            connection.setRequestMethod(method);
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);
            connection.setRequestProperty("Accept", "application/json");

            int statusCode = connection.getResponseCode();
            BufferedReader reader;
            if (statusCode >= 200 && statusCode < 400) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8));
            }

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            return ResponseEntity.status(statusCode).body(response.toString());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body("{\"error\":\"No se pudo conectar con backend\"}");
        }
    }

    private String nextBackendUrl() {
        int current = Math.abs(index.getAndIncrement());
        return backendUrls.get(current % backendUrls.size());
    }

    private List<String> parseUrls(String backendUrlsEnv, String serverPort) {
        List<String> urls = new ArrayList<>();

        if (backendUrlsEnv != null && !backendUrlsEnv.isBlank()) {
            String[] parts = backendUrlsEnv.split(",");
            for (String part : parts) {
                String trimmed = part.trim();
                if (!trimmed.isEmpty()) {
                    urls.add(trimmed);
                }
            }
        }

        if (urls.isEmpty()) {
            urls.add("http://localhost:" + serverPort + "/api/lucasseq");
        }

        return urls;
    }
}
