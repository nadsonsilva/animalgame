package com.example.animalgame.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        System.out.println("JWT inicializado com sucesso.");
    }

    public String gerarToken(String email) {
        Date agora = new Date();
        Date validade = new Date(agora.getTime() + expiration);

        String token = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(agora)
                .setExpiration(validade)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        System.out.println("Token gerado para: " + email);
        System.out.println("Expiração do token: " + validade);

        return token;
    }

    public String extrairEmail(String token) {
        String email = extrairClaims(token).getSubject();
        System.out.println("Email extraído do token: " + email);
        return email;
    }

    public boolean tokenValido(String token) {
        try {
            Claims claims = extrairClaims(token);
            Date expiracao = claims.getExpiration();
            Date agora = new Date();

            System.out.println("=== VALIDAÇÃO JWT ===");
            System.out.println("Expiração do token: " + expiracao);
            System.out.println("Data atual: " + agora);

            boolean valido = expiracao.after(agora);
            System.out.println("Token válido? " + valido);

            return valido;
        } catch (Exception e) {
            System.out.println("Erro ao validar token: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private Claims extrairClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}