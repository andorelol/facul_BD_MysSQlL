package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FaculdadeDAO {
    private Connection connection;

    public FaculdadeDAO(Connection connection) {
        this.connection = connection;
    }

    // Listar todos os alunos
    public void listarAlunos() throws SQLException {
        String query = "SELECT codRA, nome, data_nascimento FROM aluno";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            System.out.println("\n--- Lista de Alunos ---");
            while (resultSet.next()) {
                int codRA = resultSet.getInt("codRA");
                String nome = resultSet.getString("nome");
                String dataNascimento = resultSet.getString("data_nascimento");
                System.out.println("RA: " + codRA + ", Nome: " + nome + ", Data de Nascimento: " + dataNascimento);
            }
        }
    }

    // Listar todas as disciplinas
    public void listarDisciplinas() throws SQLException {
        String query = "SELECT codDisciplina, nome FROM disciplina";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            System.out.println("\n--- Lista de Disciplinas ---");
            while (resultSet.next()) {
                int codDisciplina = resultSet.getInt("codDisciplina");
                String nome = resultSet.getString("nome");
                System.out.println("Código: " + codDisciplina + ", Nome: " + nome);
            }
        }
    }

    // Listar todas as disciplinas de um aluno
    public void listarDisciplinasDoAluno(int codRA) throws SQLException {
        String query = "SELECT a.nome AS aluno_nome, a.codRA, c.nome AS curso_nome, d.codDisciplina, d.nome AS disciplina_nome, t.semestre " +
                "FROM aluno a " +
                "JOIN turma t ON a.codRA = t.aluno " +
                "JOIN disciplina d ON d.codDisciplina = t.disciplina " +
                "JOIN curso_aluno ca ON a.codRA = ca.codRA " +
                "JOIN curso c ON ca.codCurso = c.codCurso " +
                "WHERE a.codRA = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, codRA);
            try (ResultSet resultSet = statement.executeQuery()) {

                System.out.println("\n--- Disciplinas do Aluno ---");
                if (resultSet.next()) {
                    String alunoNome = resultSet.getString("aluno_nome");
                    int alunoRA = resultSet.getInt("codRA");
                    String cursoNome = resultSet.getString("curso_nome");
                    System.out.println("Aluno: " + alunoNome + " (RA: " + alunoRA + ")");
                    System.out.println("Curso: " + cursoNome);
                    System.out.println("Disciplinas:");
                    do {
                        int codDisciplina = resultSet.getInt("codDisciplina");
                        String disciplinaNome = resultSet.getString("disciplina_nome");
                        int semestre = resultSet.getInt("semestre");
                        System.out.println("Código: " + codDisciplina + ", Nome: " + disciplinaNome + ", Semestre: " + semestre);
                    } while (resultSet.next());
                } else {
                    System.out.println("Nenhuma disciplina encontrada para o aluno com RA: " + codRA);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar disciplinas do aluno.");
            e.printStackTrace();
        }
    }

    // Cadastrar um aluno
    public void cadastrarAluno(int codRA, String nome, String dataNascimento) throws SQLException {
        String query = "INSERT INTO aluno (codRA, nome, data_nascimento) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, codRA);
            statement.setString(2, nome);
            statement.setString(3, dataNascimento);
            statement.executeUpdate();
            System.out.println("Aluno cadastrado com sucesso.");
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar aluno.");
            e.printStackTrace();
        }
    }

    // Cadastrar um aluno em um curso
    public void cadastrarAlunoEmCurso(int codRA, int codCurso) throws SQLException {
        String query = "INSERT INTO curso_aluno (codRA, codCurso) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, codRA);
            statement.setInt(2, codCurso);
            statement.executeUpdate();
            System.out.println("Aluno cadastrado no curso com sucesso.");
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar aluno no curso.");
            e.printStackTrace();
        }
    }

    // Cadastrar uma disciplina
    public void cadastrarDisciplina(int codDisciplina, String nome) throws SQLException {
        String query = "INSERT INTO disciplina (codDisciplina, nome) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, codDisciplina);
            statement.setString(2, nome);
            statement.executeUpdate();
            System.out.println("Disciplina cadastrada com sucesso.");
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar disciplina.");
            e.printStackTrace();
        }
    }

    // Cadastrar um aluno em uma disciplina
    public void cadastrarAlunoEmDisciplina(int codRA, int codDisciplina, int semestre) throws SQLException {
        // Verificar se a disciplina já está cheia (máximo 30 alunos)
        String verificaTurmaQuery = "SELECT COUNT(*) AS total_alunos FROM turma WHERE disciplina = ? AND semestre = ?";
        try (PreparedStatement verificaTurmaStmt = connection.prepareStatement(verificaTurmaQuery)) {
            verificaTurmaStmt.setInt(1, codDisciplina);
            verificaTurmaStmt.setInt(2, semestre);
            ResultSet resultSet = verificaTurmaStmt.executeQuery();

            if (resultSet.next() && resultSet.getInt("total_alunos") >= 30) {
                System.out.println("A disciplina já atingiu o número máximo de alunos (30).");
                return;
            }
        }

        // Inserir o aluno na turma
        String insertTurmaQuery = "INSERT INTO turma (aluno, disciplina, semestre) VALUES (?, ?, ?)";
        try (PreparedStatement insertTurmaStmt = connection.prepareStatement(insertTurmaQuery)) {
            insertTurmaStmt.setInt(1, codRA);
            insertTurmaStmt.setInt(2, codDisciplina);
            insertTurmaStmt.setInt(3, semestre);
            insertTurmaStmt.executeUpdate();
            System.out.println("Aluno matriculado na disciplina com sucesso.");
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar aluno na disciplina. Verifique se o RA e o código da disciplina estão corretos.");
            e.printStackTrace();
        }
    }

    // Cadastrar um curso
    public void cadastrarCurso(int codCurso, String nome) throws SQLException {
        String query = "INSERT INTO curso (codCurso, nome) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, codCurso);
            statement.setString(2, nome);
            statement.executeUpdate();
            System.out.println("Curso cadastrado com sucesso.");
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar curso.");
            e.printStackTrace();
        }
    }

    // Cadastrar uma disciplina em um curso
    public void cadastrarDisciplinaNoCurso(int codCurso, int codDisciplina) throws SQLException {
        String query = "INSERT INTO curso_disciplina (codCurso, codDisciplina) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, codCurso);
            statement.setInt(2, codDisciplina);
            statement.executeUpdate();
            System.out.println("Disciplina cadastrada no curso com sucesso.");
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar disciplina no curso.");
            e.printStackTrace();
        }
    }

    // Listar todos os cursos e suas disciplinas
    public void listarCursos() throws SQLException {
        String query = "SELECT c.codCurso, c.nome AS curso_nome, d.codDisciplina, d.nome AS disciplina_nome " +
                "FROM curso c " +
                "JOIN curso_disciplina cd ON c.codCurso = cd.codCurso " +
                "JOIN disciplina d ON cd.codDisciplina = d.codDisciplina";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            System.out.println("\n--- Lista de Cursos e Disciplinas ---");
            int currentCurso = -1;
            while (resultSet.next()) {
                int codCurso = resultSet.getInt("codCurso");
                String cursoNome = resultSet.getString("curso_nome");
                int codDisciplina = resultSet.getInt("codDisciplina");
                String disciplinaNome = resultSet.getString("disciplina_nome");

                if (codCurso != currentCurso) {
                    if (currentCurso != -1) {
                        System.out.println();
                    }
                    System.out.println("Curso: " + cursoNome + " (Código: " + codCurso + ")");
                    currentCurso = codCurso;
                }
                System.out.println("  - Disciplina: " + disciplinaNome + " (Código: " + codDisciplina + ")");
            }
        }
    }

    // Nova função para listar todos os cursos e suas disciplinas
    public void listarCursosEDisciplinas() throws SQLException {
        String sql = "SELECT c.codCurso, c.nome AS curso_nome, d.codDisciplina, d.nome AS disciplina_nome " +
                "FROM curso c " +
                "LEFT JOIN curso_disciplina cd ON c.codCurso = cd.codCurso " +
                "LEFT JOIN disciplina d ON cd.codDisciplina = d.codDisciplina";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n--- Lista de Cursos e Disciplinas ---");
            int currentCurso = -1;
            while (rs.next()) {
                int codCurso = rs.getInt("codCurso");
                String cursoNome = rs.getString("curso_nome");
                int codDisciplina = rs.getInt("codDisciplina");
                String disciplinaNome = rs.getString("disciplina_nome");

                if (codCurso != currentCurso) {
                    if (currentCurso != -1) {
                        System.out.println();
                    }
                    System.out.println("Curso: " + cursoNome + " (Código: " + codCurso + ")");
                    currentCurso = codCurso;
                }
                if (codDisciplina != 0) { // Exibir disciplina apenas se existir
                    System.out.println("  - Disciplina: " + disciplinaNome + " (Código: " + codDisciplina + ")");
                }
            }
        }
    }

    // Nova função para cadastrar aluno em um curso
    public void cadastrarAlunoNoCurso(int codRA, int codCurso) throws SQLException {
        String sql = "INSERT INTO curso_aluno (codRA, codCurso) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, codRA);
            pstmt.setInt(2, codCurso);
            pstmt.executeUpdate();
            System.out.println("Aluno cadastrado no curso com sucesso.");
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar aluno no curso.");
            e.printStackTrace();
        }
    }

    // Listar todos os professores
    public void listarProfessores() throws SQLException {
        String query = "SELECT codProfessor, nome FROM professor";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            System.out.println("\n--- Lista de Professores ---");
            while (resultSet.next()) {
                int codProfessor = resultSet.getInt("codProfessor");
                String nome = resultSet.getString("nome");
                System.out.println("Código: " + codProfessor + ", Nome: " + nome);
            }
        }
    }

    // Cadastrar um professor
    public void cadastrarProfessor(int codProfessor, String nome) throws SQLException {
        String query = "INSERT INTO professor (codProfessor, nome) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, codProfessor);
            statement.setString(2, nome);
            statement.executeUpdate();
            System.out.println("Professor cadastrado com sucesso.");
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar professor.");
            e.printStackTrace();
        }
    }
}
