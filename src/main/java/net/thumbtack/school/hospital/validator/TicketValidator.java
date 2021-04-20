package net.thumbtack.school.hospital.validator;

import net.thumbtack.school.hospital.dto.request.TicketRequest;
import net.thumbtack.school.hospital.exceptions.HospitalErrorCode;
import net.thumbtack.school.hospital.exceptions.HospitalException;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class TicketValidator implements ConstraintValidator<IdOrSpeciality, TicketRequest> {

    @Override
    public void initialize(IdOrSpeciality constraintAnnotation) {

    }

    @Override
    public boolean isValid(TicketRequest request, ConstraintValidatorContext constraintValidatorContext) {
        return (request.getDoctorId() == 0 && request.getSpeciality() != null) || (request.getDoctorId() != 0 && request.getSpeciality() == null);
    }

}
