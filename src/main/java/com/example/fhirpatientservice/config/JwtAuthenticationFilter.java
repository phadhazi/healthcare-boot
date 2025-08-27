package com.example.fhirpatientservice.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final String secret = "mySuperSecretKey123mySuperSecretKey123"; // 32+ karakter a HS256-hoz

    private final Key key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)); // ÚJ: SecretKey típus

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.replace("Bearer ", "");

            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                String role = claims.get("role", String.class);

                System.out.println("JWT role from token: " + role);

                List<GrantedAuthority> authorities = List.of(
                        new SimpleGrantedAuthority("ROLE_" + role)
                );

                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        claims.getSubject(), null, authorities);

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (JwtException e) {
                response.setContentType("application/fhir+json");
                response.setCharacterEncoding("UTF-8");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

                OperationOutcome outcome = new OperationOutcome();
                OperationOutcome.OperationOutcomeIssueComponent issue = outcome.addIssue();
                issue.setSeverity(OperationOutcome.IssueSeverity.ERROR);
                issue.setCode(OperationOutcome.IssueType.LOGIN);
                issue.setDiagnostics("Invalid or expired JWT token.");

                FhirContext ctx = FhirContext.forR4();
                IParser jsonParser = ctx.newJsonParser();
                String json = jsonParser.encodeResourceToString(outcome);

                response.getWriter().write(json);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
