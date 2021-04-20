package net.thumbtack.school.hospital.validator;

import net.thumbtack.school.hospital.dao.UserDAO;
import net.thumbtack.school.hospital.exceptions.HospitalErrorCode;
import net.thumbtack.school.hospital.exceptions.HospitalException;
import net.thumbtack.school.hospital.model.User;
import net.thumbtack.school.hospital.model.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static net.thumbtack.school.hospital.model.UserType.ADMIN;

@Component
public class UserValidator {
    private final UserDAO userDAO;

    @Autowired
    public UserValidator(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void checkUserInSessionAdmin(String sessionId) throws HospitalException {
        User user = userDAO.getBySessionId(sessionId);
        if (user.getUserType() != ADMIN) {
            throw new HospitalException(HospitalErrorCode.USER_IS_NOT_ADMIN);
        }
    }

    public void checkUserExist(User user) throws HospitalException {
        if (user == null) {
            throw new HospitalException(HospitalErrorCode.USER_NOT_EXISTS);
        }
    }

    public void checkPasswordMatch(String firstPass, String secondPass) throws HospitalException {
        if (!firstPass.equals(secondPass)) {
            throw new HospitalException(HospitalErrorCode.WRONG_PASSWORD);
        }
    }

    public void checkUserIsDoctor(User user) throws HospitalException {
        if (!user.getUserType().equals(UserType.DOCTOR)) {
            throw new HospitalException(HospitalErrorCode.USER_IS_NOT_DOCTOR);
        }
    }

    public void checkUserIsPatient(User user) throws HospitalException {
        if (!user.getUserType().equals(UserType.PATIENT)) {
            throw new HospitalException(HospitalErrorCode.USER_IS_NOT_PATIENT);
        }
    }

    public void checkUserNotPatient(User user) throws HospitalException {
        if (user.getUserType().equals(UserType.PATIENT)) {
            throw new HospitalException(HospitalErrorCode.NOT_ENOUGH_RIGHTS);
        }
    }

    public void checkUserIsAdminOrMemberOfAppointment(User user, int patientId, List<Integer> doctorIds) throws HospitalException {
        if (!user.getUserType().equals(ADMIN) && patientId != user.getId() && !doctorIds.contains(user.getId())) {
            throw new HospitalException(HospitalErrorCode.NOT_ENOUGH_RIGHTS);
        }
    }

}
