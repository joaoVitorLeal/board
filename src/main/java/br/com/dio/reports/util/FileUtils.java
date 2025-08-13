package br.com.dio.reports.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.IOException;

public class FileUtils {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
    private static final String PREFIX = "board_report_V";
    private static final String SUFFIX = ".pdf";
    private static final String BASE_DIR = "build";
    private static final String TARGET_DIR = "board-reports";

    public static Path buildReportPath() throws IOException {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String fileName = PREFIX + timestamp + SUFFIX;
        Path outputPath = Paths.get(BASE_DIR, TARGET_DIR, fileName);
        Files.createDirectories(outputPath.getParent()); // garante criação da pasta
        return outputPath;
    }
}
