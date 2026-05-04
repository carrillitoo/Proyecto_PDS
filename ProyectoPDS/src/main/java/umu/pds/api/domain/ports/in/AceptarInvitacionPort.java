package umu.pds.api.domain.ports.in;

public interface AceptarInvitacionPort {
    void ejecutar(String email, String tableroId);
}
