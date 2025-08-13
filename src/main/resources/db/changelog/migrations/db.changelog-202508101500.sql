--liquibase formatted sql
--changeset joao.vitor.leal:202508101510
--comment: add columns entered_column_at and exited_column_at to cards table

ALTER TABLE CARDS
ADD COLUMN entered_column_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN exited_column_at TIMESTAMP NULL;

--rollback ALTER TABLE CARDS DROP COLUMN entered_column_at, DROP COLUMN exited_column_at;
