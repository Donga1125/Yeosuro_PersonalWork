package greenjangtanji.yeosuro.global.exception;


import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class ErrorResponse {
    private int status;
    private String message;


    public ErrorResponse(ExceptionCode code){
        this.status = code.getStatus();
        this.message = code.getMessage();
    }

}