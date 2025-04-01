package com.extraccion.xml.model.response;

import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
@XmlRootElement(name = "datos")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResponseBusqueda{

    @XmlElement(name = "persona")
    private Persona persona;
}
