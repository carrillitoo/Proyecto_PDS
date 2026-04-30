package umu.pds.api.domain.ports.in;



public interface ValidarCodigoPort {
    boolean ejecutar(String email, String codigo);
}
