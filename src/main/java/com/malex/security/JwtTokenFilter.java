package com.malex.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtTokenFilter extends OncePerRequestFilter {

  @Value("${jwt.app.secret}")
  private String jwtSecret;

  @Value("${jwt.app.enable_security}")
  private boolean enableSecurity;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {
    if (enableSecurity) {
      var jwtToken = getJwtTokenFromAuthorizationHeader(request);
      verifyJwtToken(jwtToken);
    }
    chain.doFilter(request, response);
  }

  private String getJwtTokenFromAuthorizationHeader(HttpServletRequest req)
      throws ServletException {
    return Optional.ofNullable(req.getHeader("Authorization"))
        .filter(header -> header.startsWith("Bearer "))
        .map(header -> header.substring(7))
        .orElseThrow(() -> new ServletException("JWT token not found in request headers"));
  }

  private void verifyJwtToken(String jwtToken) throws ServletException {
    try {
      var signedJWT = SignedJWT.parse(jwtToken);
      var verifier = new MACVerifier(jwtSecret);
      if (!signedJWT.verify(verifier)) {
        throw new ServletException("JWT token verification failed");
      }
      // Token is valid
      // You can extract claims from the JWT token if needed
    } catch (ParseException | JOSEException e) {
      throw new ServletException("Error parsing JWT token", e);
    }
  }
}
