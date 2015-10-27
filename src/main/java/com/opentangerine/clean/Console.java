package com.opentangerine.clean;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * Created by gagre on 27.10.2015.
 */
public class Console {
    private PrintWriter writer = new PrintWriter(
            new OutputStreamWriter(System.out, StandardCharsets.UTF_8));

    public Console() {
    }

    public void help() {
        final String path = "/ot-clean/readme.txt";
        try {
            writer.append(IOUtils.toString(getClass().getResourceAsStream(path)));
            writer.println();
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to file readme file");
        }
    }
}
