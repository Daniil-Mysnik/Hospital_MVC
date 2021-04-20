package net.thumbtack.school.hospital.controller;

import net.thumbtack.school.hospital.dto.response.ErrorResponse;
import net.thumbtack.school.hospital.exceptions.HospitalErrorCode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestSettingController extends TestControllerBase {

    @Test
    public void testGetSettingsNotAuthorized() {
        try {
            getSettings(null);
            fail();
        } catch (HttpClientErrorException e) {
            assertEquals(gson.fromJson(e.getResponseBodyAsString(), ErrorResponse.class).getErrorCode(), HospitalErrorCode.SESSION_NOT_EXIST.toString());
            assertEquals(gson.fromJson(e.getResponseBodyAsString(), ErrorResponse.class).getField(), HospitalErrorCode.SESSION_NOT_EXIST.getField());
        }
    }

}
