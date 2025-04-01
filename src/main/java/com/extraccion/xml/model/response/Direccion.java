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
public class Direccion {

    @XmlElement(name = "calle")
    private String calle;

    @XmlElement(name = "colonia")
    private String colonia;

    @XmlElement(name = "municipio")
    private String municipio;

    @XmlElement(name = "entidad")
    private String entidad;

    @XmlElement(name = "codigoPostal")
    private String codigoPostal;
}
