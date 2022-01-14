package com.eleks.academy.pharmagator.dataproviders.dto.aptslav;

import lombok.Getter;

@Getter
public class ResponseBodyIsNullException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Response body is null";
    }

}
