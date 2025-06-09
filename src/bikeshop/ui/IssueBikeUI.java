package bikeshop.ui;

import bikeshop.logic.Payment;
import bikeshop.model.Bike;
import bikeshop.model.Customer;
import bikeshop.servicos.Hire;

import java.time.LocalDate;
import java.util.Scanner;

public class IssueBikeUI {
    private Bike chosenBike = null;
    private Customer customer = null;
    private Payment payment = null;
    private Hire hire = null;
    private int numberOfDays = 0;

    public void showBikeDetails(int bikeNum) {
        chosenBike = Bike.findBikeByNumber(bikeNum);
        if (chosenBike != null) {
            chosenBike.showDetails();
        } else {
            System.out.println("Bike not found.");
        }
    }

    public void calculateCost(int numDays) {
        this.numberOfDays = numDays;
        int cost = chosenBike.calculateCost(numDays);
        System.out.printf("Estimated cost for %d day(s): %d%n", numDays, cost);
    }

    public void createCustomer(String name, String postcode,String email, String telephone) {
        this.customer = new Customer(name,postcode,email,telephone);
        this.payment = new Payment(customer);
        this.hire = new Hire(LocalDate.now(), numberOfDays, chosenBike, customer);
    }

    public void calculateTotalPayment() {
        if (payment != null && hire != null) {
            payment.calculateTotalPayment(hire);
        } else {
            System.out.println("Cannot calculate payment: customer or hire not initialized.");
        }
    }
    public void CadastrarCliente(Scanner sc){
        System.out.println("Deseja cadastrar de Forma rapida? (S/N)");
        String escolha = sc.nextLine();
        String nome,postcode,email,telefone;

        if (escolha.equalsIgnoreCase("S")){
            System.out.println("Digite seus dados (nome,postcode,email,telefone)");
            String linha = sc.nextLine();
            String[] partes = linha.split(",");
            if (partes.length != 4) {
                System.out.println("Insira os dados na forma: Nome,Postcode,Email,Telefone");
                System.out.println("Exemplo: João da Silva,12345-000,joao@email.com,21999999999");
            }
            nome = partes[0].trim();
            postcode = partes[1].trim();
            email = partes[2].trim();
            telefone = partes[3].trim();

        }else {
                System.out.println("Nome:");
                 nome = sc.nextLine();
                System.out.println("CEP (00000-000):");
                 postcode = sc.nextLine();
                System.out.println("Email:");
                 email = sc.nextLine();
                System.out.println("telefone (DDD+00000-000):");
                 telefone = sc.nextLine();
        }
        if (!isValidCEP(postcode)) {
            System.out.println("CEP inválido. Use o formato 00000-000.");
            return;
        }

        if (!isValidEmail(email)) {
            System.out.println("Email inválido. Deve conter @ e terminar com .com");
            return;
        }

        if (!isValidTelefone(telefone)) {
            System.out.println("Telefone inválido. Use o formato DDD00000-0000.");
            return;
        }
        createCustomer(nome,postcode,email,telefone);
        System.out.println("Cliente cadastrado com sucesso!");
        }
    private boolean isValidEmail(String email) {
        return email.matches("^[\\w\\.-]+@[\\w\\.-]+\\.com$");
    }

    private boolean isValidTelefone(String telefone) {
        return telefone.matches("^\\(?\\d{2}\\)?\\s?\\d{4,5}-?\\d{4}$");
    }

    private boolean isValidCEP(String cep) {
        return cep.matches("^\\d{5}-?\\d{3}$");
    }
    }

