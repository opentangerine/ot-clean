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

import com.jcabi.log.Logger;
import java.io.IOException;
import java.io.InputStream;
import javax.validation.constraints.NotNull;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;

/**
 * Resource loader.
 *
 * @author Grzegorz Gajos (grzegorz.gajos@opentangerine.com)
 * @version $Id$
 * @since 0.10
 */
interface Res {

    /**
     * Load resource under specific path.
     *
     * @param path Resource path.
     * @return Content.
     */
    static String resource(final String path) {
        try {
            final InputStream stream = Res.class.getResourceAsStream(path);
            Validate.isTrue(stream != null, "File not found");
            return IOUtils.toString(stream);
        } catch (final IllegalArgumentException | IOException exc) {
            Logger.error(Res.class, "Unable to read '%s'", path);
            throw new IllegalArgumentException(
                String.format("Unable to read resource: %s", path), exc
            );
        }
    }

}
