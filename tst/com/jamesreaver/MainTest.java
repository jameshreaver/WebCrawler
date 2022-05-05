package com.jamesreaver;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class MainTest {

    @Test
    public void main(){
        // given
        String input =
                "invalid\n" +
                "http://klee.doc.ic.ac.uk\n" +
                "\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        // when
        Main.main(new String[]{});

        // then
        assertTrue(output.toString().contains("http://klee.doc.ic.ac.uk/user/login"));
        assertTrue(output.toString().contains("http://klee.doc.ic.ac.uk/user/register"));
    }
}
