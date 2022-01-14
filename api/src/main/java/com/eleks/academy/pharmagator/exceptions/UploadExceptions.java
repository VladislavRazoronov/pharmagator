package com.eleks.academy.pharmagator.exceptions;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UploadExceptions extends RuntimeException {

    private final Error error;

    @Getter
    @AllArgsConstructor
    public enum Error {
        INVALID_FILE_FORMAT("Format of file is invalid"),
        SAVE_WAS_NOT_SUCCESSFUL("Something was bad while saving");

        private String message;
    }

}
