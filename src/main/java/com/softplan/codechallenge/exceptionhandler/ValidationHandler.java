package com.softplan.codechallenge.exceptionhandler;

import com.softplan.codechallenge.domain.exception.CpfJaExistenteException;
import com.softplan.codechallenge.domain.exception.PessoaNaoEncontradaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ValidationHandler extends ResponseEntityExceptionHandler {

	@Autowired
	private MessageSource messageSource;

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
																  HttpHeaders headers, HttpStatus status, WebRequest request) {

		Validation validation = newValidation("Um ou mais campos estão incorretos. Corrija e tente novamente", status);

		List<Validation.Campo> campos = ex.getBindingResult().getAllErrors()
				.stream()
				.map(e -> new Validation.Campo(((FieldError) e).getField(),
						messageSource.getMessage(e, LocaleContextHolder.getLocale())))
				.collect(Collectors.toList());

		validation.setCampos(campos);

		return super.handleExceptionInternal(ex, validation, headers, status, request);
	}

	@ExceptionHandler(CpfJaExistenteException.class)
	public ResponseEntity<Object> cpfExistenteHandler(CpfJaExistenteException ex, WebRequest webRequest) {

		var status = HttpStatus.BAD_REQUEST;

		Validation validation = newValidation(ex.getMessage(), status);
		return super.handleExceptionInternal(ex, validation, new HttpHeaders(), status, webRequest);
	}

	@ExceptionHandler(PessoaNaoEncontradaException.class)
	public ResponseEntity<Object> pessoaNaoEncontradaHandler(PessoaNaoEncontradaException ex, WebRequest webRequest) {

		var status = HttpStatus.NOT_FOUND;

		Validation validation = newValidation(ex.getMessage(), status);
		return super.handleExceptionInternal(ex, validation, new HttpHeaders(), status, webRequest);
	}

	private Validation newValidation(String titulo, HttpStatus status) {
		Validation validation = new Validation();
		validation.setStatus(status.value());
		validation.setDataHora(OffsetDateTime.now());
		validation.setTitulo(titulo);

		return validation;
	}
}
