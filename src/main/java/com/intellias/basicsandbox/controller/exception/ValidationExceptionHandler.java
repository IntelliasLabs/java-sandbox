package com.intellias.basicsandbox.controller.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.intellias.basicsandbox.controller.dto.ErrorDTO;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ValidationExceptionHandler {

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorDTO methodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        String langHeader = request.getHeader("Accept-Language");
        return processFieldErrors(fieldErrors, langHeader);
    }

    private ErrorDTO processFieldErrors(List<FieldError> fieldErrors, String language) {
        StringBuilder validationErrorMessage = new StringBuilder();
        Locale locale = extractLocaleFromHeader(language);
        ResourceBundle localizedResource = ResourceBundle.getBundle("resourcebundle.messages", locale);
        for (org.springframework.validation.FieldError fieldError: fieldErrors) {
            if (fieldError.getDefaultMessage() != null) {
                validationErrorMessage.append("[")
                        .append(fieldError.getField())
                        .append("] ")
                        .append(applyCustomTranslation(localizedResource, fieldError))
                        .append(". ");
            }
        }
        return new ErrorDTO(BAD_REQUEST, validationErrorMessage.toString());
    }

    private String applyCustomTranslation(ResourceBundle localizedResource, FieldError fieldError) {
        String message = fieldError.getDefaultMessage();
        if (message != null && message.startsWith("{") && message.endsWith("}") ) {
            String translationKey = message.substring(1, message.length() - 1);
            if (localizedResource.containsKey(translationKey)){
                return localizedResource.getString(translationKey);
            }
        }
        return message;
    }

    private Locale extractLocaleFromHeader(String acceptLanguageHeader) {
        if (acceptLanguageHeader != null && !acceptLanguageHeader.isEmpty()) {
            String[] languages = acceptLanguageHeader.split(",");
            for (String language : languages) {
                String[] parts = language.trim().split(";q=");
                if (parts.length > 0) {
                    Locale locale = Locale.forLanguageTag(parts[0]);
                    if (locale != null) {
                        return locale;
                    }
                }
            }
        }
        // Return the default locale if no valid language is found in the header
        return Locale.getDefault();
    }

}
