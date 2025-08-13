--liquibase formatted sql
--changeset joao.vitor.leal:202508121525 endDelimiter:$$
--comment: add action after update command in CARDS table to record column changes

CREATE TRIGGER TRG_CARD_ACTIVITY_HISTORY_UPDATE
AFTER UPDATE ON CARDS
FOR EACH ROW
BEGIN
    -- Somente executa se a coluna mudou
    IF OLD.board_column_id <> NEW.board_column_id THEN
        -- Atualiza a data de saída da coluna anterior
        UPDATE CARD_ACTIVITY_HISTORY
        SET exited_at = NOW()
        WHERE card_id = OLD.id
          AND board_column_id = OLD.board_column_id
          AND exited_at IS NULL;


        -- Insere o registro de entrada na nova coluna
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
    END IF;
END;

--rollback DROP TRIGGER TRG_CARD_ACTIVITY_HISTORY_UPDATE;
