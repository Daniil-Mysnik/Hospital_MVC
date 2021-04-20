package net.thumbtack.school.hospital.exceptions;

import net.thumbtack.school.hospital.dto.response.ErrorResponse;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.ServletException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public List<ErrorResponse> handleHospitalException(MethodArgumentNotValidException MNVex) {
        List<ErrorResponse> errorResponses = new ArrayList<>();
        String stringErrorCode;
        for (ObjectError fieldError : MNVex.getBindingResult().getGlobalErrors()) {
            stringErrorCode = Objects.requireNonNull(fieldError.getCode()).toUpperCase();
            if (EnumUtils.isValidEnum(HospitalErrorCode.class, stringErrorCode)) {
                HospitalErrorCode errorCode = HospitalErrorCode.valueOf(stringErrorCode);
                errorResponses.add(new ErrorResponse(errorCode.toString(), errorCode.getField(), errorCode.getMessage()));
            }
        }
        for (FieldError fieldError : MNVex.getBindingResult().getFieldErrors()) {
            errorResponses.add(new ErrorResponse(Objects.requireNonNull(fieldError.getCode()).toUpperCase(), fieldError.getField(), fieldError.getDefaultMessage()));
        }
        return errorResponses;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HospitalException.class)
    @ResponseBody
    public ErrorResponse handleHospitalException(HospitalException ex) {
        return new ErrorResponse(ex.getErrorCode().toString(), ex.getErrorCode().getField(), ex.getErrorCode().getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ServletException.class})
    @ResponseBody
    public ErrorResponse handleBodyError() {
        HospitalErrorCode errorCode = HospitalErrorCode.SESSION_NOT_EXIST;
        return new ErrorResponse(errorCode.toString(), errorCode.getField(), errorCode.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseBody
    public ErrorResponse handleNotFoundException() {
        HospitalErrorCode errorCode = HospitalErrorCode.NOT_FOUND;
        return new ErrorResponse(errorCode.toString(), errorCode.getField(), errorCode.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public ErrorResponse handleInternalError() {
        HospitalErrorCode errorCode = HospitalErrorCode.INTERNAL_SERVER_ERROR;
        return new ErrorResponse(errorCode.toString(), errorCode.getField(), errorCode.getMessage());
    }

}
