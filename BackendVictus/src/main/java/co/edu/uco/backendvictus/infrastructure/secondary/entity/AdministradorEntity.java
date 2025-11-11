package co.edu.uco.backendvictus.infrastructure.secondary.entity;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("administrador")
public class AdministradorEntity implements Persistable<UUID> {

    @Id
    private UUID id;

    @Column("primer_nombre")
    private String primerNombre;

    @Column("segundo_nombres")
    private String segundoNombres;

    @Column("primer_apellido")
    private String primerApellido;

    @Column("segundo_apellido")
    private String segundoApellido;

    @Column("email")
    private String email;

    @Column("telefono")
    private String telefono;

    @Column("activo")
    private boolean activo;

    @Transient
    private boolean newEntity = true;

    public AdministradorEntity() {
        // R2DBC requires a default constructor
    }

    public AdministradorEntity(final UUID id, final String primerNombre, final String segundoNombres,
            final String primerApellido, final String segundoApellido, final String email, final String telefono,
            final boolean activo) {
        this.id = id;
        this.primerNombre = primerNombre;
        this.segundoNombres = segundoNombres;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
        this.email = email;
        this.telefono = telefono;
        this.activo = activo;
        this.newEntity = true;
    }

    public AdministradorEntity markNew(final boolean newEntity) {
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

    public String getPrimerNombre() {
        return primerNombre;
    }

    public void setPrimerNombre(final String primerNombre) {
        this.primerNombre = primerNombre;
    }

    public String getSegundoNombres() {
        return segundoNombres;
    }

    public void setSegundoNombres(final String segundoNombres) {
        this.segundoNombres = segundoNombres;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(final String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(final String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(final String telefono) {
        this.telefono = telefono;
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
