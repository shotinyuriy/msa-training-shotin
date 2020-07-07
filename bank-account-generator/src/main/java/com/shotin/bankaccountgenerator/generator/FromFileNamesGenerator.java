package com.shotin.bankaccountgenerator.generator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class FromFileNamesGenerator implements NamesGenerator {

    Logger LOG = LoggerFactory.getLogger(FromFileNamesGenerator.class);

    private String CHARSET = StandardCharsets.UTF_8.name();
    private String firstNamesFilePath;
    private String lastNamesFilePath;
    private String patronymicsFilePath;

    private List<String> firstNames;
    private List<String> lastNames;
    private List<String> patronymics;

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
        patronymics = readNames(patronymicsFilePath);
    }

    protected List<String> readNames(String fileName) {
        if (fileName == null) {
            return new ArrayList<>();
        }

        if (fileName.toLowerCase().startsWith("classpath:")) {
            fileName = fileName.replaceFirst("classpath:","");
            ClassPathResource classPathResource = new ClassPathResource(fileName);
            try {
                fileName = classPathResource.getURL().getPath();
                LOG.info("CLASSPATH RESOURCE URL="+fileName);
                try (InputStream inputStream = classPathResource.getInputStream();
                    InputStreamReader reader = new InputStreamReader(inputStream, CHARSET);
                    BufferedReader bufferedReader = new BufferedReader(reader)) {
                    LOG.info("READING FROM CLASSPATH RESOURCE URL="+fileName);
                    return bufferedReader.lines().collect(Collectors.toList());
                }
            } catch (IOException e) {
                LOG.error("FAILED TO LOAD CLASSPATH RESOURCE FILE="+fileName+" EXCEPTION="+e);
            }
        }

        try {
            if (isWindows()) {
                fileName = fileName.replaceAll("^/", "");
            }
            Path filePath;
                filePath = Paths.get(fileName);
                return Files.lines(filePath, StandardCharsets.UTF_8)
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.toList());
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Failed to load names from file "+fileName, e);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load names from file "+fileName, e);
        } catch (Exception e) {
            LOG.error("FAILED TO READ FILE="+fileName);
            return new ArrayList<>();
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
        int index = random.nextInt(patronymics.size());
        return patronymics.get(index);
    }
}
