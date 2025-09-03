package com.crediya.model.exception;

public class AccessDeniedException extends IllegalArgumentException {
     public AccessDeniedException() {
         super("Access denied: Does not have permission to access this resource");
     }
}
