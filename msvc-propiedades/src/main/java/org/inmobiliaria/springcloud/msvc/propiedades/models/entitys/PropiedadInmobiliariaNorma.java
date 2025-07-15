package org.inmobiliaria.springcloud.msvc.propiedades.models.entitys;

import jakarta.persistence.*;

import java.util.Objects;
@Entity
@Table(name="Propiedades_normas")
public class PropiedadInmobiliariaNorma {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
@Column(name = "Norma_Id",unique = true)
private Long normaId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNormaId() {
        return normaId;
    }

    public void setNormaId(Long normaId) {
        this.normaId = normaId;
    }

    @Override
    public boolean equals(Object o) {
        if(this==o)
            return true;
        if(!(o instanceof PropiedadInmobiliariaNorma))
            return false;

        PropiedadInmobiliariaNorma propiedaNorma =(PropiedadInmobiliariaNorma) o;
        return this.normaId !=null&& this.normaId.equals(propiedaNorma.normaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, normaId);
    }
}

