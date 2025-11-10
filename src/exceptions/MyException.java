package exceptions;

public class MyException extends Exception {
    public MyException(String message) {
        super(message);  //aici folosim super() pt vrem sa dam apel la constructorul unei superclase
            }
}

//exceptii individuale de facut 