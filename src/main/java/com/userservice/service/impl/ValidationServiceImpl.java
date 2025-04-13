package com.userservice.service.impl;

import com.userservice.constant.ResponseConstant;
import com.userservice.dto.CommonException;
import com.userservice.service.ValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import java.util.regex.Pattern;

@Service
public class ValidationServiceImpl implements ValidationService {
    private static final Logger log = LoggerFactory.getLogger(ValidationServiceImpl.class);

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    @Autowired
    private MessageSource messageSource;

    @Override
    public void emailIdVerification(String emailId, String refId) {
        if(null == emailId || emailId.trim().isEmpty()){
            log.error("Provided email is blank for refId {}", refId);
            throw CommonException.builder().code(ResponseConstant.BLANK_EMAIL)
                    .exception(messageSource.getMessage(ResponseConstant.BLANK_EMAIL, null, null))
                    .refId(refId)
                    .build();
        }

        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        if(!pattern.matcher(emailId).matches()){
            log.error("Provided email is invalid for refId {}", refId);
            throw CommonException.builder().code(ResponseConstant.INVALID_EMAIL)
                    .exception(messageSource.getMessage(ResponseConstant.INVALID_EMAIL, null, null))
                    .refId(refId)
                    .build();
        }
    }
}
