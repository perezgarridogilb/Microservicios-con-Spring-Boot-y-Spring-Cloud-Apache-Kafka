package com.example.crudrapido.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;

    /**
     * Configura el codificador de claves
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Obtiene el AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtil, customUserDetailsService);
    }
/*
   Equivale en Laravel a la configuración centralizada de rutas y seguridad en 'routes/api.php' 
   y 'bootstrap/app.php':

   // 1. Deshabilitar CSRF y Stateless:
   // En Laravel API, CSRF está desactivado globalmente y la sesión es stateless por defecto.

   // 2. Definición de filtros de acceso (authorizeHttpRequests):
   Route::post('/api/auth/login', ...)->permitAll(); // .requestMatchers("/api/auth/**").permitAll()

   Route::middleware(['auth:sanctum', 'role:PACIENTE'])->group(function () {
       Route::get('/api/atenciones/mias', ...);
   });

   Route::middleware(['auth:sanctum', 'role:ADMIN,MEDICO'])->group(function () {
       Route::post('/api/...', ...);
       Route::put('/api/...', ...);
       Route::delete('/api/...', ...);
   });

   // 3. addFilterBefore(jwtAuthenticationFilter):
   // En Laravel, esto es el registro de un Middleware personalizado.
   // Se registra en 'bootstrap/app.php' y se aplica a las rutas protegidas:
   
   ->withMiddleware(function (Middleware $middleware) {
       $middleware->alias(['jwt.verify' => \App\Http\Middleware\JwtMiddleware::class]);
   });

   // La petición pasa primero por JwtMiddleware (filtro) antes de llegar al controlador,
   // exactamente igual que tu jwtAuthenticationFilter en Spring.
*/
    /**
     * Configura la cadena de filtros de seguridad
     */
    //AbstractHttpConfigurer::disable es un comando de configuración que sirve para desactivar la protección contra ataques CSRF (Cross-Site Request Forgery)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/api/auth/**").permitAll()
                    .requestMatchers("/api/atenciones/mias").hasRole("PACIENTE")
                    .requestMatchers(HttpMethod.POST, "/api/**").hasAnyRole("ADMIN","MEDICO")
                    .requestMatchers(HttpMethod.PUT, "/api/**").hasAnyRole("ADMIN","MEDICO")
                    .requestMatchers(HttpMethod.DELETE, "/api/**").hasAnyRole("ADMIN","MEDICO")
                .anyRequest().authenticated()
            )
            // Agregar el filtro JWT antes del filtro de autenticación por defecto
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
