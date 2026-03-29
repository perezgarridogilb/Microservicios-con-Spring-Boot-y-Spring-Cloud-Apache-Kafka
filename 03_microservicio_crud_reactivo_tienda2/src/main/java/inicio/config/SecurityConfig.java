package inicio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(csrf -> csrf.disable())       // Desactivar CSRF
                .formLogin(form -> form.disable())  // Desactivar login por form
                .httpBasic(basic -> basic.disable())// Desactivar http basic
                .authorizeExchange(ex -> ex.anyExchange().permitAll()) // Permitir todo
                .build();
    }
}