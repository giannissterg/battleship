package gr.ste.domain.models;

public class Result<T> {
}

class Success<T> extends Result<T> {
    private final T data;

    public Success(T data) {
        this.data = data;
    }
}

class Failure<T> extends Result<T> {
    private final String message;

    Failure(String message) {
        this.message = message;
    }
}