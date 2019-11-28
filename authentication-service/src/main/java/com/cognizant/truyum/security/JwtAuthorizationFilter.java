package com.cognizant.truyum.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.cognizant.truyum.TruyumConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter implements Filter {

	public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
		TruyumConstants.LOGGER.info("Start");
		TruyumConstants.LOGGER.debug("{}: ", authenticationManager);
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
		TruyumConstants.LOGGER.info("Start");
		String header = req.getHeader("Authorization");
		if(header == null || !header.startsWith("Bearer ")) {
			TruyumConstants.LOGGER.info("Inside if");
			chain.doFilter(req, res);
			return;
		}
		UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(req, res);
	}
	
	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		if(token != null) {
			Jws<Claims> jws;
			try {
				jws = Jwts.parser().setSigningKey("secretkey").parseClaimsJws(token.replaceFirst("Bearer ", ""));
				String user = jws.getBody().getSubject().split(":")[0];
				String role = jws.getBody().getSubject().split(":")[1];
				System.out.println("Username " + user);
				System.out.println("Role " + role);
				
				List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
				authorities.add(new SimpleGrantedAuthority(role));
				
				
				//String roles = jws.getBody();
				if(user != null) {
					return new UsernamePasswordAuthenticationToken(user, null, authorities);
				}
			}
			catch (JwtException ex) {
				return null;
			}
			return null;
		}
		return null;
	}
	
	
}
