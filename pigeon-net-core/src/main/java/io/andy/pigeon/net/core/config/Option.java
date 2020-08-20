package io.andy.pigeon.net.core.config;


import java.util.Objects;

/**
 */
public class Option<T> {
    private final String name;
    private T defaultValue;
    private Class<? extends Option > type;

    protected Option(String name, T defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.type = getClass();
    }

    public String name() {
        return this.name;
    }

    public Class<? extends Option > type() {
        return this.type;
    }

    protected void setType(Class<? extends Option > type) {
        this.type = type;
    }

    public T defaultValue() {
        return this.defaultValue;
    }

    public static <T> Option<T> valueOf(String name) {
        return new Option<T>(name, null);
    }

    public static <T> Option<T> valueOf(String name, T defaultValue) {
        return new Option<T>(name, defaultValue);
    }

    public static <T> Option<T> valueOf(Class<? extends Option > type, String name) {
        Option<T> option = new Option<>(name, null);
        option.setType(type);
        return option;
    }

    public static <T> Option<T> valueOf(Class<? extends Option > type, String name, T defaultValue) {
        Option<T> option = new Option<>(name, defaultValue);
        option.setType(type);
        return option;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Option<?> that = (Option<?>) o;
        return name == null ? Objects.equals(name, that.name) : name == null;
    }

    @Override
    public int hashCode() {
        return name == null ? 0 : name.hashCode();
    }
}
