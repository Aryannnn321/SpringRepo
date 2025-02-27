package com.insuranceapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.insuranceapp.util.JwtTokenUtil;

import ch.qos.logback.classic.Logger;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private AppUserServiceImpl appUserServiceImpl;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		 // get the header from the request
        String header = request.getHeader("Authorization");
        String jwtToken = null;
        String username =null;
        
        // if header is not null and starts with bearer
        if(header!=null && header.startsWith("Bearer ")) {
            // get the token
            jwtToken = header.substring(7);
            username = jwtTokenUtil.getUsernameFromToken(jwtToken);
        }else{
            logger.warn("Invalid Token");
        }
        // if the username is not null, check if it exists in the database
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        	//check in db call the service method
        	UserDetails userDetails = appUserServiceImpl.loadUserByUsername(username);
        	System.out.println(userDetails);
                // validate the token
                // if valid configure the security manually
                if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request, response);
        }
		
	}