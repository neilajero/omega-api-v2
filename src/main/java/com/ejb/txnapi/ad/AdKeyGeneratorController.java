package com.ejb.txnapi.ad;

import jakarta.ejb.Local;
import java.util.Map;

@Local
public interface AdKeyGeneratorController {
    String generateKey(String username);

    Map<String, String> generateTokens(String username);

    String refreshToken(String refreshToken);
}