package service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import controller.ProductosController;
import model.Producto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import repository.ProductosRepository;

@Service
public class ProductosServiceImpl implements ProductosService {

    @Autowired
    ProductosRepository productosRepository;

    @Override
    public Flux<Producto> catalogo() {
        return productosRepository.findAll()
                .delayElements(Duration.ofMillis(500));
    }

    @Override
    public Flux<Producto> productosCategoria(String categoria) {
        // return catalogo()
        // .filter(p->p.getCategoria().equals(categoria));
        return productosRepository.findByCategoria(categoria);
    }

    @Override
    public Mono<Producto> productoCodigo(int cod) {
        // return catalogo() // Flux<Producto>
        // .filter(p->p.getCodProducto()==cod) // Flux<Producto>
        // .next(); // Mono<Producto>
        return productosRepository.findById(cod);
    }

    @Override
/**
 * Realiza el alta de un nuevo producto en el sistema de forma reactiva.
 * Valida que el producto no exista previamente por su código antes de persistirlo.
 * * @param producto El objeto Producto que se desea dar de alta.
 * @return Un Mono<Void> que emite una señal vacía cuando la operación finaliza con éxito.
 */
public Mono<Void> altaProducto(Producto producto) {
    
    return productoCodigo(producto.getCodProducto()) // 1. Busca si el producto ya existe en la BD (Retorna Mono<Producto>)
        
        // 2. Si el Mono anterior viene vacío (el producto NO existe), se activa este bloque condicional
        .switchIfEmpty(
            // Envuelve el objeto síncrono 'producto' en un flujo reactivo (Mono)
            Mono.just(producto) 
                // Guarda el producto en la BD de forma asíncrona y aplanar el Mono<Producto> resultante
                .flatMap(p -> productosRepository.save(p)) 
        ) 
        
        // 3. Ignora el objeto Producto emitido (ya sea el encontrado o el guardado) 
        // y lo transforma en un Mono<Void> para retornar únicamente una señal de éxito.
        .then(); 
}

    @Override
    public Mono<Producto> eliminarProducto(int cod) {
/*         return productoCodigo(cod) // Mono<Producto>
        .map(p->{
            productos.removeIf(r->r.getCodProducto()==cod);
            return p;
        }) */ // Mono<Producto>
        /* .switchIfEmpty(null) */; 
        return productoCodigo(cod

        ).flatMap(p->productosRepository.deleteById(cod).then(Mono.just(p)));
    }

    @Override
    public Mono<Producto> actualizarPrecio(int cod, double precio) {
        return productoCodigo(cod) // Mono<Producto>
        .flatMap(p->{
            p.setPrecioUnitario(precio);
            return productosRepository.save(p);
        });
    }

}
