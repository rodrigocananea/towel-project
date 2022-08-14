package com.towel.el;

public class NotResolvableFieldException extends RuntimeException {
    private String fieldName;
    private Class<?> owner;

    public NotResolvableFieldException(String name, Class<?> owner2) {
        this.fieldName = name;
        this.owner = owner2;
    }

    public String getMessage() {
        return "Field '" + this.fieldName + "' can't be resolved for class: " + this.owner.getCanonicalName() + ".";
    }

    public static NotResolvableFieldException create(Throwable stack, String name, Class<?> clazz) {
        NotResolvableFieldException ex = new NotResolvableFieldException(name, clazz);
        if (stack != null) {
            ex.setStackTrace(stack.getStackTrace());
        }
        return ex;
    }
}
