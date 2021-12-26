package com.company.domain.hei;

import com.company.domain.ClassWithName;
import com.company.exceptoins.EmptyListException;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InstitutionTest {
    Institution obj = new Institution() {
        @Override
        public String enterName() throws IOException {
            return super.enterName();
        }

        @Override
        protected boolean showList(List col, String name) {
            return super.showList(col, name);
        }

        @Override
        protected Object getOne(Collection<? extends ClassWithName> col, String name, int i) throws EmptyListException {
            return super.getOne(col, name, i);
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