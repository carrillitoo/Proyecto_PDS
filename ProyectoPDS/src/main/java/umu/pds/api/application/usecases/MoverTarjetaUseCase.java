package umu.pds.api.application.usecases;

public interface MoverTarjetaUseCase {
    public void ejecutar(String tableroIdStr, String tarjetaIdStr, String listaOrigen, String listaDestino);
}
