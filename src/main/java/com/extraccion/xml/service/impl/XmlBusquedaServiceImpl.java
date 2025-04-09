package com.extraccion.xml.service.impl;

import com.extraccion.xml.constantes.Constantes;
import com.extraccion.xml.model.response.ResponseBusqueda;
import com.extraccion.xml.service.XmlBusquedaService;
import com.extraccion.xml.utils.archivos.BusquedaDocumento;
import com.extraccion.xml.utils.archivos.Documento;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Implementación del servicio para búsquedas de personas en archivos XML
 *
 * @author Jonathan Garcia
 * @version 1.0.0
 * @since 05-04-2025
 */
@Service
public class XmlBusquedaServiceImpl implements XmlBusquedaService {

    /**
     * Inicializar los logs
     */
    private static final Logger logger = LogManager.getLogger(XmlBusquedaServiceImpl.class);

    /**
     * componente para consultar el archivo xml
     */
    @Autowired
    private Documento file;
    @Autowired
    private BusquedaDocumento busqueda;

    /**
     * Implementación que busca una persona por ID en un archivo XML
     *
     * @param id Identificador único de la persona. Debe ser un valor positivo.
     * @return {@link ResponseBusqueda} con los datos encontrados o {@code null} si:
     *         <ul>
     *           <li>El ID es nulo (se registra advertencia)</li>
     *           <li>No se encuentra la persona (se registra info)</li>
     *         </ul>
     *
     * @throws RuntimeException Si ocurre un error al procesar el XML
     * @implNote Flujo de ejecución:
     * <ol>
     *   <li>Valida que el ID no sea nulo</li>
     *   <li>Busca el nodo correspondiente en el XML</li>
     *   <li>Construye la respuesta desde el nodo encontrado</li>
     * </ol>
     */
    @Override
    public ResponseBusqueda buscarPersonaPorId(Long id) {
        try {
            if (id == null) {
                logger.warn(Constantes.NULO);
                return null;
            }

            Document document = file.getDocument();
            Node personaNode = busqueda.findPersonNodeById(document, id);

            if (personaNode == null) {
                logger.info(Constantes.NO_ENCONTRO, id);
                return null;
            }

            return busqueda.buildResponseFromNode((Element) personaNode);

        } catch (Exception e) {
            logger.error(Constantes.ERROR_XML_ID, id, e);
            throw new RuntimeException(Constantes.ERROR_XML, e);
        }
    }
}