package Statements;

import Compiler.SemanthicException;

public interface Statement {

    void evaluate() throws SemanthicException;
}
