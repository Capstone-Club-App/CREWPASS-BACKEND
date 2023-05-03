package Capstone.Crewpass.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ResponseFormat<T> {
    private Integer statusCode;
    private String responseMessage;
    private T data;

    public ResponseFormat(final int statusCode, final String responseMessage){
        this.statusCode = statusCode;
        this.responseMessage = responseMessage;
        this.data = null;
    }

    public static<T> ResponseFormat<T> responseFormat(final int statusCode, final String responseMessage){
        return responseFormat(statusCode, responseMessage, null);
    }

    public static<T> ResponseFormat<T> responseFormat(final int statusCode, final String responseMessage, final  T t){
        return ResponseFormat.<T>builder()
                .data(t)
                .statusCode(statusCode)
                .responseMessage(responseMessage)
                .build();
    }

}