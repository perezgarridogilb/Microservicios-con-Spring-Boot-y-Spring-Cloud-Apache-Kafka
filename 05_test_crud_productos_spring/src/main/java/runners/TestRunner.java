package runners;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import model.Producto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class TestRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {

        WebClient client = WebClient.create("http://localhost:8000");

    //     Flux<Producto> flux = client.get()
    //             .uri("/productos")
    // .accept(MediaType.TEXT_EVENT_STREAM) // <- importante
    //             .retrieve()
    //             .bodyToFlux(Producto.class);

// flux.subscribe(p ->
//     System.out.println(
//         java.time.LocalTime.now() + " -> " +
//         p.getNombre() + " - " + p.getPrecioUnitario()
//     )
// );
Producto producto = new Producto(200, "prueba", "categoria test", 5.0, 20);

        client
        .post()
        .uri("/alta")
      .body(Mono.just(producto), Producto.class)
        //        .uri("/producto?cod=102")
        .accept(MediaType.TEXT_EVENT_STREAM)
        .retrieve()
        .bodyToMono(Void.class)
        .doOnTerminate(()->
        System.out.println("Se ha dado de alta el producto, o no"))
        .block();
    }
}