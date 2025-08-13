--liquibase formatted sql
--changeset joao.vitor.leal:202508121424
--comment: add CARD_ACTIVITY_HISTORY table for reports

CREATE TABLE CARD_ACTIVITY_HISTORY (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Unique record identifier.',
    card_id BIGINT NOT NULL COMMENT 'Card identifier.',
    board_column_id BIGINT NOT NULL COMMENT 'Board column identifier.',
    entered_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Moment the card entered the column.',
    exited_at TIMESTAMP NULL COMMENT 'Moment the card exited the column.',
    PRIMARY KEY (id),
    CONSTRAINT FK_CARD_ACTIVITY_CARD FOREIGN KEY (card_id) REFERENCES CARDS(id),
    CONSTRAINT FK_CARD_ACTIVITY_BOARD_COLUMN FOREIGN KEY (board_column_id) REFERENCES BOARDS_COLUMNS(id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COMMENT = 'Card activity history table for reports';

CREATE INDEX IDX_CARD_ACTIVITY_CARD_ID ON CARD_ACTIVITY_HISTORY(card_id);
CREATE INDEX IDX_CARD_ACTIVITY_BOARD_COLUMN_ID ON CARD_ACTIVITY_HISTORY(board_column_id);
CREATE INDEX IDX_CARD_ACTIVITY_ENTERED_AT ON CARD_ACTIVITY_HISTORY(entered_at);

--rollback DROP TABLE CARD_ACTIVITY_HISTORY;
