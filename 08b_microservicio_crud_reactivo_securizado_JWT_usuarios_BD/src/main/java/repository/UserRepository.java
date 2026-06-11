package repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import model.Usuario;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<Usuario, String> {
    Mono<Usuario> findByUser(String user);
}
