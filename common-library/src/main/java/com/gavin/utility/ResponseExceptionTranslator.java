package com.gavin.utility;

import com.gavin.exception.CustomException;
import com.gavin.dto.common.CustomResponseBody;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseExceptionTranslator {

    public static ResponseEntity<CustomResponseBody> translate(Exception e) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Cache-Control", "no-store");
        headers.set("Pragma", "no-cache");

        CustomResponseBody body = new CustomResponseBody();

        ResponseEntity<CustomResponseBody> response;
        HttpStatus httpStatus;
        if (e instanceof CustomException) {
            CustomException ce = (CustomException) e;
            body.setResultCode(ce.getErrorCode());
            body.setErrorMessage(ce.getMessage());
            httpStatus = HttpStatus.valueOf(ce.getHttpCode());
        } else {
            body.setResultCode("server_error");
            body.setErrorMessage(e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        response = new ResponseEntity<>(body, headers, httpStatus);
        return response;
    }

}
