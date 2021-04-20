package net.thumbtack.school.hospital.dto.response;

public class LoginResponse {
    private String sessionId;
    private UserResponse userResponse;

    public LoginResponse() {
    }

    public LoginResponse(String sessionId, UserResponse userResponse) {
        this.sessionId = sessionId;
        this.userResponse = userResponse;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public UserResponse getUserResponse() {
        return userResponse;
    }

    public void setUserResponse(UserResponse userResponse) {
        this.userResponse = userResponse;
    }

}
