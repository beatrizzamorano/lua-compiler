package Compiler;

/**
 * Created by beatriz zamorano on 21/03/17.
 */
public class SemanthicException extends Exception {
    private int errorNumber;

    public SemanthicException(int errorNumber) {
        this.errorNumber = errorNumber;
    }

    public int getErrorNumber() {
        return this.errorNumber;
    }
}
