package service;

import java.util.Base64;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import model.Elemento;
import reactor.core.publisher.Flux;

@Service
public class ElementosServiceImpl implements ElementosService {
    String url1 = "http://localhost:8000";
    String url2 = "http://127.0.0.1:9001";

    private Flux<Elemento> catalogo(String url, String tienda) {
        // 1. Crear el cliente para la URL correspondiente
        WebClient webClient = WebClient.create(url);
        
        // 2. Inicializamos el builder del Request de forma dinámica
        var requestSpec = webClient.get().uri("/productos");

        // 3. Configuración condicional según el puerto/microservicio
        if (url.contains("8000")) {
            // Tienda 1: Requiere seguridad y responde como Stream de Eventos
            String credentials64 = getEncoderBase64Credentials("user1", "user1");
            requestSpec.accept(MediaType.TEXT_EVENT_STREAM);
            requestSpec.header("Authorization", "Basic " + credentials64);
        } else {
            // Tienda 2 (Puerto 9000): No tiene seguridad y responde con JSON estándar
            requestSpec.accept(MediaType.APPLICATION_JSON);
        }

        // 4. Ejecución del flujo reactivo
        return requestSpec
            .retrieve()
            .bodyToFlux(Elemento.class)
            .onErrorResume(e -> {
                System.out.println("Error WebClient en " + tienda + " (" + url + "): " + e.getMessage());
                return Flux.empty(); // Si el 9000 u 8000 fallan, el otro flujo continúa
            })
            .map(e -> {
                e.setTienda(tienda);
                return e;
            });
    }

    private String getEncoderBase64Credentials(String user, String pwd){
        String credential = user + ":" + pwd;
        return Base64.getEncoder().encodeToString(credential.getBytes());
    }

    @Override
    public Flux<Elemento> elementosPrecioMax(double precioMax) {
        Flux<Elemento> flux1 = catalogo(url1, "tienda 1");
        Flux<Elemento> flux2 = catalogo(url2, "tienda 2");
        
        return Flux.merge(flux1, flux2)
            .filter(e -> { 
                return (e.getPrecioUnitario() <= precioMax);
            });
    }
}