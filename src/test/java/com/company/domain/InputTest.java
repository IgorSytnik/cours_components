package com.company.domain;

import com.company.domain.Input;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InputTest {
    String name = "bruh ee ee";
    String expected = "123\ny\nn\nbruh ee ee\ny";
    InputStream sysInBackup = System.in; // backup System.in to restore it later
    InputStream in = new ByteArrayInputStream(expected.getBytes());

    @Test
    void inputString_Numbers_Equals() throws IOException {
        System.setIn(in);

        Assertions.assertEquals("123", Input.inputString("STR", "[0-9\\s]+"));

        System.setIn(sysInBackup);
    }

    @Test
    void inputString_Word_Equals() throws IOException {
        System.setIn(in);

        assertEquals(name, Input.inputString("STR", "[a-z\\s]+"));

        System.setIn(sysInBackup);
    }

    @Test
    void inputInt_Numbers_Equals() throws IOException {
        System.setIn(in);

        assertEquals(123, Input.inputInt("NUM", 2, 200));

        System.setIn(sysInBackup);
    }
}