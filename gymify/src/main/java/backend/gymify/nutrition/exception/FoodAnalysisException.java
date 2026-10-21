package backend.gymify.nutrition.exception;

import backend.gymify.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class FoodAnalysisException extends ApiException {
    public FoodAnalysisException(String message) {
        super(message, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
