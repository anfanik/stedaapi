package me.anfanik.steda.api.access;

import java.util.function.Predicate;

@FunctionalInterface
public interface AccessCheck<T> {

    static <T> AccessCheck<T> simple(Predicate<T> predicate, String errorMessage) {
        return new AccessCheck<T>() {
            @Override
            public boolean checkAccess(T t) {
                return predicate.test(t);
            }

            @Override
            public String getErrorMessage(T t) {
                return errorMessage;
            }
        };
    }

    boolean checkAccess(T t);

    default String getErrorMessage(T t) {
        return "";
    }

    default String getNegativeErrorMessage(T t) {
        return "";
    }

    default AccessCheck<T> negative() {
        return new AccessCheck<T>() {
            @Override
            public boolean checkAccess(T t) {
                return !AccessCheck.this.checkAccess(t);
            }

            @Override
            public String getErrorMessage(T t) {
                return getNegativeErrorMessage(t);
            }
        };
    }

}