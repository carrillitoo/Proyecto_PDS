package umu.pds.api.adapters.out.email;

import org.springframework.stereotype.Component;
import umu.pds.api.domain.models.Email;
import umu.pds.api.domain.ports.out.EmailPort;

//adaptador para simular el envio de emails
@Component
public class ConsoleEmailAdapter implements EmailPort {

    @Override
    public void enviarCodigoAcceso(Email destino, String codigo) {
        System.out.println("\n------------------------------------------------");
        System.out.println("📩 [SIMULADOR DE EMAIL]");
        System.out.println("PARA: " + destino.getDireccion());
        System.out.println("ASUNTO: Tu código de acceso a Trello PDS");
        System.out.println("MENSAJE: Tu código de seguridad es: " + codigo);
        System.out.println("------------------------------------------------\n");
    }
}