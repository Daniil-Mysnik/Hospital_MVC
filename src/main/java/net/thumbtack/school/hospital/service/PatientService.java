package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.converter.PatientConverter;
import net.thumbtack.school.hospital.dao.PatientDAO;
import net.thumbtack.school.hospital.dao.UserDAO;
import net.thumbtack.school.hospital.dto.request.CreatePatientRequest;
import net.thumbtack.school.hospital.dto.request.UpdatePatientRequest;
import net.thumbtack.school.hospital.dto.response.PatientResponse;
import net.thumbtack.school.hospital.exceptions.HospitalException;
import net.thumbtack.school.hospital.model.Patient;
import net.thumbtack.school.hospital.model.User;
import net.thumbtack.school.hospital.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientService {
    private final UserDAO userDAO;
    private final PatientDAO patientDAO;
    private final PatientConverter patientConverter;
    private final UserValidator userValidator;

    @Autowired
    public PatientService(UserDAO userDAO,
                          PatientDAO patientDAO,
                          PatientConverter patientConverter,
                          UserValidator userValidator) {
        this.userDAO = userDAO;
        this.patientDAO = patientDAO;
        this.patientConverter = patientConverter;
        this.userValidator = userValidator;
    }

    public PatientResponse create(CreatePatientRequest request) throws HospitalException {
        Patient patient = patientDAO.save(patientConverter.inflateEntity(request));
        return patientConverter.inflateResponse(patient);
    }

    public PatientResponse get(String sessionId, int patientId) throws HospitalException {
        User user = userDAO.getBySessionId(sessionId);
        userValidator.checkUserNotPatient(user);
        Patient patient = patientDAO.getById(patientId);
        return patientConverter.inflateResponse(patient);
    }

    public PatientResponse update(String sessionId, UpdatePatientRequest request) throws HospitalException {
        User user = userDAO.getBySessionId(sessionId);
        userValidator.checkPasswordMatch(user.getPassword(), request.getOldPassword());
        Patient patient = patientDAO.getById(user.getId());
        patient.setFirstName(request.getFirstName());
        patient.setLastName(request.getLastName());
        patient.setPatronymic(request.getPatronymic());
        patient.setPassword(request.getNewPassword());
        patient.setEmail(request.getEmail());
        patient.setAddress(request.getAddress());
        patient.setPhone(request.getPhone());
        patientDAO.update(patient);
        return patientConverter.inflateResponse(patient);
    }

}
