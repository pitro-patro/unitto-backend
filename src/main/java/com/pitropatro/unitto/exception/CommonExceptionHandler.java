package com.pitropatro.unitto.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CommonExceptionHandler {
    // TODO: HttpStatus 상태 처리 뭘로할지 중요한가
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Error Occured"));
    }

    @ExceptionHandler(NotExistingLotteryNumberException.class)
    public ResponseEntity<Object> handleNotExistingLotteryNumberException(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Not Existing Lottery Number"));
    }

    @ExceptionHandler(UniqueNumberMaxTryException.class)
    public ResponseEntity<Object> handleUniqueNumberMaxTryException(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Finding Unique Number Max Try Exceeded"));
    }

    @ExceptionHandler(WrongApproachException.class)
    public ResponseEntity<Object> handleWrongApproachException(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Wrong Approach"));
    }

    @ExceptionHandler(LotteryNumberOptionSizeException.class)
    public ResponseEntity<Object> handleLotteryNumberOptionSizeException(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Lottery Number Option Size Exceeded"));
    }
}