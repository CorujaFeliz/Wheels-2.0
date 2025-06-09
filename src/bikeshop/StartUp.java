package bikeshop;

import java.util.Scanner;
import bikeshop.ui.IssueBikeUI;


public class StartUp {
    public static void main(String[] args) {

        IssueBikeUI ui = new IssueBikeUI();
        Scanner sc = new Scanner(System.in);
        int opcao;

        while (true){
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
                    //ui.menuCadastrarBike(sc);
                    break;
                case 2:
                  //  ui.mostrarContagemDeBikes();
                    break;
                case 3:
                   // ui.menuLogin(sc);
                    break;
                case 4:
                    ui.CadastrarCliente(sc);
                    break;
                case 0:
                    System.out.println("Saindo...");
                    sc.close();
                    return;
                default:
                    System.out.println("Opção inválida.");
            }

            System.out.println(); // espaço entre execuções

        }


    }
}

