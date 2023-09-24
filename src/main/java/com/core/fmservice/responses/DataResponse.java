package com.core.fmservice.responses;

import com.core.fmservice.constants.ModConstant;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@Data
public class DataResponse {
    private String message;
    private Object data;
    private  ModConstant.StatusCode statusCode;

    public DataResponse(String message,Object data,ModConstant.StatusCode statusCode){
        this.data=data;
        this.message=message;
        this.statusCode=statusCode;
    }
}
