package co.edu.uco.backendvictus.infrastructure.secondary.entity;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("pais")
public class PaisEntity implements Persistable<UUID> {

    @Id
    private UUID id;

    @Column("nombre")
    private String nombre;

    @Column("activo")
    private boolean activo;

    @Transient
    private boolean newEntity = true;

    public PaisEntity() {
        // R2DBC requires a default constructor
    }

    public PaisEntity(final UUID id, final String nombre, final boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.activo = activo;
        this.newEntity = true;
    }

    public PaisEntity markNew(final boolean newEntity) {
        this.newEntity = newEntity;
        return this;
    }

    @Override
    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = nombre;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(final boolean activo) {
        this.activo = activo;
    }

    @Override
    public boolean isNew() {
        return newEntity || id == null;
    }
}
