package com.pitropatro.unitto.exception;

import com.pitropatro.unitto.exception.lottery.LotteryNumberOptionSizeException;
import com.pitropatro.unitto.exception.lottery.NotExistingLotteryNumberException;
import com.pitropatro.unitto.exception.lottery.SaveConfirmedUniqueNumberFailedException;
import com.pitropatro.unitto.exception.lottery.UniqueNumberMaxTryException;
import com.pitropatro.unitto.exception.token.EmptyTokenException;
import com.pitropatro.unitto.exception.token.ExpiredTokenException;
import com.pitropatro.unitto.exception.token.InvalidTokenException;
import com.pitropatro.unitto.exception.user.UserEmailNullException;

import com.pitropatro.unitto.exception.user.UserSignUpFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CommonExceptionHandler {
    // TODO: HttpStatus 상태 처리 뭘로할지 중요한가
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(Exception e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.toString()));
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

    //User
    @ExceptionHandler(UserEmailNullException.class)
    public ResponseEntity<Object> handleUserEmailNullException(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("User Email Value Is NULL"));
    }

    @ExceptionHandler(UserSignUpFailedException.class)
    public ResponseEntity<Object> handleUserSignUpFailedException(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("User SignUp Failed"));
    }

    //ConfirmedUniqueNumberRepository
    @ExceptionHandler(SaveConfirmedUniqueNumberFailedException.class)
    public ResponseEntity<Object> handleSaveConfirmedUniqueNumberFailedException(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Saving Confirmed Unique Number to MySQL Failed"));
    }

    //Token
    @ExceptionHandler(EmptyTokenException.class)
    public ResponseEntity<Object> emptyTokenException(){
        //return new ResponseEntity<Object>("Token Null", HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Token is NULL"));
    }

    @ExceptionHandler(ExpiredTokenException.class)
    public ResponseEntity<Object> expiredTokenException(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Token is Expired"));
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<Object> invalidTokenException(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Token is Invalid"));
    }
}
