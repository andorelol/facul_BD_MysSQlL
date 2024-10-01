package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // URL de conexão para MySQL
    private static final String DB_URL = "jdbc:mysql://localhost:3306/andre";
    private static final String USER = "root";
    private static final String PASSWORD = "andre";

    // Método para obter a conexão com o banco de dados
    public static Connection getConnection() {
        Connection connection = null;
        try {
            // Tentativa de estabelecer a conexão
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            System.out.println("Conexão estabelecida com sucesso!");
        } catch (SQLException e) {
            // Tratamento da exceção em caso de erro na conexão
            System.err.println("Erro ao conectar ao banco de dados: " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }
}
