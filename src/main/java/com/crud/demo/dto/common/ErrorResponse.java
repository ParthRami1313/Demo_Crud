package com.crud.demo.dto.common;

import java.util.Date;

import lombok.Data;

@Data
public class ErrorResponse <T>{
    private Date timeStamp;
    private T message;
    private int statusCode;
    private String path;
}
