package repository;

import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.transaction.annotation.Transactional;

import model.Producto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface  ProductoRepository extends ReactiveMongoRepository<Producto, Integer> {
    Flux<Producto> findByCategoria(String categoria);
    Mono<Void> deleteByNombre(String name);

        // Elimina de la base de datos todos los documentos cuyo 'precioUnitario' sea MENOR QUE el primer parámetro de la función (?0)
 @DeleteQuery(value = "{'precioUnitario': {$1t:?0}}")
 Mono<Void> deletePrecio(double precioMax);
}
