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

import com.google.common.collect.Lists;
import com.jcabi.log.Logger;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.apache.maven.shared.utils.io.DirectoryScanner;

/**
 * Scan directories and return list of files or directories to delete.
 *
 * @author Grzegorz Gajos (grzegorz.gajos@opentangerine.com)
 * @version $Id$
 * @since 0.11
 */
public final class Scan {

    /**
     * Execute scan.
     *
     * @param path Working directory.
     * @param patterns Array of patterns to delete.
     * @return Stream of files.
     */
    public Stream<Path> scan(final Path path, final String[] patterns) {
        Logger.debug(this, "Scanner: %s", path);
        final DirectoryScanner scanner = new DirectoryScanner();
        scanner.setIncludes(patterns);
        Logger.debug(this, "- deletes: %s", Lists.newArrayList(patterns));
        scanner.setBasedir(path.toFile());
        scanner.setCaseSensitive(false);
        scanner.scan();
        return Stream.concat(
            Arrays.stream(scanner.getIncludedDirectories()),
            Arrays.stream(scanner.getIncludedFiles())
        ).map(path::resolve);
    }

    /**
     * Proxy. See {@link Scan#scan(Path, String[])}.
     *
     * @param path Working directory.
     * @param patterns List of patterns to delete.
     * @return Stream of files.
     */
    public Stream<Path> scan(final Path path, final List<String> patterns) {
        return this.scan(path, patterns.toArray(new String[patterns.size()]));
    }

}
