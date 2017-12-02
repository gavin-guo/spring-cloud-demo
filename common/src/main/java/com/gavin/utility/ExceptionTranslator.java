package com.gavin.utility;

import com.gavin.dto.common.CustomResponseBody;
import com.gavin.exception.CustomException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ExceptionTranslator {

    public static ResponseEntity<CustomResponseBody> translate(Exception e) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Cache-Control", "no-store");
        headers.set("Pragma", "no-cache");

        CustomResponseBody body = new CustomResponseBody();

        ResponseEntity<CustomResponseBody> response;
        HttpStatus httpStatus;
        if (e instanceof CustomException) {
            CustomException ce = (CustomException) e;
            body.setCode(ce.getErrorCode());
            body.setMessage(ce.getMessage());
            httpStatus = HttpStatus.valueOf(ce.getHttpCode());
        } else {
            body.setCode("server_error");
            body.setMessage(e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        response = new ResponseEntity<>(body, headers, httpStatus);
        return response;
    }

}
