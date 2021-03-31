package com.redvinck.SpringBootProjectWeekCloud;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.Assert;

class ScopeValidator implements OAuth2TokenValidator<Jwt> {
    private final String scope;

    ScopeValidator(String scope) {
        Assert.hasText(scope, "scope is null or empty");
        this.scope = scope;
    }

    public OAuth2TokenValidatorResult validate(Jwt jwt) {
        String scopes = jwt.getClaims().get("scope").toString();
        System.out.println("Contains sequence 'partner': " + jwt.getClaims().get("scope").toString());
        System.out.println("Contains sequence 'partner': " + jwt.getClaims().get("scope").toString().contains("partner"));
        if (scopes.contains(this.scope)) {
            System.out.println("Successful ScopeValidator");
            return OAuth2TokenValidatorResult.success();
        }
        OAuth2Error err = new OAuth2Error(OAuth2ErrorCodes.INVALID_TOKEN);
        return OAuth2TokenValidatorResult.failure(err);
    }
}