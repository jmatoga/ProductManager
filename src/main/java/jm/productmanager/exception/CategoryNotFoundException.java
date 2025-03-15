package jm.productmanager.exception;


import java.util.UUID;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(UUID id) {
        super("Category with ID: " + id + " not found.");
    }
}

