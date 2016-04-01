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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * This is initial class, should be changed to something else.
 *
 * @author Grzegorz Gajos (grzegorz.gajos@opentangerine.com)
 * @version $Id$
 * @since 0.5
 */
public final class Clean {
    /**
     * Cleaners.
     */
    private final transient Iterable<Cleanable> cleaners;

    /**
     * Cleaning summary.
     */
    private final transient Summary summary;

    /**
     * Clean application.
     *
     * @param cargs Execution arguments.
     */
    public Clean(final String... cargs) {
        this(new Mode(cargs));
    }

    /**
     * Ctor.
     *
     * @param mode Mode.
     */
    public Clean(final Mode mode) {
        this.summary = new Summary(mode);
        this.cleaners = Arrays.asList(
            new Cleanable.Maven(new Delete(mode, this.summary)),
            new Cleanable.Yclean(new Delete(mode, this.summary))
        );
    }

    /**
     * Entry point of application.
     *
     * @param args Application arguments.
     */
    public static void main(final String... args) {
        new Clean(args).clean(Paths.get(System.getProperty("user.dir")));
    }

    /**
     * Start cleanup on specific path.
     *
     * @param path Working directory.
     */
    public void clean(final Path path) {
        this.recurrence(path);
        this.summary.finished();
    }

    /**
     * Execute cleaning for current and nested directories.
     *
     * @param path Current path.
     */
    private void recurrence(final Path path) {
        this.cleaners.forEach(
            it -> {
                if (it.match(path)) {
                    it.display(path, new Console());
                    it.clean(path);
                }
                this.jump(path);
            }
        );
        Arrays
            .stream(path.toFile().listFiles(File::isDirectory))
            .forEach(it -> this.recurrence(it.toPath()));
    }

    /**
     * Check if configuration of clean defines dirs section and crawler
     * should jump to different directory.
     * @param path Working directory.
     */
    private void jump(final Path path) {
        Yconfig
            // FIXME GG: in progress, let's add new method in Yconfig to scal for this specific file
            .load(path.resolve(".clean.yml").toFile())
            .dirs().forEach(this::recurrence);
    }

}
