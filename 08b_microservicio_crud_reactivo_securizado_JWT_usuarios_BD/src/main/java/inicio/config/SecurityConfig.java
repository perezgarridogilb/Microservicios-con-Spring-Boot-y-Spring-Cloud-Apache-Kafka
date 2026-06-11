package inicio.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import service.UserDetailService;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Configuration
public class SecurityConfig {
	private AuthManager authenticationManager;
	private SecurityContextRepository securityContextRepository;
	private UserDetailService detailService;

	

	public SecurityConfig(AuthManager authenticationManager, SecurityContextRepository securityContextRepository,
			UserDetailService detailService) {
		this.authenticationManager = authenticationManager;
		this.securityContextRepository = securityContextRepository;
		this.detailService = detailService;
	}
	@Bean
	// Es solo para validación y matcheo automático durante el login
	public ReactiveUserDetailsService users() throws Exception{
		// fn($user) => $detailService->findByUsername($user)
		return user->detailService.findByUsername(user);
		
	}
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	@Bean
	public SecurityWebFilterChain filter(ServerHttpSecurity http) throws Exception{
		return http.csrf(c->c.disable())
		.authenticationManager(authenticationManager)
        .securityContextRepository(securityContextRepository)
		.authorizeExchange(auth->
					auth.pathMatchers(HttpMethod.POST, "/alta").hasAnyRole("ADMIN")
					.pathMatchers(HttpMethod.DELETE,"/eliminar/**").hasAnyRole("ADMIN","OPERATOR")
					.pathMatchers("/productos/**").authenticated()
					.anyExchange().permitAll()
				)
		.build();
	}
	
}
