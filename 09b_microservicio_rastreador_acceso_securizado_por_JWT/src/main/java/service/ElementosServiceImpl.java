package service;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import model.Credentials;
import model.Elemento;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service

public class ElementosServiceImpl implements ElementosService {
    String url1 = "http://localhost:8000";
    String url2 = "http://127.0.0.1:9001";

@Value("${app.security.user}")
    String user;
    
    @Value("${app.security.pwd}")
    String pwd;

String token1, token2;

@jakarta.annotation.PostConstruct
public void init(){
    Credentials credentials1 = new Credentials(user, pwd);
    Credentials credentials2 = new Credentials(user, pwd);
    loadToken(url1, credentials1).subscribe(s->token1=s);
    loadToken(url2, credentials2).subscribe(s->token2=s);
}

private Mono<String> loadToken(String url, Credentials credentials) {
    System.out.println("[DEBUGGER] Iniciando petición de login hacia: " + url + "/login");
    System.out.println("[DEBUGGER] Credenciales enviadas -> User: " + credentials);
    return WebClient.create(url)
    .post()
    .uri("/login")
    .contentType(MediaType.APPLICATION_JSON)
    .bodyValue(credentials)
    .accept(MediaType.TEXT_PLAIN)
    .retrieve()
    .bodyToMono(String.class)
    .onErrorReturn("TOKEN_ERROR");
}

    private Flux<Elemento> catalogo(String url, String tienda, String token) {
        // 1. Crear el cliente para la URL correspondiente
        WebClient webClient = WebClient.create(url);
        
        // 2. Inicializamos el builder del Request de forma dinámica
        var requestSpec = webClient.get().uri("/productos");

        // 3. Configuración condicional según el puerto/microservicio
        if (url.contains("8000")) {
            // Tienda 1: Requiere seguridad y responde como Stream de Eventos
            String credentials64 = getEncoderBase64Credentials("user1", "user1");
            requestSpec.accept(MediaType.TEXT_EVENT_STREAM);
            requestSpec.header("Authorization", "Bearer " + token);
        } else {
            // Tienda 2 (Puerto 9000): No tiene seguridad y responde con JSON estándar
                      requestSpec.accept(MediaType.TEXT_EVENT_STREAM);
            requestSpec.header("Authorization", "Bearer " + token);
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
        Flux<Elemento> flux1 = catalogo(url1, "tienda 1", token1);
        Flux<Elemento> flux2 = catalogo(url2, "tienda 2", token2);
        
        return Flux.merge(flux1, flux2)
            .filter(e -> { 
                return (e.getPrecioUnitario() <= precioMax);
            });
    }
}