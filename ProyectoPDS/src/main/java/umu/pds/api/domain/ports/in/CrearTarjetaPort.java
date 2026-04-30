package umu.pds.api.domain.ports.in;


import umu.pds.api.domain.models.Tarjeta;

// Puerto de entrada para crear tarjetas

public interface CrearTarjetaPort {
    Tarjeta ejecutar(String tableroId, String listaId, String titulo, String descripcion, String tipo, String contenido);
}