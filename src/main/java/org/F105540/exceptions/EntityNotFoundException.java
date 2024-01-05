package org.F105540.exceptions;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String entityType, int entityId) {
        super(String.format("%s with ID %d not found", entityType, entityId));
    }
}
