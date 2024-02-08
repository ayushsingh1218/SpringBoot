package Ecommerce.service;

import org.springframework.stereotype.Service;

import Ecommerce.entity.User;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class SessionService {

    public final Map<String, User> loggedInUsers = new HashMap<>();

    public String createSession(User user) {
        String sessionId = generateSessionId();
        loggedInUsers.put(sessionId, user);
        return sessionId;
    }

    public User getUserBySessionId(String sessionId) {
        return loggedInUsers.get(sessionId);
    }

    public void removeSession(String sessionId) {
        loggedInUsers.remove(sessionId);
    }

    public String generateSessionId() {
        return UUID.randomUUID().toString();
    }
}