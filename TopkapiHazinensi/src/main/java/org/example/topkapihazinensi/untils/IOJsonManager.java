package org.example.topkapihazinensi.untils;

import java.io.File;

public class IOJsonManager {

    private final String filePath;

    public IOJsonManager(String file) {

        this.filePath = file;
        initFile();
    }

    // read file content
    public String getFile() {
        return "";
    }

    // write json file content into file
    public void setFile(String file) {
    }


    // init this to if file not exist build it
    private void initFile()
    {
        if (!new File(filePath).exists())
        {
            new File(filePath).mkdir();
        }
        // create file if not exist
    }
}
