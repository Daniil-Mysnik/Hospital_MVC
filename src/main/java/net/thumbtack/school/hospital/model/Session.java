package net.thumbtack.school.hospital.model;

import java.util.Objects;

public class Session {
    private String sessionId;
    private User user;

    public Session() {
    }

    public Session(String sessionId, User user) {
        this.sessionId = sessionId;
        this.user = user;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Session session = (Session) o;
        return Objects.equals(sessionId, session.sessionId) &&
                Objects.equals(user, session.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionId, user);
    }

}
