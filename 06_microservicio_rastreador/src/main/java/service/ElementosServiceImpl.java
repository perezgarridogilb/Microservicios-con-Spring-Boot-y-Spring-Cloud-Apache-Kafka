package service;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import model.Elemento;
import reactor.core.publisher.Flux;

@Service
public class ElementosServiceImpl implements ElementosService {
String url1 = "http://localhost:8000";
String url2 = "http://localhost:9000";

    


private Flux<Elemento> catalogo(String url, String tienda) {
    WebClient webClient = WebClient.create(url);
    return webClient
        .get()
        .uri("/productos")
        .accept(MediaType.TEXT_EVENT_STREAM)
        .retrieve()
        .bodyToFlux(Elemento.class)
            .onErrorResume(e -> {
        System.out.println("Error WebClient: " + e.getMessage());
        return Flux.empty();
    })
        .map(e -> {
            e.setTienda(tienda);
            return e;
        });
}

    @Override
    public Flux<Elemento> elementosPrecioMax(double precioMax) {
        Flux<Elemento> flux1 = catalogo(url1, "tienda 1");
        Flux<Elemento> flux2 = catalogo(url2, "tienda 2");
        return Flux.merge(flux1, flux2)
        .filter(e->{ 
            return (e.getPrecioUnitario()<=precioMax);
        });
    }

}
