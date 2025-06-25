package bikeshop;

import bikeshop.ui.IssueBikeUI;
import bikeshop.dao.AppDAO;
import bikeshop.logic.security.SegurancaCripto;
import bikeshop.servicos.Email;
import bikeshop.controller.AppController;
import com.google.gson.Gson;
import io.javalin.Javalin;

import java.util.Scanner;

public class StartUp {
    public static void main(String[] args) {
        System.out.println("Escolha o modo de execução:");
        System.out.println("1. Interface CLI (console)");
        System.out.println("2. Servidor REST API");
        System.out.print("Opção: ");
        Scanner sc = new Scanner(System.in);
        int modo = sc.nextInt();
        sc.nextLine();

        SegurancaCripto cripto = new SegurancaCripto();
        AppDAO dao = new AppDAO(cripto);
        Email emailService = new Email();

        if (modo == 1) {
            runCli(sc, dao, cripto, emailService);
        } else if (modo == 2) {
            runApi(dao, cripto, emailService);
        } else {
            System.out.println("Opção inválida. Encerrando.");
        }
    }

    private static void runCli(Scanner sc, AppDAO dao, SegurancaCripto cripto, Email emailService) {
        IssueBikeUI ui = new IssueBikeUI(dao, cripto, emailService);
        int opcao;
        while (true) {
            System.out.println("=== MENU BIKE SHOP ===");
            System.out.println("1. Cadastrar nova bike");
            System.out.println("2. Mostrar quantidade de bikes ativas");
            System.out.println("3. Login (cliente já cadastrado)");
            System.out.println("4. Cadastrar novo cliente");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1:
                    // ui.menuCadastrarBike(sc); // Implemente se desejar
                    break;
                case 2:
                    // ui.mostrarContagemDeBikes(); // Implemente se desejar
                    break;
                case 3:
                    ui.menuLogin(sc);
                    break;
                case 4:
                    ui.menuRegisterCliente(sc);
                    break;
                case 0:
                    System.out.println("Saindo...");
                    sc.close();
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
            System.out.println();
        }
    }

    private static void runApi(AppDAO dao, SegurancaCripto cripto, Email emailService) {
        Javalin app = Javalin.create(cfg -> {
            cfg.jsonMapper(new bikeshop.config.GsonMapper(new Gson()));
        }).start(7000);

        new AppController(app, dao, cripto, emailService);
        System.out.println("API REST disponível em http://localhost:7000/");
        System.out.println("Pressione CTRL+C para encerrar.");
    }
}
