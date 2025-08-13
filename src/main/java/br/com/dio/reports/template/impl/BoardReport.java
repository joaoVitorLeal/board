package br.com.dio.reports.template.impl;

import br.com.dio.reports.dto.BoardReportContentDTO;
import br.com.dio.reports.template.BoardReportTemplate;
import br.com.dio.reports.util.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;

public final class BoardReport extends BoardReportTemplate {

    @Override
    public void generate(BoardReportContentDTO contentToFile) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                int yPosition = 750;

                // Título principal
                writeSectionAndTitle(contentStream, BoardReportTemplate.REPORT_TITLE, yPosition, 18);

                // Sessão 1: Dados do Card
                yPosition = writeSectionAndTitle(contentStream, SECTION_CARD_DATA, yPosition - 40, 14);
                yPosition = writeCardData(contentStream, contentToFile,yPosition - 20);

                // Sessão 2: Tempo por tarefa
                yPosition = writeSectionAndTitle(contentStream, SECTION_TASK_TIMES, yPosition - 40, 14);
                yPosition = writeTaskTimes(contentStream, contentToFile,yPosition - 20);

                // Sessão 3: Bloqueios
                yPosition = writeSectionAndTitle(contentStream, SECTION_BLOCKS, yPosition - 40, 14);
                yPosition = writeBlocks(contentStream, contentToFile, yPosition - 20);

                // Sessão 4: Desbloqueios
                yPosition = writeSectionAndTitle(contentStream, SECTION_UNBLOCKS, yPosition - 40, 14);
                writeUnblocks(contentStream, contentToFile, yPosition - 20);
            }
            Path reportOutputPath = FileUtils.buildReportPath();
            document.save(reportOutputPath.toFile());
        } catch (IOException e) {
            System.out.println(e.getMessage() + " : " + e.getCause());
        }
    }



    private int writeSectionAndTitle(PDPageContentStream contentStream, String text, int yPosition, int fontSize)
            throws IOException {
        contentStream.beginText();
        contentStream.setFont(FONT_TITLE_AND_SECTION, fontSize);
        contentStream.newLineAtOffset(50, yPosition);
        contentStream.showText(text);
        contentStream.endText();
        return yPosition;
    }

    private int writeCardData(PDPageContentStream contentStream, BoardReportContentDTO boardReportContentDTO, int yPosition)
            throws IOException {
        contentStream.beginText();
        contentStream.setFont(FONT_BODY_TEXT, 14);
        contentStream.newLineAtOffset(70, yPosition);
        contentStream.showText("Título do Card: " + boardReportContentDTO.cardTitle());
        contentStream.endText();
        return yPosition - 40;
    }

    private int writeTaskTimes(PDPageContentStream contentStream, BoardReportContentDTO boardReportContentDTO, int yPosition)
            throws IOException {
        String durationStr = getDurationStr(
                boardReportContentDTO.cardEnteredColumnAt(),
                boardReportContentDTO.cardExitedColumnAt()
        );
        contentStream.beginText();
        contentStream.setFont(FONT_BODY_TEXT, 14);
        String displayDuration = durationStr != null ? "Duração: " + durationStr : "O card ainda não foi movido";
        contentStream.newLineAtOffset(70, yPosition);
        contentStream.showText(displayDuration);
        contentStream.endText();
        return yPosition - 40;
    }

    private Integer writeBlocks(PDPageContentStream contentStream, BoardReportContentDTO boardReportContentDTO, int yPosition)
            throws IOException {
        String blockReason = boardReportContentDTO.blockReason();
        LocalDateTime blockedAt = boardReportContentDTO.blockedAt();
        contentStream.beginText();
        contentStream.setFont(FONT_BODY_TEXT, 14);
        contentStream.newLineAtOffset(70, yPosition);
        if (blockedAt == null) {
            contentStream.showText("Não há cards bloqueados.");
            contentStream.newLineAtOffset(0, -20);
            contentStream.endText();
            return yPosition - 40;
        }
        contentStream.showText("Card com título '" + boardReportContentDTO.cardTitle() + "', foi bloqueado.");
        contentStream.newLineAtOffset(0, -20);
        contentStream.showText("Bloqueado em: " + blockedAt);
        contentStream.newLineAtOffset(0, -20);
        contentStream.showText("Justificativa de bloqueio: " + blockReason);
        contentStream.endText();
        return yPosition - 40;
    }

    private int writeUnblocks(PDPageContentStream contentStream, BoardReportContentDTO boardReportContentDTO, int yPosition)
            throws IOException {
        String blockReason = boardReportContentDTO.unblockReason();
        LocalDateTime blockedAt = boardReportContentDTO.blockedAt();
        LocalDateTime unblockedAt = boardReportContentDTO.unblockedAt();
        String durationStr = getDurationStr(blockedAt, unblockedAt);
        contentStream.beginText();
        contentStream.setFont(FONT_BODY_TEXT, 14);
        contentStream.newLineAtOffset(70, yPosition);
        if (durationStr != null) {
            contentStream.showText("Card com título '" + boardReportContentDTO.cardTitle() + "', foi desbloqueado.");
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Duração do bloqueio: " + durationStr);
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Justificativa do desbloqueio: " + blockReason);
            contentStream.endText();
            return yPosition - 40;
        }
        contentStream.showText("Não há cards desbloqueados.");
        contentStream.endText();
        return yPosition - 40;
    }

    private String getDurationStr(LocalDateTime beginTime, LocalDateTime endTime) {
        if (beginTime == null) {
            return null;
        }
        if (endTime == null) {
            return null;
        }
        Duration duration = Duration.between(beginTime, endTime);
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;
        return String.format("%dh %dm %ds", hours, minutes, seconds);
    }
}
