package com.extraccion.xml.model.response;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
@XmlAccessorType(XmlAccessType.FIELD)
public class Persona {

    @XmlElement(name = "id")
    private String id;

    @XmlElement(name = "nombre")
    private String nombre;

    @XmlElement(name = "apellidoPaterno")
    private String apellidoPaterno;

    @XmlElement(name = "apellidoMaterno")
    private String apellidoMaterno;

    @XmlElement(name = "fechaNacimiento")
    private String fechaNacimiento;

    @XmlElement(name = "curp")
    private String curp;

    @XmlElement
    private Direccion direccion;
}
