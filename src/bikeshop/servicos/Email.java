package bikeshop.servicos;
import bikeshop.model.Token;
public class Email {
    public void sendTokenWmail(String recipientEmail,String Token){
        System.out.println("Enviando email para: " + recipientEmail);
        System.out.println("Seu token de acesso é: " + token);
        System.out.println("Este token expira em alguns minutos.");
    }
}
