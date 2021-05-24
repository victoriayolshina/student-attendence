package ru.isu.studentattendance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import ru.isu.studentattendance.domain.model.AutoUser;
import ru.isu.studentattendance.domain.repository.AutoUserRepository;

@Component("customAuthenticationProvider")
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    AutoUserRepository repo;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        AutoUser user = repo.findByUsername(token.getName());
        if (user==null || !user.getPassword().equalsIgnoreCase(token.getCredentials().toString())){
            throw new BadCredentialsException("No user or password");
        }
        return new UsernamePasswordAuthenticationToken(user,user.getPassword(), user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UsernamePasswordAuthenticationToken.class.equals(aClass);
    }
}
