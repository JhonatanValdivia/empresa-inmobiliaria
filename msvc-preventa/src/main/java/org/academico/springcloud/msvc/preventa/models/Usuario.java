package org.academico.springcloud.msvc.preventa.models;

public class Usuario
{
    private Long id;

    private NombreCompleto nombreCompleto;

    private TipoUsuario tipoUsuario;

    private Telefono telefono;

    private CorreoElectronico correoElectronico;

    private Direccion direccion;

    public static class NombreCompleto{
        private String primerNombre;
        private String segundoNombre;
        private String primerApellido;
        private String segundoApellido;

        public String getPrimerNombre() {
            return primerNombre;
        }

        public void setPrimerNombre(String primerNombre) {
            this.primerNombre = primerNombre;
        }

        public String getSegundoNombre() {
            return segundoNombre;
        }

        public void setSegundoNombre(String segundoNombre) {
            this.segundoNombre = segundoNombre;
        }

        public String getPrimerApellido() {
            return primerApellido;
        }

        public void setPrimerApellido(String primerApellido) {
            this.primerApellido = primerApellido;
        }

        public String getSegundoApellido() {
            return segundoApellido;
        }

        public void setSegundoApellido(String segundoApellido) {
            this.segundoApellido = segundoApellido;
        }
    }
    public static class Telefono{
        private String numero;
        private String codigoPais;

        public String getNumero() {
            return numero;
        }

        public void setNumero(String numero) {
            this.numero = numero;
        }

        public String getCodigoPais() {
            return codigoPais;
        }

        public void setCodigoPais(String codigoPais) {
            this.codigoPais = codigoPais;
        }
    }
    public static class CorreoElectronico{
        private String dominio;
        private String valorCorreo;

        public String getDominio() {
            return dominio;
        }

        public void setDominio(String dominio) {
            this.dominio = dominio;
        }

        public String getValorCorreo() {
            return valorCorreo;
        }

        public void setValorCorreo(String valorCorreo) {
            this.valorCorreo = valorCorreo;
        }
    }
    public static class Direccion{
        private String ubigeo;
        private String ciudad;
        private String direccion;

        public String getUbigeo() {
            return ubigeo;
        }

        public void setUbigeo(String ubigeo) {
            this.ubigeo = ubigeo;
        }

        public String getCiudad() {
            return ciudad;
        }

        public void setCiudad(String ciudad) {
            this.ciudad = ciudad;
        }

        public String getDireccion() {
            return direccion;
        }

        public void setDireccion(String direccion) {
            this.direccion = direccion;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NombreCompleto getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(NombreCompleto nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public Telefono getTelefono() {
        return telefono;
    }

    public void setTelefono(Telefono telefono) {
        this.telefono = telefono;
    }

    public CorreoElectronico getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(CorreoElectronico correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public enum TipoUsuario{
        CLIENTE,
        AGENTE,
        PROPIETARIO
    }
}
