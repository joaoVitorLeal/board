package br.com.dio;

import br.com.dio.persistence.migration.MigrationStrategy;
import br.com.dio.ui.MainMenu;

import java.io.IOException;
import java.sql.SQLException;

import static br.com.dio.persistence.config.ConnectionConfig.getConnection;


public final class Main {

    public static void main(String[] args) throws SQLException, IOException {
        try(var connection = getConnection()){
            new MigrationStrategy(connection).executeMigration();
        }
        new MainMenu().execute();
    }
}
