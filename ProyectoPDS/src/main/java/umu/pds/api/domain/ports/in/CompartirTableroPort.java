package umu.pds.api.domain.ports.in;



//Puerto de entrada para compartir tableros
public interface CompartirTableroPort {
	void ejecutar(String emailInvitado, String tableroId, String rol);
}
