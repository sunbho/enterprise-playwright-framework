package com.playwright.framework.api;

import com.microsoft.playwright.APIResponse;
import com.playwright.framework.config.ConfigManager;
import com.playwright.framework.exceptions.ApiException;
import com.playwright.framework.models.GenerateTokenRequest;
import com.playwright.framework.models.GenerateTokenResponse;

import java.time.Instant;

/**
 * Generates, stores, reuses, and refreshes JWT tokens per execution thread.
 */
public final class TokenManager {

    private static final String USERNAME_PROPERTY = "api.username";
    private static final String PASSWORD_PROPERTY = "api.password";
    private static final ThreadLocal<TokenState> TOKEN_STATE = new ThreadLocal<>();

    private TokenManager() {
        throw new IllegalStateException("TokenManager must not be instantiated");
    }

    public static String getToken() {
        TokenState state = TOKEN_STATE.get();
        if (state == null || state.isExpired()) {
            state = generateToken();
            TOKEN_STATE.set(state);
        }
        return state.token();
    }

    public static String refreshToken() {
        TokenState state = generateToken();
        TOKEN_STATE.set(state);
        return state.token();
    }

    public static void clearToken() {
        TOKEN_STATE.remove();
    }

    private static TokenState generateToken() {
        ConfigManager config = ConfigManager.getInstance();
        GenerateTokenRequest request = new GenerateTokenRequest(
                requiredProperty(config, USERNAME_PROPERTY),
                requiredProperty(config, PASSWORD_PROPERTY));

        AccountApi accountApi = new AccountApi(ApiFactory.getContext());
        APIResponse response = accountApi.generateToken(request);
        if (response.status() != 200) {
            throw new ApiException("Token generation failed with status: " + response.status());
        }

        GenerateTokenResponse tokenResponse = accountApi.deserialize(response, GenerateTokenResponse.class);
        if (tokenResponse.getToken() == null || tokenResponse.getToken().isBlank()) {
            throw new ApiException("Token generation response did not include a token");
        }

        return new TokenState(tokenResponse.getToken(), Instant.now().plusSeconds(55 * 60));
    }

    private static String requiredProperty(ConfigManager config, String key) {
        String value = config.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Required API authentication property is missing: " + key);
        }
        return value;
    }

    private record TokenState(String token, Instant expiresAt) {
        private boolean isExpired() {
            return Instant.now().isAfter(expiresAt);
        }
    }
}
