package com.extraccion.xml.service.impl;

import com.extraccion.xml.constantes.Constantes;
import com.extraccion.xml.model.response.Persona;
import com.extraccion.xml.model.request.RequestSociodemograficos;
import com.extraccion.xml.model.response.ResponseBusqueda;
import com.extraccion.xml.service.XmlBusquedaService;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.StringReader;

@Service
public class XmlBusquedaServiceImpl implements XmlBusquedaService {

    @Override
    public ResponseBusqueda buscarPersonaPorId(Long id) {
        try {
            // Cargar el archivo XML desde resources
            ClassPathResource resource = new ClassPathResource("datos.xml");

            // Configurar JAXB
            JAXBContext context = JAXBContext.newInstance(ResponseBusqueda.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            // Leer el XML
            ResponseBusqueda responseBusqueda = (ResponseBusqueda) unmarshaller.unmarshal(resource.getInputStream());

            // Verificar si el ID coincide
            if (responseBusqueda.getPersona() != null &&
                    responseBusqueda.getPersona().getId().equals(id)) {
                return responseBusqueda;
            }

            return null;

        } catch (Exception e) {
            throw new RuntimeException("Error al procesar el XML", e);
        }
    }
}
