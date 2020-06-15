package com.shotin.bankaccountgenerator.generator;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class FromFileNamesGenerator implements NamesGenerator {

    private String firstNamesFilePath;
    private String lastNamesFilePath;
    private String patronymicsFilePath;

    private List<String> firstNames;
    private List<String> lastNames;
    private List<String> patronimics;

    private Random random = new Random(System.nanoTime());

    public String getFirstNamesFilePath() {
        return firstNamesFilePath;
    }

    public void setFirstNamesFilePath(String firstNamesFilePath) {
        this.firstNamesFilePath = firstNamesFilePath;
    }

    public String getLastNamesFilePath() {
        return lastNamesFilePath;
    }

    public void setLastNamesFilePath(String lastNamesFilePath) {
        this.lastNamesFilePath = lastNamesFilePath;
    }

    public String getPatronymicsFilePath() {
        return patronymicsFilePath;
    }

    public void setPatronymicsFilePath(String patronymicsFilePath) {
        this.patronymicsFilePath = patronymicsFilePath;
    }

    @PostConstruct
    protected void readAllNames() {
        firstNames = readNames(firstNamesFilePath);
        lastNames = readNames(lastNamesFilePath);
        patronimics = readNames(patronymicsFilePath);
    }

    protected List<String> readNames(String fileName) {
        if (fileName == null) {
            return new ArrayList<>();
        }
        if (fileName.toLowerCase().startsWith("classpath")) {
            fileName = fileName.replaceAll("^classpath\\*?:", "");
            URL url = getClass().getClassLoader().getResource(fileName);
            if (url == null) {
                throw new RuntimeException("Failed to load names from classpath file: "+fileName+" file does not exist");
            }
            fileName = url.getPath();
        }

        try {
            if (isWindows()) {
                fileName = fileName.replaceAll("^/", "");
            }
            Path filePath;
            try {
                filePath = Paths.get(fileName);
            } catch (Exception e) {
                filePath = Paths.get(fileName);
            }

            return Files.lines(filePath, StandardCharsets.UTF_8)
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Failed to load names from file "+fileName, e);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load names from file "+fileName, e);
        }
    }

    private boolean isWindows() {
        return System.getProperty("os.name").startsWith("Windows");
    }

    @Override
    public String getRandomFirstName() {
        int index = random.nextInt(firstNames.size());
        return firstNames.get(index);
    }

    @Override
    public String getRandomLastName() {
        int index = random.nextInt(lastNames.size());
        return lastNames.get(index);
    }

    @Override
    public String getRandomPatronymic() {
        int index = random.nextInt(patronimics.size());
        return patronimics.get(index);
    }
}
