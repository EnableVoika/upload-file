package com.voika.uploadfile.infrastructure;

import com.voika.uploadfile.infrastructure.exception.BusinessException;
import com.voika.uploadfile.infrastructure.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
@Slf4j
public class ControllerExceltionHandler {

    @ExceptionHandler(Exception.class)
    public JsonData exception(Exception e, HttpServletRequest request) {
        log.error("",e);
        return JsonData.error("系统异常,请联系管理员");
    }

    @ExceptionHandler(BusinessException.class)
    public JsonData businessException(BusinessException e) {
        if (StringUtil.isNotEmpty(e.getMessage())) {
            return JsonData.error(e.getMessage());
        }
        return JsonData.error();
    }

}
