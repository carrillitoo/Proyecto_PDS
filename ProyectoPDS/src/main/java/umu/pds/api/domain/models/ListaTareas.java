package umu.pds.api.domain.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import umu.pds.api.domain.exceptions.LimiteListaExcedidoException;

public class ListaTareas {
    private final UUID id;
    private String nombre;
    private int limiteTarjetas;
    private final List<Tarjeta> tarjetas;
    private final List<String> listasPreviasRequeridas;

    public static final int LIM_PD = Integer.MAX_VALUE;

    // ---------------------------------BUILDERSS---------------------------------
    // builders con lim_pd que tendra que ser infinito
    public ListaTareas(String nom, int lim) {
        if (nom == null || nom.trim().isEmpty())
            throw new IllegalArgumentException("El nombre de la lista no puede ser null o vacio");
        if (lim <= 0)
            throw new IllegalArgumentException("El limite de las tarjetas tiene que ser positivo");

        this.id = UUID.randomUUID();
        this.nombre = nom;
        this.limiteTarjetas = lim;
        this.tarjetas = new ArrayList<>();
        this.listasPreviasRequeridas = new ArrayList<>();
    }

    public ListaTareas(String nom) {
        this(nom, LIM_PD);
    }

    // esto es para reconstruir un objeto y en la persistencia fuera de la
    // experiencia de usuario
    public static ListaTareas reconstituir(UUID id, String nombre, int limiteTarjetas, List<Tarjeta> tarjetas,
            List<String> reglas) {
        return new ListaTareas(id, nombre, limiteTarjetas, tarjetas, reglas);
    }

    private ListaTareas(UUID id, String nombre, int limiteTarjetas, List<Tarjeta> tarjetas, List<String> reglas) {
        this.id = id;
        this.nombre = nombre;
        this.limiteTarjetas = limiteTarjetas;
        this.tarjetas = new ArrayList<>(tarjetas);
        this.listasPreviasRequeridas = new ArrayList<>(reglas);
    }

    // ---------------------------------GETTERS---------------------------------
    public UUID getId() {
        return id;
    }

    public int getLimiteTarjetas() {
        return limiteTarjetas;
    }

    public String getNombre() {
        return nombre;
    }

    public List<Tarjeta> getTarjetas() {
        return Collections.unmodifiableList(this.tarjetas);
    }

    public List<String> getListasPreviasRequeridas() {
        return Collections.unmodifiableList(this.listasPreviasRequeridas);
    }

    public static int getLimPd() {
        return LIM_PD;
    }

    // ---------------------------------OPERACIONES DE
    // TARJETAS---------------------------------
    public void addTarjeta(Tarjeta tarjeta) throws LimiteListaExcedidoException {
        if (tarjetas.size() >= limiteTarjetas)
            throw new LimiteListaExcedidoException("La lista " + nombre + " ha alcanzado su limite.");

        this.tarjetas.add(tarjeta);
    }

    public Tarjeta extraerTarjeta(UUID tarjetaId) {
        Tarjeta tarjetaExtraida = buscarTarjeta(tarjetaId)
                .orElseThrow(() -> new IllegalArgumentException("La tarjeta no esta en la lista " + nombre));

        this.tarjetas.remove(tarjetaExtraida);
        return tarjetaExtraida;
    }

    public boolean containsTarjeta(UUID tarjetaId) {
        return buscarTarjeta(tarjetaId).isPresent();
    }

    // ---------------------------------MEJORA D LOS PÀSOS
    // PREVIOS---------------------------------
    public void requerirPasoPrevioPor(String nomListaOrig) {
        if (nomListaOrig == null || nomListaOrig.trim().isEmpty())
            throw new IllegalArgumentException("El nombre de la lista previa no puede estar vacio");

        if (!this.listasPreviasRequeridas.contains(nomListaOrig))
            this.listasPreviasRequeridas.add(nomListaOrig);
    }

    // ---------------------------------METODO AUX---------------------------------
    private Optional<Tarjeta> buscarTarjeta(UUID tarjetaId) {
        return this.tarjetas.stream()
                .filter(t -> t.getId().equals(tarjetaId))
                .findFirst();
    }

}
