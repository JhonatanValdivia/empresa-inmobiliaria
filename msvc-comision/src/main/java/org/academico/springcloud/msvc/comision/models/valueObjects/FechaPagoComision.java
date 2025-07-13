package org.academico.springcloud.msvc.comision.models.valueObjects;

import jakarta.persistence.Embeddable;

import java.time.LocalDate;
import java.util.Objects;

@Embeddable
public class FechaPagoComision {
    private LocalDate fechaPagoComision;

    public FechaPagoComision(){}
    public FechaPagoComision(LocalDate fechaPagoComision){
        if(fechaPagoComision==null){
            throw  new IllegalArgumentException("La fecha no puede ser nula");
        }
        if(fechaPagoComision.isAfter(LocalDate.now())){
            throw new IllegalArgumentException("La fecha no puede estar establecida en un tiempo futuro");
        }

        this.fechaPagoComision=fechaPagoComision;
    }
    //solo get, set no porque es un objeto de valor inmutable
    public LocalDate getFechaPagoComision(){
        return fechaPagoComision;
    }

    public boolean esHoy(){
        return fechaPagoComision.isEqual(LocalDate.now());
    }

    public boolean esAntesDe(LocalDate otraFecha){
        return fechaPagoComision.isBefore(otraFecha);
    }

    public boolean esDespuesDe(LocalDate otraFecha){
        return fechaPagoComision.isAfter(otraFecha);
    }
    //equals() y hascode() necesarios para que funcione como Object Value
    @Override
    public boolean equals(Object object){
        if(this==object)
            return true;

        if(!(object instanceof FechaPagoComision))
            return false;

        FechaPagoComision fechaPago=(FechaPagoComision) object;
        return fechaPagoComision.equals(fechaPago.fechaPagoComision);
    }
    @Override
    public int hashCode(){
        return Objects.hash(fechaPagoComision);
    }

    @Override
    public String toString(){
        return fechaPagoComision.toString(); // devuelve el valor de la fecha "yyyy-MM-dd"
    }

}

