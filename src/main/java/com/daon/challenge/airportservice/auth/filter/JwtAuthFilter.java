package com.daon.challenge.airportservice.auth.filter;

import com.daon.challenge.airportservice.auth.principal.UserPrincipal;
import com.daon.challenge.airportservice.auth.service.AirportserviceUserDetailsService;
import com.daon.challenge.airportservice.auth.token.TokenProvider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Getter
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final AirportserviceUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        var token = getToken(httpServletRequest);

        if (StringUtils.hasText(token) && tokenProvider.isValid(token)) {
            var username = tokenProvider.getUsername(token);
            var userPrincipal = (UserPrincipal) userDetailsService.loadUserByUsername(username);

            var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
            var webAuthenticationDetails = new WebAuthenticationDetailsSource().buildDetails(httpServletRequest);
            usernamePasswordAuthenticationToken.setDetails(webAuthenticationDetails);

            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private String getToken(HttpServletRequest request) {
        var bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}
