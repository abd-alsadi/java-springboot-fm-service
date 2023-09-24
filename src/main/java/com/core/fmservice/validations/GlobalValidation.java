package com.core.fmservice.validations;

import lombok.*;
import java.util.*;

import com.core.fmservice.constants.ModConstant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class GlobalValidation {
    private String message;
    private HashMap<String,String> errors;
    private  ModConstant.StatusCode statusCode;
}
