package org.example;

import java.sql.Statement;
import java.sql.SQLException;

public class FaculdadeDB {

    private static final String DB_NAME = "ubsb_faculdade";

    public static void main(String[] args) {
        // Simulação ou outros processos podem ser adicionados aqui no lugar da lógica de conexão

        // Criar as tabelas
        criarTabelas();

        // Inserir dados nas tabelas
        inserirDados();
    }

    private static void criarTabelas() {
        // Código SQL para criar tabelas
        String createDepartamento = "CREATE TABLE IF NOT EXISTS Departamento (" +
                "codDepto INT PRIMARY KEY AUTO_INCREMENT," +
                "nome VARCHAR(100) NOT NULL)";
        System.out.println(createDepartamento);

        String createCurso = "CREATE TABLE IF NOT EXISTS Curso (" +
                "codCurso INT PRIMARY KEY AUTO_INCREMENT," +
                "nome VARCHAR(100) NOT NULL)";
        System.out.println(createCurso);

        String createProfessor = "CREATE TABLE IF NOT EXISTS Professor (" +
                "codProfessor INT PRIMARY KEY AUTO_INCREMENT," +
                "nome VARCHAR(100) NOT NULL," +
                "departamento INT," +
                "FOREIGN KEY (departamento) REFERENCES Departamento(codDepto))";
        System.out.println(createProfessor);

        String createAluno = "CREATE TABLE IF NOT EXISTS Aluno (" +
                "codRA INT PRIMARY KEY AUTO_INCREMENT," +
                "nome VARCHAR(100) NOT NULL," +
                "data_nascimento DATE NOT NULL)";
        System.out.println(createAluno);

        String createDisciplina = "CREATE TABLE IF NOT EXISTS Disciplina (" +
                "codDisciplina INT PRIMARY KEY AUTO_INCREMENT," +
                "nome VARCHAR(100) NOT NULL," +
                "obrigatoria BOOLEAN," +
                "departamento INT," +
                "FOREIGN KEY (departamento) REFERENCES Departamento(codDepto))";
        System.out.println(createDisciplina);

        String createTurma = "CREATE TABLE IF NOT EXISTS Turma (" +
                "codTurma INT PRIMARY KEY AUTO_INCREMENT," +
                "aluno INT," +
                "disciplina INT," +
                "semestre INT NOT NULL," +
                "FOREIGN KEY (aluno) REFERENCES Aluno(codRA)," +
                "FOREIGN KEY (disciplina) REFERENCES Disciplina(codDisciplina)," +
                "UNIQUE (aluno, disciplina, semestre))";
        System.out.println(createTurma);

        String createHistorico = "CREATE TABLE IF NOT EXISTS Historico (" +
                "codHist INT PRIMARY KEY AUTO_INCREMENT," +
                "aluno INT," +
                "disciplina INT," +
                "nota_final DECIMAL(3, 2)," +
                "frequencia DECIMAL(5, 2)," +
                "periodo VARCHAR(10)," +
                "FOREIGN KEY (aluno) REFERENCES Aluno(codRA)," +
                "FOREIGN KEY (disciplina) REFERENCES Disciplina(codDisciplina))";
        System.out.println(createHistorico);
    }

    private static void inserirDados() {
        // Código SQL para inserir dados
        String insertDepto = "INSERT INTO Departamento (nome) VALUES ('Ciências Exatas'), ('Ciências Humanas')";
        System.out.println(insertDepto);

        String insertProfessor = "INSERT INTO Professor (nome, departamento) VALUES ('Carlos Silva', 1), ('Ana Costa', 2)";
        System.out.println(insertProfessor);

        String insertCurso = "INSERT INTO Curso (nome) VALUES ('Engenharia de Software'), ('História')";
        System.out.println(insertCurso);

        String insertAluno = "INSERT INTO Aluno (nome, data_nascimento) VALUES ('Ana Souza', '2001-06-15'), ('João Oliveira', '2000-10-25')";
        System.out.println(insertAluno);

        String insertDisciplina = "INSERT INTO Disciplina (nome, obrigatoria, departamento) " +
                "VALUES ('Cálculo I', TRUE, 1), ('História Moderna', TRUE, 2)";
        System.out.println(insertDisciplina);

        String insertTurma = "INSERT INTO Turma (aluno, disciplina, semestre) VALUES (1, 1, 20241), (2, 2, 20241)";
        System.out.println(insertTurma);

        String insertHistorico = "INSERT INTO Historico (aluno, disciplina, nota_final, frequencia, periodo) " +
                "VALUES (1, 1, 8.5, 90, '2024-1'), (2, 2, 7.0, 85, '2024-1')";
        System.out.println(insertHistorico);
    }
}
