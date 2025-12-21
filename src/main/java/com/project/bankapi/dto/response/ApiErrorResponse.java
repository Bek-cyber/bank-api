package com.project.bankapi.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;


@JsonPropertyOrder({
        "timestamp",
        "status",
        "error",
        "message",
        "path"
})
@Getter
@Builder
public class ApiErrorResponse {
    private OffsetDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}
