package fun.example.parcel.controller;

public record CustomResponse<T>(Boolean success, String message, T data) {}
