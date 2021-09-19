package com.bitsindri.bit.methods;

public class Resource<T> {
    private T data;
    private Status status;
    private String message;

    public Resource(T data, Status status, String message) {
        this.data = data;
        this.status = status;
        this.message = message;
    }

    public static <T> Resource loading(T data){
        return new Resource(data,Status.LOADING,null);
    }
    public static <T> Resource success(T data){
        return new Resource(data,Status.SUCCESS,null);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

