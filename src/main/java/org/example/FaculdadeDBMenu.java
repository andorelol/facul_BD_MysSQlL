package org.example;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

public class FaculdadeDBMenu {

    private static final SimpleDateFormat INPUT_DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
    private static final SimpleDateFormat OUTPUT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) {
        try (Connection connection = DatabaseConnection.getConnection();
             Scanner scanner = new Scanner(System.in)) {

            FaculdadeDAO dao = new FaculdadeDAO(connection);

            while (true) {
                // Exibir o menu principal
                System.out.println("\n--- Menu da Faculdade ---");
                System.out.println("1. Alunos");
                System.out.println("2. Professores");
                System.out.println("3. Cursos");
                System.out.println("0. Sair");
                System.out.print("Escolha uma opção: ");

                int opcao = obterOpcao(scanner);

                switch (opcao) {
                    case 1:
                        menuAlunos(dao, scanner);
                        break;
                    case 2:
                        menuProfessores(dao, scanner);
                        break;
                    case 3:
                        menuCursos(dao, scanner);
                        break;
                    case 0:
                        System.out.println("Saindo...");
                        return;
                    default:
                        System.out.println("Opção inválida, tente novamente.");
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados.");
            e.printStackTrace();
        }
    }

    // Função para obter uma opção do menu com tratamento de exceção
    private static int obterOpcao(Scanner scanner) {
        while (true) {
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, insira um número inteiro.");
                scanner.next(); // Limpar o buffer do scanner
            }
        }
    }

    // Função para o menu Alunos
    private static void menuAlunos(FaculdadeDAO dao, Scanner scanner) {
        while (true) {
            System.out.println("\n--- Menu Alunos ---");
            System.out.println("1. Listar Alunos");
            System.out.println("2. Cadastrar Aluno");
            System.out.println("3. Listar Disciplinas do Aluno");
            System.out.println("4. Cadastrar Aluno em Curso"); // Nova opção adicionada
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");

            int opcao = obterOpcao(scanner);

            switch (opcao) {
                case 1:
                    try {
                        dao.listarAlunos();
                    } catch (SQLException e) {
                        System.err.println("Erro ao listar alunos.");
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    cadastrarAluno(dao, scanner);
                    break;
                case 3:
                    listarDisciplinasDoAluno(dao, scanner);
                    break;
                case 4:
                    cadastrarAlunoNoCurso(dao, scanner);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opção inválida, tente novamente.");
            }
        }
    }

    // Função para o menu Professores
    private static void menuProfessores(FaculdadeDAO dao, Scanner scanner) {
        while (true) {
            System.out.println("\n--- Menu Professores ---");
            System.out.println("1. Listar Professores");
            System.out.println("2. Cadastrar Professor");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");

            int opcao = obterOpcao(scanner);

            switch (opcao) {
                case 1:
                    try {
                        dao.listarProfessores();
                    } catch (SQLException e) {
                        System.err.println("Erro ao listar professores.");
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    cadastrarProfessor(dao, scanner);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opção inválida, tente novamente.");
            }
        }
    }

    // Função para o menu Cursos
    private static void menuCursos(FaculdadeDAO dao, Scanner scanner) {
        while (true) {
            System.out.println("\n--- Menu Cursos ---");
            System.out.println("1. Cadastrar Curso");
            System.out.println("2. Cadastrar Disciplina no Curso");
            System.out.println("3. Listar Disciplinas");
            System.out.println("4. Cadastrar Aluno em Disciplina");
            System.out.println("5. Cadastrar Disciplina");
            System.out.println("6. Listar Cursos e Disciplinas"); // Nova opção adicionada
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");

            int opcao = obterOpcao(scanner);

            switch (opcao) {
                case 1:
                    cadastrarCurso(dao, scanner);
                    break;
                case 2:
                    cadastrarDisciplinaNoCurso(dao, scanner);
                    break;
                case 3:
                    try {
                        dao.listarDisciplinas();
                    } catch (SQLException e) {
                        System.err.println("Erro ao listar disciplinas.");
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    cadastrarAlunoEmDisciplina(dao, scanner);
                    break;
                case 5:
                    cadastrarDisciplina(dao, scanner);
                    break;
                case 6:
                    listarCursosEDisciplinas(dao, scanner); // Função para listar cursos e disciplinas
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opção inválida, tente novamente.");
            }
        }
    }

    // Função para cadastrar um aluno
    private static void cadastrarAluno(FaculdadeDAO dao, Scanner scanner) {
        int codRA = 0;
        String nome = "";
        String dataNascimentoStr = "";
        Date dataNascimento = null;

        // Solicitar informações ao usuário
        System.out.print("\nDigite o RA do aluno: ");
        while (true) {
            try {
                codRA = scanner.nextInt();
                break; // Sai do loop se a entrada for válida
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, insira um número inteiro para o RA.");
                scanner.next(); // Limpar o buffer do scanner
            }
        }

        scanner.nextLine(); // Consome a nova linha deixada pelo nextInt()

        System.out.print("Digite o nome do aluno: ");
        nome = scanner.nextLine();

        System.out.print("Digite a data de nascimento do aluno (dd-MM-yyyy): ");
        while (true) {
            try {
                dataNascimentoStr = scanner.nextLine();
                dataNascimento = INPUT_DATE_FORMAT.parse(dataNascimentoStr);
                break; // Sai do loop se a entrada for válida
            } catch (ParseException e) {
                System.out.println("Formato inválido. Por favor, insira a data no formato dd-MM-yyyy.");
            }
        }

        try {
            dao.cadastrarAluno(codRA, nome, OUTPUT_DATE_FORMAT.format(dataNascimento));
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar aluno.");
            e.printStackTrace();
        }
    }

    // Função para listar as disciplinas de um aluno
    private static void listarDisciplinasDoAluno(FaculdadeDAO dao, Scanner scanner) {
        int codRA = 0;

        // Solicitar informações ao usuário
        System.out.print("\nDigite o RA do aluno: ");
        while (true) {
            try {
                codRA = scanner.nextInt();
                break; // Sai do loop se a entrada for válida
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, insira um número inteiro para o RA.");
                scanner.next(); // Limpar o buffer do scanner
            }
        }

        try {
            dao.listarDisciplinasDoAluno(codRA);
        } catch (SQLException e) {
            System.err.println("Erro ao listar disciplinas do aluno.");
            e.printStackTrace();
        }
    }

    // Função para cadastrar um professor
    private static void cadastrarProfessor(FaculdadeDAO dao, Scanner scanner) {
        int codProfessor = 0;
        String nome = "";

        // Solicitar informações ao usuário
        System.out.print("\nDigite o código do professor: ");
        while (true) {
            try {
                codProfessor = scanner.nextInt();
                break; // Sai do loop se a entrada for válida
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, insira um número inteiro para o código do professor.");
                scanner.next(); // Limpar o buffer do scanner
            }
        }

        scanner.nextLine(); // Consome a nova linha deixada pelo nextInt()

        System.out.print("Digite o nome do professor: ");
        nome = scanner.nextLine();

        try {
            dao.cadastrarProfessor(codProfessor, nome);
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar professor.");
            e.printStackTrace();
        }
    }

    // Função para cadastrar um curso
    private static void cadastrarCurso(FaculdadeDAO dao, Scanner scanner) {
        int codCurso = 0;
        String nome = "";

        // Solicitar informações ao usuário
        System.out.print("\nDigite o código do curso: ");
        while (true) {
            try {
                codCurso = scanner.nextInt();
                break; // Sai do loop se a entrada for válida
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, insira um número inteiro para o código do curso.");
                scanner.next(); // Limpar o buffer do scanner
            }
        }

        scanner.nextLine(); // Consome a nova linha deixada pelo nextInt()

        System.out.print("Digite o nome do curso: ");
        nome = scanner.nextLine();

        try {
            dao.cadastrarCurso(codCurso, nome);
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar curso.");
            e.printStackTrace();
        }
    }

    // Função para cadastrar uma disciplina no curso
    private static void cadastrarDisciplinaNoCurso(FaculdadeDAO dao, Scanner scanner) {
        int codCurso = 0;
        int codDisciplina = 0;

        // Solicitar informações ao usuário
        System.out.print("\nDigite o código do curso: ");
        while (true) {
            try {
                codCurso = scanner.nextInt();
                break; // Sai do loop se a entrada for válida
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, insira um número inteiro para o código do curso.");
                scanner.next(); // Limpar o buffer do scanner
            }
        }

        System.out.print("Digite o código da disciplina: ");
        while (true) {
            try {
                codDisciplina = scanner.nextInt();
                break; // Sai do loop se a entrada for válida
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, insira um número inteiro para o código da disciplina.");
                scanner.next(); // Limpar o buffer do scanner
            }
        }

        try {
            dao.cadastrarDisciplinaNoCurso(codCurso, codDisciplina);
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar disciplina no curso.");
            e.printStackTrace();
        }
    }

    // Função para cadastrar um aluno em uma disciplina
    private static void cadastrarAlunoEmDisciplina(FaculdadeDAO dao, Scanner scanner) {
        int codRA = 0;
        int codDisciplina = 0;
        int semestre = 0;

        // Solicitar informações ao usuário
        System.out.print("\nDigite o RA do aluno: ");
        while (true) {
            try {
                codRA = scanner.nextInt();
                break; // Sai do loop se a entrada for válida
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, insira um número inteiro para o RA.");
                scanner.next(); // Limpar o buffer do scanner
            }
        }

        System.out.print("Digite o código da disciplina: ");
        while (true) {
            try {
                codDisciplina = scanner.nextInt();
                break; // Sai do loop se a entrada for válida
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, insira um número inteiro para o código da disciplina.");
                scanner.next(); // Limpar o buffer do scanner
            }
        }

        System.out.print("Digite o semestre: ");
        while (true) {
            try {
                semestre = scanner.nextInt();
                break; // Sai do loop se a entrada for válida
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, insira um número inteiro para o semestre.");
                scanner.next(); // Limpar o buffer do scanner
            }
        }

        try {
            dao.cadastrarAlunoEmDisciplina(codRA, codDisciplina, semestre); // Passar o semestre aqui
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar aluno em disciplina.");
            e.printStackTrace();
        }
    }

    // Função para cadastrar uma disciplina
    private static void cadastrarDisciplina(FaculdadeDAO dao, Scanner scanner) {
        int codDisciplina = 0;
        String nome = "";

        // Solicitar informações ao usuário
        System.out.print("\nDigite o código da disciplina: ");
        while (true) {
            try {
                codDisciplina = scanner.nextInt();
                break; // Sai do loop se a entrada for válida
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, insira um número inteiro para o código da disciplina.");
                scanner.next(); // Limpar o buffer do scanner
            }
        }

        scanner.nextLine(); // Consome a nova linha deixada pelo nextInt()

        System.out.print("Digite o nome da disciplina: ");
        nome = scanner.nextLine();

        try {
            dao.cadastrarDisciplina(codDisciplina, nome);
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar disciplina.");
            e.printStackTrace();
        }
    }

    // Função para cadastrar um aluno em um curso
    private static void cadastrarAlunoNoCurso(FaculdadeDAO dao, Scanner scanner) {
        int codRA = 0;
        int codCurso = 0;

        // Solicitar informações ao usuário
        System.out.print("\nDigite o RA do aluno: ");
        while (true) {
            try {
                codRA = scanner.nextInt();
                break; // Sai do loop se a entrada for válida
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, insira um número inteiro para o RA.");
                scanner.next(); // Limpar o buffer do scanner
            }
        }

        System.out.print("Digite o código do curso: ");
        while (true) {
            try {
                codCurso = scanner.nextInt();
                break; // Sai do loop se a entrada for válida
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, insira um número inteiro para o código do curso.");
                scanner.next(); // Limpar o buffer do scanner
            }
        }

        try {
            dao.cadastrarAlunoNoCurso(codRA, codCurso);
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar aluno no curso.");
            e.printStackTrace();
        }
    }

    // Função para listar cursos e disciplinas
    private static void listarCursosEDisciplinas(FaculdadeDAO dao, Scanner scanner) {
        try {
            dao.listarCursosEDisciplinas();
        } catch (SQLException e) {
            System.err.println("Erro ao listar cursos e disciplinas.");
            e.printStackTrace();
        }
    }
}
