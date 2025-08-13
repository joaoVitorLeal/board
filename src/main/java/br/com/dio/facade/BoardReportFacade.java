package br.com.dio.facade;

import br.com.dio.reports.dto.BoardReportContentDTO;
import br.com.dio.reports.template.BoardReportTemplate;
import br.com.dio.reports.template.impl.BoardReport;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public class BoardReportFacade {

    private final BoardReportTemplate boardReport;
    private final Connection conn;

    public BoardReportFacade(Connection conn, BoardReport boardReport) {
        this.boardReport = boardReport;
        this.conn = conn;
    }

    public void registerReportDataByCardId(Long cardId) {
        final String sql = """
                    SELECT
                        bc.name AS column_name,
                        c.title AS card_title,
                        cah.entered_at AS entered_at,
                        cah.exited_at AS exited_at,
                        b.blocked_at AS blocked_at,
                        b.block_reason AS block_reason,
                        b.unblocked_at AS unblocked_at,
                        b.unblock_reason AS unblock_reason
                    FROM CARD_ACTIVITY_HISTORY cah
                    LEFT JOIN BOARDS_COLUMNS bc ON cah.board_column_id = bc.id
                    LEFT JOIN CARDS c ON cah.card_id = c.id
                    LEFT JOIN BLOCKS b ON cah.card_id = b.card_id
                    WHERE c.id = ? AND cah.exited_at IS NOT NULL
                    ORDER BY cah.entered_at
                """;
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setLong(1, cardId); // Use o ID do card que quer

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    BoardReportContentDTO boardReportContentDTO = new BoardReportContentDTO(
                            resultSet.getString("column_name"),
                            resultSet.getString("card_title"),
                            toLocalDateTime(resultSet.getTimestamp("entered_at")),
                            toLocalDateTime(resultSet.getTimestamp("exited_at")),
                            toLocalDateTime(resultSet.getTimestamp("blocked_at")),
                            resultSet.getString("block_reason"),
                            toLocalDateTime(resultSet.getTimestamp("unblocked_at")),
                            resultSet.getString("unblock_reason")
                    );
                    boardReport.generate(boardReportContentDTO);
                }
            }
        } catch (SQLException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void registerReportData() {
        final String sql = """
                    SELECT
                        bc.name AS column_name,
                        c.title AS card_title,
                        cah.entered_at AS entered_at,
                        cah.exited_at AS exited_at,
                        b.blocked_at AS blocked_at,
                        b.block_reason AS block_reason,
                        b.unblocked_at AS unblocked_at,
                        b.unblock_reason AS unblock_reason
                    FROM CARD_ACTIVITY_HISTORY cah
                    LEFT JOIN BOARDS_COLUMNS bc ON cah.board_column_id = bc.id
                    LEFT JOIN CARDS c ON cah.card_id = c.id
                    LEFT JOIN BLOCKS b ON cah.card_id = b.card_id
                    WHERE cah.exited_at IS NOT NULL
                    ORDER BY cah.entered_at
                    LIMIT 1
                """;
        try ( PreparedStatement statement = conn.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery() ){


            while (resultSet.next()) {
                LocalDateTime exitedAt = toLocalDateTime(resultSet.getTimestamp("exited_at"));

                if (exitedAt == null) {
                    continue;
                }
                BoardReportContentDTO boardReportContentDTO = new BoardReportContentDTO(
                        resultSet.getString("column_name"),
                        resultSet.getString("card_title"),
                        toLocalDateTime(resultSet.getTimestamp("entered_at")),
                        exitedAt,
                        toLocalDateTime(resultSet.getTimestamp("blocked_at")),
                        resultSet.getString("block_reason"),
                        toLocalDateTime(resultSet.getTimestamp("unblocked_at")),
                        resultSet.getString("unblock_reason")
                );
                boardReport.generate(boardReportContentDTO);
            }

        } catch (SQLException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private LocalDateTime toLocalDateTime(Timestamp timestamp) {
        return timestamp != null ? timestamp.toLocalDateTime() : null;
    }

    private Timestamp toTimestamp(OffsetDateTime offsetDateTime) {
        return Timestamp.from(offsetDateTime.toInstant());
    }

}
