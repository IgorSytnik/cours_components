package com.company.domain;

import com.company.domain.ClassWithName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClassWithNameTest {

    private final ClassWithName obj = new ClassWithName() {
        @Override
        public String enterName(String question, String regex) throws IOException {
            return super.enterName(question, regex);
        }

        @Override
        public String enterName() throws IOException {
            return super.enterName();
        }

        @Override
        public void setName(String name) {
            super.setName(name);
        }

        @Override
        public String getName() {
            return super.getName();
        }
    };

    @Test
    void enterName_GotTheProperString_Equals() throws IOException {
        String name = "My string";
        String expected = "My@@ string\n" +
                        "My string.\n" +
                        "My string\n" +
                        "My string\n" +
                        "n\n" +
                        "My string\n" +
                        "y\n";
        InputStream sysInBackup = System.in; // backup System.in to restore it later
        ByteArrayInputStream in = new ByteArrayInputStream(expected.getBytes());
        System.setIn(in);

        assertEquals(name, obj.enterName());
        System.setIn(sysInBackup);
    }
}