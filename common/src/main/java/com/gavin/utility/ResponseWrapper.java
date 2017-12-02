package com.gavin.utility;

import com.gavin.constants.ResponseCodeConstants;
import com.gavin.dto.common.CustomResponseBody;
import com.gavin.dto.common.PageResult;

public class ResponseWrapper {

    public static Object wrap(Object body) {
        if (body == null) {
            // when return type of controller method is void
            CustomResponseBody<Object> responseBody = new CustomResponseBody<>();
            responseBody.setCode(ResponseCodeConstants.OK);
            return responseBody;
        } else if (body instanceof CustomResponseBody) {
            // already processed by ExceptionHandler
            if (!((CustomResponseBody) body).getCode().equalsIgnoreCase(ResponseCodeConstants.OK)) {
                return body;
            }
        } else if (body instanceof PageResult) {
            PageResult pageResult = (PageResult) body;
            CustomResponseBody<Object> responseBody = new CustomResponseBody<>();
            responseBody.setCode(ResponseCodeConstants.OK);
            responseBody.setPageResult(pageResult);

            return responseBody;
        } else {
            CustomResponseBody<Object> responseBody = new CustomResponseBody<>();
            responseBody.setCode(ResponseCodeConstants.OK);
            responseBody.setRecord(body);
            return responseBody;
        }

        return body;
    }

}
