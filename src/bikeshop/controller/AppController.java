package bikeshop.controller;

import bikeshop.dao.AppDAO;
import bikeshop.logic.security.SegurancaCripto;
import bikeshop.model.Customer;
import bikeshop.model.Hire;
import bikeshop.model.Token;
import bikeshop.servicos.Email;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.regex.Pattern;

public class AppController {
    private final AppDAO dao;
    private final SegurancaCripto cripto;
    private final Email emailService;
    // Você pode incorporar TokenServicos como helper aqui, se não quiser como classe separada

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.com$");

    public AppController(Javalin app, AppDAO dao, SegurancaCripto cripto, Email emailService) {
        this.dao = dao;
        this.cripto = cripto;
        this.emailService = emailService;

        // Rotas Bike
        app.get("/bikes/available", ctx -> ctx.json(dao.findAvailableBikes()));
        app.get("/bikes/count",     ctx -> ctx.json(Map.of("count", dao.countAvailableBikes())));

        // Rotas Customer
        app.post("/customers/register", this::register);
        app.post("/customers/confirm",  this::confirm);
        app.post("/customers/login",    this::login);

        // Rotas Hire
        app.post("/rentals/start", this::startRental);
        app.post("/rentals/end",   this::endRental);
    }

    // ---- DTOs ----
    public static class RegistrationForm {
        public String nome, postcode, email, telefone, senha;
    }
    public static class ConfirmForm { public String email, token; }
    public static class LoginForm   { public String email, senha; }
    public static class StartForm   { public int customerId, bikeId, numberOfDays; }
    public static class EndForm     { public int hireId; }

    // ---- Métodos Customer ----
    private void register(Context ctx) {
        RegistrationForm f = ctx.bodyAsClass(RegistrationForm.class);
        if (!EMAIL_PATTERN.matcher(f.email).matches()) {
            ctx.status(400).json(Map.of("error", "Email inválido"));
            return;
        }
        Customer c = new Customer(f.nome, f.postcode, f.email, f.telefone, cripto.hash(f.senha), false);
        dao.insertCustomer(c);
        Customer saved = dao.findCustomerByEmail(c.getEmail());

        // Gere token, insira no banco e envie email
        Token tokenObj = Token.generateForCustomer(saved.getId()); // Suponha um método factory no seu Token, ou gere aqui
        dao.insertToken(saved.getId(), tokenObj);
        emailService.sendTokenMail(saved.getEmail(), tokenObj.getTokenValue());

        ctx.status(201).json(Map.of("message", "Cadastro realizado. Verifique seu email para confirmar."));
    }

    private void confirm(Context ctx) {
        ConfirmForm f = ctx.bodyAsClass(ConfirmForm.class);
        Customer existing = dao.findCustomerByEmail(f.email);
        if (existing == null) {
            ctx.status(404).json(Map.of("error", "Cliente não encontrado"));
            return;
        }
        if (existing.isEmailVerificado()) {
            ctx.status(200).json(Map.of("message", "Email já verificado"));
            return;
        }
        Token validToken = dao.findValidToken(f.token);
        if (validToken == null) {
            ctx.status(400).json(Map.of("error", "Token inválido ou expirado"));
            return;
        }
        dao.markTokenUsed(f.token);
        dao.updateCustomerEmailVerified(existing.getId(), true);
        ctx.json(Map.of("message", "Email verificado com sucesso"));
    }

    private void login(Context ctx) {
        LoginForm f = ctx.bodyAsClass(LoginForm.class);
        Customer existing = dao.findCustomerByEmail(f.email);
        if (existing == null || !cripto.verificar(f.senha, existing.getSenhaHash())) {
            ctx.status(401).json(Map.of("error", "Credenciais inválidas"));
            return;
        }
        if (!existing.isEmailVerificado()) {
            ctx.status(403).json(Map.of("error", "Email não verificado"));
            return;
        }
        ctx.json(existing);
    }

    // ---- Métodos Hire (Aluguel) ----
    private void startRental(Context ctx) {
        StartForm f = ctx.bodyAsClass(StartForm.class);
        if (!dao.markBikeRented(f.bikeId)) {
            ctx.status(400).json(Map.of("error", "Bike não disponível"));
            return;
        }
        Hire hire = dao.insertHire(f.customerId, f.bikeId, LocalDate.now(), f.numberOfDays);
        ctx.status(201).json(hire);
    }

    private void endRental(Context ctx) {
        EndForm f = ctx.bodyAsClass(EndForm.class);
        Hire hire = dao.findHireById(f.hireId);
        if (hire == null) {
            ctx.status(404).json(Map.of("error", "Aluguel não encontrado"));
            return;
        }
        dao.endHire(f.hireId);
        dao.markBikeReturned(hire.getBike().getBikeNumber());
        ctx.json(Map.of("message", "Aluguel encerrado"));
    }
}
