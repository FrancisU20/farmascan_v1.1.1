package com.farma.appfarmaenlace.utils;

public class Resource<T> {
    private Status status;
    private T data;
    private String message;

    public Resource(Status status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static <T> Resource success(T data){
        return new Resource<>(Status.SUCCESS,data,null);
    }

    public static <T> Resource error(String msg, T data){
        return new Resource<>(Status.ERROR,data,msg);
    }

    public static <T> Resource loading(T data){
        return  new Resource<>(Status.LOADING,data,null);
    }
}
