package umu.pds.api.adapters.out.email;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import umu.pds.api.domain.models.Email;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

public class SmtpEmailAdapterImplTest {

    @Test
    void testEnviarCodigoAcceso() {
        JavaMailSender mailSender = Mockito.mock(JavaMailSender.class);
        SmtpEmailAdapterImpl adapter = new SmtpEmailAdapterImpl(mailSender);
        Email email = new Email("test@email.com");
        
        adapter.enviarCodigoAcceso(email, "123456");

        verify(mailSender).send(any(SimpleMailMessage.class));
    }
}
