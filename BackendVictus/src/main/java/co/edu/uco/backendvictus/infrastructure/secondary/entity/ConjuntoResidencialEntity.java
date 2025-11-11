package co.edu.uco.backendvictus.infrastructure.secondary.entity;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("conjunto_residencial")
public class ConjuntoResidencialEntity implements Persistable<UUID> {

    @Id
    private UUID id;

    @Column("nombre")
    private String nombre;

    @Column("direccion")
    private String direccion;

    @Column("ciudad_id")
    private UUID ciudadId;

    @Column("administrador_id")
    private UUID administradorId;

    @Column("activo")
    private boolean activo;

    @Transient
    private boolean newEntity = true;

    public ConjuntoResidencialEntity() {
        // R2DBC requires a default constructor
    }

    public ConjuntoResidencialEntity(final UUID id, final String nombre, final String direccion, final UUID ciudadId,
            final UUID administradorId, final boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.ciudadId = ciudadId;
        this.administradorId = administradorId;
        this.activo = activo;
        this.newEntity = true;
    }

    public ConjuntoResidencialEntity markNew(final boolean newEntity) {
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(final String direccion) {
        this.direccion = direccion;
    }

    public UUID getCiudadId() {
        return ciudadId;
    }

    public void setCiudadId(final UUID ciudadId) {
        this.ciudadId = ciudadId;
    }

    public UUID getAdministradorId() {
        return administradorId;
    }

    public void setAdministradorId(final UUID administradorId) {
        this.administradorId = administradorId;
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
