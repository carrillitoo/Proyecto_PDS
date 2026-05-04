package umu.pds.api.application.usecases;

import umu.pds.api.domain.exceptions.LimiteListaExcedidoException;
import umu.pds.api.domain.models.Tarjeta;

public interface AddTarjetaListaUseCase {
    Tarjeta ejecutar(String tableroIdStr, String nombreLista, String titulo, String descripcion, String tipo,
            String contenidoTarea) throws LimiteListaExcedidoException;
}
