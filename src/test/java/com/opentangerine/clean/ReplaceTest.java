/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 opentangerine.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.opentangerine.clean;

import java.util.function.Function;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Tests for {link Replace}.
 *
 * @author Grzegorz Gajos (grzegorz.gajos@opentangerine.com)
 * @version $Id$
 * @since 0.5
 */
public final class ReplaceTest {

    /**
     * Check replace when input matches.
     */
    @Test
    public void matching() {
        MatcherAssert.assertThat(
            new Replace("- input")
                .replace(
                    line -> line.contains("inp"),
                    bracketing()
                ).output(),
            Matchers.equalToIgnoringWhiteSpace("[- input]")
        );
    }

    /**
     * Check pattern replace when input matches.
     */
    @Test
    public void matchingSimplePattern() {
        MatcherAssert.assertThat(
            new Replace("+input")
                .replace(
                    "put",
                    "--"
                ).output(),
            Matchers.equalToIgnoringWhiteSpace("+in--")
        );
    }

    /**
     * Check replace when input does not match.
     */
    @Test
    public void skipit() {
        final String given = "- ZZZut";
        MatcherAssert.assertThat(
            new Replace(given)
                .replace(
                    line -> line.contains("check"),
                    bracketing()
                ).output(),
            Matchers.equalToIgnoringWhiteSpace(given)
        );
    }

    /**
     * Check replace when only single line match (Windows style).
     */
    @Test
    public void replaceWindows() {
        final String given = "- line1\r\n- line2";
        MatcherAssert.assertThat(
            new Replace(given)
                .replace(
                    line -> line.contains("line2"),
                    bracketing()
                ).output(),
            Matchers.equalToIgnoringWhiteSpace("- line1 [- line2]")
        );
    }

    /**
     * Check replace when only single line match (Linux style).
     */
    @Test
    public void replaceLinux() {
        final String given = "- line0\n- line7";
        MatcherAssert.assertThat(
            new Replace(given)
                .replace(
                    line -> line.contains("line7"),
                    bracketing()
                ).output(),
            Matchers.equalToIgnoringWhiteSpace("- line0 [- line7]")
        );
    }

    /**
     * Simple transformation that is adding brackets.
     *
     * @return Bracketing closure.
     */
    private static Function<String, String> bracketing() {
        return line -> StringUtils.join("[", line, "]");
    }

}
