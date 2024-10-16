package ai.latta.recorder;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;


public class LattaAPI {
    private final String apiKey;
    private static final String NEW_INSTANCE_URL = "https://recording.latta.ai/v1/instance/backend";
    private static final String NEW_SNAPSHOT_URL = "https://recording.latta.ai/v1/snapshot/";
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public LattaAPI(String apiKey) {
        this.apiKey = apiKey;
    }

    public LattaInstance putInstance(String os, String osVersion, String lang, String device, String framework, String frameworkVersion) throws IOException {
        Map<String, String> instanceData = new HashMap<>();
        instanceData.put("os", os);
        instanceData.put("os_version", osVersion);
        instanceData.put("lang", lang);
        instanceData.put("device", device);
        instanceData.put("framework", framework);
        instanceData.put("framework_version", frameworkVersion);

        String requestBody = objectMapper.writeValueAsString(instanceData);

        Request request = new Request.Builder()
                .url(NEW_INSTANCE_URL)
                .addHeader("Authorization", "Bearer " + apiKey)
                .put(RequestBody.create(MediaType.parse("application/json"), requestBody))
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseString = response.body().string();
            return objectMapper.readValue(responseString, LattaInstance.class);
        }
    }

    public void putSnapshot(LattaInstance instance, String message, String relationId, String relatedToRelationId) throws IOException {
        Map<String, String> snapshotData = new HashMap<>();
        snapshotData.put("message", message);
        snapshotData.put("relation_id", relationId != null ? relationId : UUID.randomUUID().toString());
        snapshotData.put("related_to_relation_id", relatedToRelationId);

        String requestBody = objectMapper.writeValueAsString(snapshotData);

        Request request = new Request.Builder()
                .url(NEW_SNAPSHOT_URL + instance.getId())
                .addHeader("Authorization", "Bearer " + apiKey)
                .put(RequestBody.create(MediaType.parse("application/json"), requestBody))
                .build();

        try (Response response = client.newCall(request).execute()) {
            response.body().string(); // Read and close the response body
        }
    }
}
