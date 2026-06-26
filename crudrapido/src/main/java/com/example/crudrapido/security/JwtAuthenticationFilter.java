package com.example.crudrapido.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

        private final JwtUtil jwtUtil;
        private final CustomUserDetailsService customUserDetailsService;

     @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                   FilterChain filterChain) throws ServletException, IOException {
        try {
            // Obtener el token del header
            String token = getTokenFromRequest(request);

            // Validar que el token exista y sea válido
            if (token != null && jwtUtil.validateToken(token)) {
                // Extraer el username del token
                String username = jwtUtil.getUsuarioFromToken(token);

                // Cargar el usuario desde la base de datos
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

                // Crear el objeto de autenticación
                UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(
                        userDetails, 
                        null, 
                        userDetails.getAuthorities()
                    );
                
                // Establecer detalles de la autenticación
                authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // Establecer la autenticación en el contexto de seguridad
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Error al autenticar usuario con JWT: {}" + e.getMessage());
        }

        // Continuar con el siguiente filtro
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
            
        }
        return null;
    }

}
