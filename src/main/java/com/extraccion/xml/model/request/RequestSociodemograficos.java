package com.extraccion.xml.model.request;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
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
