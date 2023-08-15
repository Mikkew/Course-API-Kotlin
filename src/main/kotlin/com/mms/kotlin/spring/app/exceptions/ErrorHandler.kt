package com.mms.kotlin.spring.app.exceptions

import com.mms.kotlin.spring.app.models.error.WrapperError
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.time.LocalDateTime

@ControllerAdvice
class ErrorHandler: ResponseEntityExceptionHandler() {

    override fun handleMethodArgumentNotValid(ex: MethodArgumentNotValidException, headers: HttpHeaders,
        status: HttpStatusCode, request: WebRequest): ResponseEntity<Any>? {
        val errors: List<String> = ex.bindingResult.fieldErrors
            .map<FieldError?, String> { err -> "${err?.field} : ${err?.defaultMessage}" }.toList();
        val response: WrapperError<List<String>?> = WrapperError(
            status = status.value(),
            time = LocalDateTime.now(),
            errors = errors
        );
        return response.createResponse(HttpStatus.valueOf(status.value()))
    }

    @ExceptionHandler(BadRequestException::class)
    fun badRequestException(ex: BadRequestException, request: HttpServletRequest): ResponseEntity<Any>? {
        val response: WrapperError<String?> = WrapperError(
                status = HttpStatus.BAD_REQUEST.value(),
                time = LocalDateTime.now(),
                errors = ex.message
        );
        return response.createResponse(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException::class)
    fun resourceNotFoundException(ex: ResourceNotFoundException, request: HttpServletRequest): ResponseEntity<Any>? {
        val response: WrapperError<String?> = WrapperError(
            status = HttpStatus.NOT_FOUND.value(),
            time = LocalDateTime.now(),
            errors = ex.message
        );
        return response.createResponse(HttpStatus.NOT_FOUND);
    }

    fun globalExceptionHandler(ex: GlobalException, request: HttpServletRequest): ResponseEntity<Any>? {
        val response: WrapperError<String?> = WrapperError(
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            time = LocalDateTime.now(),
            errors = ex.message
        );
        return response.createResponse(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}