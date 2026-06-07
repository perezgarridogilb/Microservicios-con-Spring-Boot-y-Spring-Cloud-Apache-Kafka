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
        return Flux.fromIterable(productos)
                .delayElements(Duration.ofMillis(500));
    }

    @Override
    public Flux<Producto> productosCategoria(String categoria) {
        return catalogo()
        .filter(p->p.getCategoria().equals(categoria));
    }

    @Override
    public Mono<Producto> productoCodigo(int cod) {
        return catalogo() // Flux<Producto>
        .filter(p->p.getCodProducto()==cod) // Flux<Producto>
        .next(); // Mono<Producto>
    }

    @Override
    public Mono<Void> altaProducto(Producto producto) {
        
        return productoCodigo(producto.getCodProducto()) // Mono<Producto>
        .switchIfEmpty(Mono.just(producto).map(p->{
            productos.add(producto);
            return p;
        })) // Mono<Producto>
        .then(); // Mono<Void>
    }

    @Override
    public Mono<Producto> eliminarProducto(int cod) {
        return productoCodigo(cod) // Mono<Producto>
        .map(p->{
            productos.removeIf(r->r.getCodProducto()==cod);
            return p;
        }) // Mono<Producto>
        /* .switchIfEmpty(null) */; 
    }

    @Override
    public Mono<Producto> actualizarPrecio(int cod, double precio) {
        return productoCodigo(cod) // Mono<Producto>
        .map(p->{
            p.setPrecioUnitario(precio);
            return p;
        });
    }

}
