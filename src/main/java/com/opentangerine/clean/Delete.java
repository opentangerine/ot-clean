package com.opentangerine.clean;

import com.jcabi.log.Logger;
import org.apache.commons.io.FileSystemUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.nio.file.Path;

/**
 * Created by gagre on 27.10.2015.
 */
public final class Delete {
    private Mode mode;

    public Delete(Mode mode) {
        this.mode = mode;
    }

    public void directory(Path path) {
        if(mode.readonly()) {
            Logger.info(this, "Directory '%s' can be deleted.", path);
        } else {
            Logger.info(this, "Deleting '%s'", path);
            FileUtils.deleteQuietly(path.toFile());
        }
    }
}
