package org.academico.springcloud.msvc.venta.models.valueObjects;

import jakarta.persistence.Embeddable;

import java.time.LocalDate;
import java.util.Objects;

@Embeddable // indica que la clase se puede integrar en una entidad y no tiene su propia identidad independiente.
public class FechaVenta
{
    private LocalDate fechaVenta;

    public FechaVenta(){}
    public FechaVenta(LocalDate fechaVenta){
        if(fechaVenta==null){
            throw  new IllegalArgumentException("La fecha no puede ser nula");
        }
        if(fechaVenta.isAfter(LocalDate.now())){
            throw new IllegalArgumentException("La fecha no puede ser en futuro");
        }

        this.fechaVenta=fechaVenta;
    }
    //solo get, set no porque es un objeto de valor inmutable
    public LocalDate getFechaVenta(){
        return fechaVenta;
    }

    public boolean esHoy(){
        return fechaVenta.isEqual(LocalDate.now());
    }

    public boolean esAntesDe(LocalDate otraFecha){
        return fechaVenta.isBefore(otraFecha);
    }

    public boolean esDespuesDe(LocalDate otraFecha){
        return fechaVenta.isAfter(otraFecha);
    }
    //equals() y hascode() necesarios para que funcione como Object Value
    @Override
    public boolean equals(Object object){
        if(this==object)
            return true;

        if(!(object instanceof FechaVenta))
            return false;

        FechaVenta fecha=(FechaVenta) object;
        return fechaVenta.equals(fecha.fechaVenta);
    }
    @Override
    public int hashCode(){
        return Objects.hash(fechaVenta);
    }

    @Override
    public String toString(){
        return fechaVenta.toString(); // devuelve el valor de la fecha "yyyy-MM-dd"
    }

}
