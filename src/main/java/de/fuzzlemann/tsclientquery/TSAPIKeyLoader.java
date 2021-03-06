package de.fuzzlemann.tsclientquery;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TSAPIKeyLoader {

    private final String[] teamSpeakClientNames = new String[]{"TS3Client", "TeamSpeak 3 Client", "TeamSpeak", "TeamSpeak 3", "ts3"};
    private final List<File> possibleConfigDirectories = new ArrayList<>();

    public Optional<String> loadAPIKey() throws IOException {
        loadPossibleConfigDirectories();
        for (File possibleConfigDirectory : possibleConfigDirectories) {
            File clientQueryIni = new File(possibleConfigDirectory, "clientquery.ini");
            if (!clientQueryIni.exists()) continue;

            String apiKey = loadAPIKey(clientQueryIni);
            if (apiKey == null) continue;

            return Optional.of(apiKey);
        }

        return Optional.empty();
    }

    private String loadAPIKey(File clientQueryIni) throws IOException {
        for (String line : FileUtils.readLines(clientQueryIni, StandardCharsets.UTF_8)) {
            if (!line.startsWith("api_key")) continue;

            String apiKey = line.split("=")[1];
            if (apiKey.length() != 29) continue;

            return apiKey;
        }

        return null;
    }

    private void loadPossibleConfigDirectories() {
        List<File> possibleParentFolders = new ArrayList<>();

        if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
            File appData = new File(System.getenv("AppData"));
            File local = new File(appData.getParentFile(), "Local");
            File programFilesX86 = new File(System.getenv("ProgramFiles(X86)"));
            File programFiles = new File(System.getenv("ProgramFiles"));

            possibleParentFolders.add(appData);
            possibleParentFolders.add(local);
            possibleParentFolders.add(programFilesX86);
            possibleParentFolders.add(programFiles);
        } else {
            File userHome = new File(System.getProperty("user.home"));
            File library = new File(userHome, "Library");
            File applicationSupport = new File(library, "Application Support");

            possibleParentFolders.add(userHome);
            possibleParentFolders.add(library);
            possibleParentFolders.add(applicationSupport);
        }

        List<File> possibleTeamSpeakFolders = new ArrayList<>();
        for (File possibleParentFolder : possibleParentFolders) {
            for (String teamSpeakClientName : teamSpeakClientNames) {
                possibleTeamSpeakFolders.add(new File(possibleParentFolder, teamSpeakClientName));
            }
        }

        for (File possibleTeamSpeakFolder : possibleTeamSpeakFolders) {
            possibleConfigDirectories.add(new File(possibleTeamSpeakFolder, "config"));
        }

        possibleConfigDirectories.removeIf(config -> !config.exists());
    }
}
