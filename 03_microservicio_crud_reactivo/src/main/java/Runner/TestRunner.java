package runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import model.Producto;
import reactor.core.publisher.Flux;

@Component
public class TestRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {

        WebClient client = WebClient.create("http://localhost:8000");

        Flux<Producto> productosFlux = client.get()
                .uri("/productos")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Producto.class);

        productosFlux.subscribe(p ->
                System.out.println(p.getNombre() + " - " + p.getPrecioUnitario())
        );
    }
}