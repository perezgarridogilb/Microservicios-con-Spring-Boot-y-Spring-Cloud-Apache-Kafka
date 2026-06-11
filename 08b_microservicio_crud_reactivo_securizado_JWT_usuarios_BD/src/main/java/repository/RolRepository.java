package repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import model.Rol;
import model.RolPk;
import reactor.core.publisher.Flux;

public interface RolRepository extends ReactiveCrudRepository<Rol, RolPk> {
    @Query("select * from roles where roles.user=?")
    Flux<Rol> findByIdUser(String user);
}
