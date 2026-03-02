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

//         Flux<Producto> flux = client.get()
//                 .uri("/productos")
//     .accept(MediaType.TEXT_EVENT_STREAM) // <- importante
//                 .retrieve()
//                 .bodyToFlux(Producto.class);

// flux.subscribe(p ->
//     System.out.println(
//         java.time.LocalTime.now() + " -> " +
//         p.getNombre() + " - " + p.getPrecioUnitario()
//     )
// );

// Producto producto = new Producto(200, "prueba1", "categoria test", 5.0, 20);

//         client
//         .post()
//         .uri("/alta")
//       .body(Mono.just(producto), Producto.class)
//         //        .uri("/producto?cod=102")
//         .accept(MediaType.TEXT_EVENT_STREAM)
//         .retrieve()
//         .bodyToMono(Void.class)
//         .doOnTerminate(()->
//         System.out.println("Se ha dado de alta el producto, o no"))
//         .block();
//   Mono<Producto> monoFind = client
//     .get()
//     .uri("/productos/{cod}", 102)
//     .retrieve()
//     .bodyToMono(Producto.class);
//     monoFind.subscribe(p -> System.out.println(p));
//     monoFind.switchIfEmpty(Mono.just(new Producto()).map(p->{
//         System.out.println("No se ha encontrado prod");
//         return p;
//     })).block();
client
    .delete()
    .uri("/productos/{cod}", 102)
    .retrieve()
    .onStatus(h -> h.is4xxClientError(), t -> {
        System.out.println("no se encuentra");
        return Mono.empty();
    })
    .bodyToMono(Void.class)
    .subscribe(v -> System.out.println("Producto eliminado" + v))
;    // monoFind.switchIfEmpty(Mono.just(new Producto()).map(p->{
    //     System.out.println("No se ha encontrado prod");
    //     return p;
    // })).block();

    }
}