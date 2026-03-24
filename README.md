# Parcial TDSE - Secuencia de Lucas

## Diseño

- Se implemento un servicio backend que calcula la secuencia de Lucas.
- Se implemento un proxy que recibe la peticion del cliente y delega al backend.
- El proxy soporta `GET` y `POST`.
- El parametro de entrada es `value` por query string.
- La respuesta es JSON con este formato:

```json
{
  "operation": "Secuencia de Lucas",
  "input": 13,
  "output": "2, 1, 3, 4, 7, 11, 18, 29, 47, 76, 123, 199, 322, 521"
}
```

## Endpoints

- Backend: `/api/lucasseq?value=n`
- Proxy: `/lucasseq?value=n`

## Frontend

Se incluyo un cliente web minimo en `src/main/resources/index.html`.
Hace llamadas asincronas al proxy con botones para GET y POST.

## Despliegue

El proxy usa la variable de entorno `BACKEND_URLS` para definir los backends en round-robin.
Acepta valores separados por coma (host:puerto o URL completa).

Video: https://youtu.be/NfC7kwac1Qs
