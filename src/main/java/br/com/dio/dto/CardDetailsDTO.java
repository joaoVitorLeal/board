package br.com.dio.dto;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public record CardDetailsDTO(
        Long id,
        String title,
        String description,
        boolean blocked,
        OffsetDateTime blockedAt,
        String blockReason,
        int blocksAmount,
        Long columnId,
        String columnName,
        LocalDateTime enteredColumnAt,
        boolean isExitedColumn,
        LocalDateTime exitedColumnAt
) {
}
