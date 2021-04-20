package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.dto.response.SettingsResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SettingsService {
    @Value("${max_name_length}")
    private int maxNameLength;

    @Value("${min_password_length}")
    private int minPasswordLength;

    public SettingsService() {
    }

    public SettingsResponse get() {
        return new SettingsResponse(maxNameLength, minPasswordLength);
    }

}
