package com.company.exceptoins;

public class EmptyListException
        extends RuntimeException {
    public EmptyListException(String listName) {
        super("The list \""+ listName +"\" is empty");
    }
}
