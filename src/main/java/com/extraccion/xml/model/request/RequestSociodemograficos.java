package com.extraccion.xml.model.request;

import com.extraccion.xml.model.response.Persona;
import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
@XmlAccessorType(XmlAccessType.FIELD)
public class RequestSociodemograficos {

    @XmlAttribute
    private String id;
}
