package bikeshop.servicos;

import jakarta.mail.Session;
import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

/**
 * Serviço de envio de e-mail via SMTP usando Jakarta Mail.
 * Carrega configurações de ambiente e encapsula lógica de envio de token.
 */
public class Email {
    private final Session session;
    private final String from;

    public Email() {
        String host = "smtp.gmail.com";
        String port = "587";
        String user = "noreplyfaculdade@gmail.com";
        String pass = "zykw mexi tfye xqlg";
        this.from   = "Bike Shop <noreplyfaculdade@gmail.com>";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        this.session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, pass);
            }
        });
    }
    /**
     * Envia o token de confirmação ao email do usuário.
     * Se falhar, apenas registra o erro internamente.
     *
     * @param recipientEmail destinatário
     * @param token          token de confirmação
     */
    public void sendTokenMail(String recipientEmail, String token) {
        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            msg.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(recipientEmail, false)
            );
            msg.setSubject("Confirmação de Cadastro - Seu Token");
            String body = String.format(
                    "Olá!\n\n" +
                            "Use este token para confirmar seu cadastro (válido por 15 minutos):\n\n" +
                            "%s\n\n" +
                            "Se não foi você, ignore este email.",
                    token
            );
            msg.setText(body);
            Transport.send(msg);
        } catch (MessagingException e) {
            System.err.println("Falha ao enviar email: " + e.getMessage());
        }
    }
}
