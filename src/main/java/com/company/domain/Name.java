package com.company.domain;

import java.io.IOException;

public interface Name {
    String enterName() throws IOException;
    String enterName(String question, String regex) throws IOException;
    void setName(String name);
    String getName();
}
