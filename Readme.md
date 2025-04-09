
# EXTRACCION_XML

- Microservicio que permite consultar un archivo xml por medio de un request y usando
el id se identifica al cliente que viene dentro del xml y como respuesta se obtiene los datos
sociodemograficos del cliente en formato Json

## 🚀 Características principales

- SpringBoot
- Lombok
- Java 17
- Maven
- Junit 5
- Jacoco
- Mockito
- Security
- Healt
  

## 📥 POSTMAN
## 1. Endpoint Busquedas

    http://localhost:8080/api/v1/busquedas

## 1.1 Request POST con el identificador del cliente 
```json
  {
     "id": 50
  }
```

## 1.2 Response con los datos sociodemograficos
```json
{
    "codigo": 200,
    "mensaje": "Operación exitosa",
    "resultado": {
        "personas": [
            {
                "id": 50,
                "nombre": "Alejandro",
                "apellidoPaterno": "Gamez",
                "apellidoMaterno": "Juarez",
                "fechaNacimiento": "25-01-1969",
                "curp": "JUGAHFOSNDDSAS01",
                "direccion": {
                    "calle": "rol de canela",
                    "colonia": "bosques",
                    "municipio": "Azcapotzalco",
                    "entidad": "CDMX",
                    "codigoPostal": "01010"
                }
            }
        ]
    }
}
```
## 1.3 Excepción para validar cuando el identificador no existe 
REQUEST

```json
  {
     "id": 1
  }
```
RESPONSE
```json
{
    "codigo": 404,
    "mensaje": "Error al procesar el xml",
    "resultado": "No se encontraron datos para el ID proporcionado."
}
```

## 2. Endpoint Monitor

    http://localhost:8080/api/monitor

## 2.1 Response GET 

```json
{
    "status": "UP",
    "components": {
        "diskSpace": {
            "status": "UP",
            "details": {
                "total": 750137634816,
                "free": 694813646848,
                "threshold": 10485760,
                "path": "C:\\Documentos\\xml\\.",
                "exists": true
            }
        },
        "monitor": {
            "status": "UP",
            "details": {
                "name": "xml",
                "version": "1.0.0",
                "javaVersion": "17.0.7",
                "runtime": "Java(TM) SE Runtime Environment"
            }
        },
        "ping": {
            "status": "UP"
        },
        "ssl": {
            "status": "UP",
            "details": {
                "validChains": [],
                "invalidChains": []
            }
        }
    }
}
```
## 🔒 SECURITY
- La clase webSecurityConfig controla y gestiona el acceso a los paths para que
  unicamente se pueda accesar a los que estan configurados en la clase
  
![security](https://github.com/user-attachments/assets/b44d2bf4-c0c6-4448-a1f9-6b2cb862e0d5)

## 📊 PRUEBAS UNITARIAS
- El microservicio tiene sus pruebas unitarias cubiertas al 100%

  ![jacoco](https://github.com/user-attachments/assets/d43fe1a2-b090-4cce-ba23-cbdf95c9945c)

