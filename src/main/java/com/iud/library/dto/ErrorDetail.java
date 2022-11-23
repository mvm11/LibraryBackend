package com.iud.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetail {

    private Date errorDate;
    private String errorMessage;
    private String errorDetails;
}
