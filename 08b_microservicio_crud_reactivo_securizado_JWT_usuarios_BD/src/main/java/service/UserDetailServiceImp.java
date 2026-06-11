package service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import model.Usuario;
import reactor.core.publisher.Mono;
import repository.RolRepository;
import repository.UserRepository;

@Service
public class UserDetailServiceImp implements UserDetailService {

    private RolRepository rolRepository;
    private UserRepository userRepository;

    

    public UserDetailServiceImp(RolRepository rolRepository, UserRepository userRepository) {
        this.rolRepository = rolRepository;
        this.userRepository = userRepository;
    }



    @Override
 public Mono<UserDetails> findByUsername(String user) {
		
		return userRepository.findByUser(user) //Mono<Usuario>
                .flatMap((Usuario us) -> rolRepository.findByIdUser(user)   //Flux<Rol>      		
                		.map(r->r.getId().getRol()) //Flux<String>
                        .collectList()//Mono<List<String>>
                        .map(roles -> User.withUsername(us.getUser())
                                .password("{noop}" + us.getPwd())
                                .roles(roles.toArray(new String[0]))
                                .build())) //Mono<UserDetails>
                .switchIfEmpty(Mono.empty());		
	}

}
