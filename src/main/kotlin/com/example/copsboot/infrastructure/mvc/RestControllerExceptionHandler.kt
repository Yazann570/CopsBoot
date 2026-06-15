package com.example.copsboot.infrastructure.mvc

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.ui.Model
import org.springframework.validation.BindException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.multipart.MultipartException

@ControllerAdvice
class RestControllerExceptionHandler {

    // This handles validation errors for JSON request bodies.
    // It converts Spring's validation exception into a clear JSON response.
    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handle(exception: MethodArgumentNotValidException): Map<String, List<FieldErrorResponse>> {
        return error(
            exception.bindingResult.fieldErrors.map { fieldError ->
                FieldErrorResponse(
                    fieldName = fieldError.field,
                    errorMessage = fieldError.defaultMessage ?: "Invalid value"
                )
            }
        )
    }

    // This handles validation errors for form or multipart requests.
    // Multipart requests do not always fail through MethodArgumentNotValidException,
    // so this keeps the response format consistent.
    @ExceptionHandler(BindException::class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handle(exception: BindException): Map<String, List<FieldErrorResponse>> {
        return error(
            exception.bindingResult.fieldErrors.map { fieldError ->
                FieldErrorResponse(
                    fieldName = fieldError.field,
                    errorMessage = fieldError.defaultMessage ?: "Invalid value"
                )
            }
        )
    }

    // This handles file upload errors such as a file being larger than the allowed size.
    // The book returns 400 Bad Request instead of letting Spring return 500 Internal Server Error.
    @ExceptionHandler(MultipartException::class)
    fun handleMultipartException(
        exception: MultipartException,
        model: Model
    ): ResponseEntity<String> {
        model.addAttribute("exception", exception)

        return ResponseEntity
            .badRequest()
            .body(exception.message ?: "Invalid multipart request")
    }

    // This wraps the list of field errors in an "errors" JSON property.
    private fun error(errors: List<FieldErrorResponse>): Map<String, List<FieldErrorResponse>> {
        return mapOf("errors" to errors)
    }
}