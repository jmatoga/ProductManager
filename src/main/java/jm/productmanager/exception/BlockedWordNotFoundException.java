package jm.productmanager.exception;


import java.util.UUID;

public class BlockedWordNotFoundException extends RuntimeException {
    public BlockedWordNotFoundException(UUID id) {
        super("Blocked word with ID: " + id + " not found.");
    }
}

