package umu.pds.api.adapters.out.email;

import org.junit.jupiter.api.Test;
import umu.pds.api.domain.models.Email;

public class ConsoleEmailAdapterImplTest {

    @Test
    void testEnviarCodigoAcceso() {
        ConsoleEmailAdapterImpl adapter = new ConsoleEmailAdapterImpl();
        Email email = new Email("test@email.com");
        
        adapter.enviarCodigoAcceso(email, "123456");
    }
}
