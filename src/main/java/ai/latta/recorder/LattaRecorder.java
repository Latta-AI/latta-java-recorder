package ai.latta.recorder;

import java.io.IOException;
import java.util.Locale;
import java.util.UUID;
import ai.latta.recorder.LattaAPI;
import ai.latta.recorder.LattaInstance;

public class LattaRecorder {
    public static void recordApplication(String apiKey, Runnable mainMethod) {
        LattaAPI api = new LattaAPI(apiKey);

        try {
            mainMethod.run();
        } catch (Exception ex) {
            try {
                LattaInstance lattaInstance = api.putInstance(
                        System.getProperty("os.name"),
                        System.getProperty("os.version"),
                        Locale.getDefault().getLanguage(),
                        "desktop",
                        "Java",
                        System.getProperty("java.version")
                );

                if (lattaInstance != null) {
                    api.putSnapshot(lattaInstance, ex.getMessage() + "\n" + getStackTraceAsString(ex), null, null);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String getStackTraceAsString(Exception ex) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : ex.getStackTrace()) {
            sb.append(element.toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}