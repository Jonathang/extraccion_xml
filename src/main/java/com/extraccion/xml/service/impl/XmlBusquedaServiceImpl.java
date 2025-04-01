package com.extraccion.xml.service.impl;

import com.extraccion.xml.model.response.ResponseBusqueda;
import com.extraccion.xml.service.XmlBusquedaService;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

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
