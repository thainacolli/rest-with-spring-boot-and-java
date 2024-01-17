package br.com.rest.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnsupportMathOperartionException extends RuntimeException {

    public UnsupportMathOperartionException (String ex) {
        super(ex);
    }
}
