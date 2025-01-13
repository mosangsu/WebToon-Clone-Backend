package com.lezhin.clone.backend.exception;

public class PaymentException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    final Object value;

    public PaymentException(String message, Object value) {
        super(message);
        this.value = value;
    }

    public Object getValue() {
        return value;
    }
}