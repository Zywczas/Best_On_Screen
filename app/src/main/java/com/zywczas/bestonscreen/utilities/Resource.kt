package com.zywczas.bestonscreen.utilities

data class Resource<out T>(val status: Status, val data: T?, val message: String?) {

    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): Resource<T> {
            return Resource(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }
    }


}

//public class Resource<T> (val status: Status, val data: T?, val message: String) {
//
//     fun success(data : T, message : String) : Resource<T> {
//        return new Resource<>(Status.SUCCESS, data, message);
//    }
//
//    public static <T> Resource<T> error( @Nullable T data, @NonNull String msg) {
//        return new Resource<>(Status.ERROR, data, msg);
//    }
//
//    public static <T> Resource<T> loading(@Nullable T data) {
//        return new Resource<>(Status.LOADING, data, null);
//    }
//
//    public enum Status { SUCCESS, ERROR, LOADING}
//
