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
     * Cleaning summary.
     */
    private final transient Summary summary;

    /**
     * Delete handler.
     */
    private final transient Delete delete;

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
     * initialization of cleanables
     *
     * @param mode Mode.
     */
    public Clean(final Mode mode) {
        this.summary = new Summary(mode);
        this.delete = new Delete(mode, this.summary);
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
        Wipe.DEFAULT.forEach(
            it -> {
                it.clean(this.delete, path);
                this.jump(path);
            }
        );
        final File[] files = path.toFile().listFiles(File::isDirectory);
        if (files != null) {
            Arrays
                .stream(files)
                .forEach(it -> this.recurrence(it.toPath()));
        }
    }

    /**
     * Check if configuration of clean defines dirs section and crawler
     * should jump to different directory.
     * @param path Working directory.
     */
    private void jump(final Path path) {
        Yconfig
            .load(path.resolve(".clean.yml").toFile())
            .dirs().forEach(
                (it) -> {
                    final Path target = path.resolve(it);
                    Logger.debug(this, "Jumping to %s", target);
                    this.recurrence(target);
                }
            );
    }

}
