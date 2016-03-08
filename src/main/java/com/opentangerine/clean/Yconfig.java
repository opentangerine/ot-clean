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

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.shared.utils.io.DirectoryScanner;
import org.yaml.snakeyaml.Yaml;

/**
 * Clean configuration file.
 *
 * @author Grzegorz Gajos (grzegorz.gajos@opentangerine.com)
 * @version $Id$
 * @since 0.5
 */
public final class Yconfig {

    /**
     * List of paths to delete.
     */
    private transient List<String> deletes = Collections.emptyList();

    /**
     * Setter. This method is used by Yaml mapper only.
     * @param sdeletes Values.
     */
    public void setDeletes(final List<String> sdeletes) {
        this.deletes = sdeletes;
    }

    /**
     * Files to delete.
     *
     * @param path Working directory.
     * @return Stream of files.
     */
    public Stream<Path> filesToDelete(final Path path) {
        final DirectoryScanner scanner = new DirectoryScanner();
        scanner.setIncludes(
            this.deletes.toArray(new String[this.deletes.size()])
        );
        scanner.setBasedir(path.toFile());
        scanner.setCaseSensitive(false);
        scanner.scan();
        return Stream.concat(
            Arrays.stream(scanner.getIncludedDirectories()),
            Arrays.stream(scanner.getIncludedFiles())
        ).map(it -> path.resolve(it));
    }

    /**
     * Load config from file.
     *
     * @param file File.
     * @return Config Object.
     */
    public static Yconfig load(final File file) {
        try {
            return Optional.ofNullable(
                new Yaml().loadAs(
                    preprocess(file),
                    Yconfig.class
                )
            ).orElse(new Yconfig());
        } catch (final IOException exc) {
            throw new IllegalStateException(
                "Unable to read config file",
                exc
            );
        }
    }

    /**
     * Preprocess input file and append double quotes for all paths in the file.
     *
     * @param file Input file.
     * @return Preprocessed file.
     * @throws IOException On file error.
     */
    private static String preprocess(final File file) throws IOException {
        final String pattern = "- *";
        return new Replace(
            FileUtils.readFileToString(file)
        ).replace(
            line -> line.contains(pattern),
            line -> StringUtils.join(
                StringUtils.replace(line, pattern, "- \"*"),
                "\""
            )
        ).output();
    }

}
