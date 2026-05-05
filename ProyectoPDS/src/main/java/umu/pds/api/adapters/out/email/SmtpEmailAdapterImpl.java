package umu.pds.api.adapters.out.email;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import umu.pds.api.domain.models.Email;
import umu.pds.api.domain.ports.out.EmailPort;

@Component
public class SmtpEmailAdapterImpl implements EmailPort {

    private final JavaMailSender mailSender;

    public SmtpEmailAdapterImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void enviarCodigoAcceso(Email destino, String codigo) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destino.getDireccion());
        message.setSubject("Tu código de acceso a Tablerellos");
        message.setText("Tu código de seguridad es: " + codigo);
        mailSender.send(message);
    }
}