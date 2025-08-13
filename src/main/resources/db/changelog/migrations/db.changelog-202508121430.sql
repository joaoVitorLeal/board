--liquibase formatted sql
--changeset joao.vitor.leal:202508121430 endDelimiter:$
--comment: add action after insert command in card table

DELIMITER $

CREATE TRIGGER TRG_CARD_ACTIVITY_HISTORY_INSERT
AFTER INSERT ON CARDS
FOR EACH ROW
BEGIN
    INSERT INTO CARD_ACTIVITY_HISTORY (
        card_id,
        board_column_id,
        entered_at,
        exited_at
    ) VALUES (
        NEW.id,
        NEW.board_column_id,
        NOW(),
        NULL
    );
END$
DELIMITER ;

--rollback DROP TRIGGER TRG_CARD_ACTIVITY_HISTORY_INSERT;