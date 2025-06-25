package bikeshop.ui;

import bikeshop.dao.AppDAO;
import bikeshop.logic.security.SegurancaCripto;
import bikeshop.model.Customer;
import bikeshop.model.Token;
import bikeshop.servicos.Email;

import java.time.LocalDateTime;
import java.util.Scanner;

public class IssueBikeUI {
    private final AppDAO dao;
    private final SegurancaCripto cripto;
    private final Email emailService;

    public IssueBikeUI(AppDAO dao, SegurancaCripto cripto, Email emailService) {
        this.dao = dao;
        this.cripto = cripto;
        this.emailService = emailService;
    }

    /** Menu de login CLI */
    public void menuLogin(Scanner sc) {
        System.out.print("Email: ");
        String email = sc.nextLine().trim();
        System.out.print("Senha: ");
        String senha = sc.nextLine().trim();

        Customer c = dao.loginCustomer(email, senha);
        if (c != null) {
            System.out.println("Login realizado com sucesso. Bem-vindo, " + c.getNome() + "!");
        } else {
            System.out.println("Falha no login. Confira seu email/senha e se o email está confirmado.");
        }
    }

    /** Menu de registro CLI */
    public void menuRegisterCliente(Scanner sc) {
        System.out.println("=== Cadastro de Cliente ===");
        System.out.print("Nome: ");
        String nome = sc.nextLine().trim();
        System.out.print("CEP (00000-000): ");
        String postcode = sc.nextLine().trim();
        System.out.print("Email: ");
        String email = sc.nextLine().trim();
        System.out.print("Telefone (DDD00000-0000): ");
        String telefone = sc.nextLine().trim();
        System.out.print("Senha: ");
        String senha = sc.nextLine().trim();

        if (!isValidCEP(postcode) || !isValidEmail(email) || !isValidTelefone(telefone)) {
            System.out.println("Dados inválidos. Verifique e tente novamente.");
            return;
        }

        // Cria e persiste cliente
        String hash = cripto.hash(senha);
        Customer c = new Customer(nome, postcode, email, telefone, hash, false);
        dao.insertCustomer(c);
        Customer saved = dao.findCustomerByEmail(email);

        // Gera token e envia por email
        Token tokenObj = Token.generateForCustomer(saved.getId());
        dao.insertToken(saved.getId(), tokenObj);
        emailService.sendTokenMail(email, tokenObj.getTokenValue());

        // Solicita confirmação de token
        System.out.print("Digite o token enviado para seu email (expira em "
                + tokenObj.getExpiration().toString() + "): ");
        String inputToken = sc.nextLine().trim();

        Token validToken = dao.findValidToken(inputToken);
        if (validToken == null) {
            System.out.println("Token inválido ou expirado. Cadastro cancelado.");
            return;
        }
        dao.markTokenUsed(inputToken);
        dao.updateCustomerEmailVerified(saved.getId(), true);
        System.out.println("Cadastro confirmado e email verificado com sucesso!");
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[\\w\\.-]+@[\\w\\.-]+\\.com$");
    }

    private boolean isValidTelefone(String t) {
        return t.matches("^\\(?\\d{2}\\)?\\s?\\d{4,5}-?\\d{4}$");
    }

    private boolean isValidCEP(String cep) {
        return cep.matches("^\\d{5}-?\\d{3}$");
    }
}