package com.example.btbuoi7.Mapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;

import java.util.Arrays;

@Configuration
public class SecurityBeans {

    @Bean
    public AccessDecisionManager accessDecisionManager() {
        return new AffirmativeBased(Arrays.asList(
                new RoleVoter(),
                new AuthenticatedVoter()
        ));
    }
}
