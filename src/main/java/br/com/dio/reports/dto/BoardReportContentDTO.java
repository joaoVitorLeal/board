package br.com.dio.reports.dto;

import java.time.LocalDateTime;

public record BoardReportContentDTO(
        String boardColumnName,
        String cardTitle,
        LocalDateTime cardEnteredColumnAt,
        LocalDateTime cardExitedColumnAt,
        LocalDateTime blockedAt,
        String blockReason,
        LocalDateTime unblockedAt,
        String unblockReason
    ) {

    public BoardReportContentDTO(){
        this("", "", null, null, null, "", null, "");
    }
}
