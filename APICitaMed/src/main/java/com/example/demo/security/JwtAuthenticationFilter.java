package com.example.demo.security;

import java.io.IOException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.entity.Usuario;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.servicesImpl.JWTServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	@Qualifier("JwtService")
	private JWTServiceImpl jwtService;

	@Autowired
	@Qualifier("UsuarioRepository")
	private UsuarioRepository usuarioRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String authHeader = request.getHeader("Authorization");
		
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			String jwt = authHeader.substring(7);
			try {
				String username = jwtService.extractUsername(jwt);
				if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
					Usuario user = usuarioRepository.findByEmail(username);
					if (user != null) {
						SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRol());
						UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
								user.getEmail(), null, Collections.singletonList(authority));
						SecurityContextHolder.getContext().setAuthentication(authToken);
					}
				}
			} catch (Exception e) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().write("Token inválido o expirado: " + e.getMessage());
				return;
			}
		}

		filterChain.doFilter(request, response);
	}

}
