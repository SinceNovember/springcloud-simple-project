package com.simple.admin.auth.handler;

import com.simple.admin.core.entity.Response;
import com.simple.admin.core.entity.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.UnsupportedGrantTypeException;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;

public class CustomWebResponseExceptionTranslator implements WebResponseExceptionTranslator {

    public static final Logger log = LoggerFactory.getLogger(CustomWebResponseExceptionTranslator.class);

    @Override
    public ResponseEntity<ResponseData> translate(Exception e) throws Exception {
        log.error("��֤�������쳣",e);

        ResponseData response = resolveException(e);

        return new ResponseEntity<ResponseData>(response, HttpStatus.valueOf(response.getCode()));
    }

    /**
     * ���������쳣
     * @param e exception
     * @return
     */
    private ResponseData resolveException(Exception e) {
        // ��ʼֵ 500
        ReturnCode returnCode = ReturnCode.RC500;
        int httpStatus = HttpStatus.UNAUTHORIZED.value();
        //��֧�ֵ���֤��ʽ
        if(e instanceof UnsupportedGrantTypeException){
            returnCode = ReturnCode.UNSUPPORTED_GRANT_TYPE;
            //�û����������쳣
        }else if(e instanceof InvalidGrantException){
            returnCode = ReturnCode.USERNAME_OR_PASSWORD_ERROR;
        }
        ResponseData ailResponse = Response.fail(HttpStatus.UNAUTHORIZED, returnCode.getMsg());

        return ailResponse;
    }
}
