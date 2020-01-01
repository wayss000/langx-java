package com.jn.langx.util.collection;

import com.jn.langx.io.resource.Resource;
import com.jn.langx.io.resource.Resources;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.io.IOs;

import java.io.*;
import java.net.URL;
import java.util.Properties;

public class Props {
    private Props() {
    }

    public Properties loadFromFile(File file) throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            return load(inputStream);
        } finally {
            IOs.close(inputStream);
        }
    }

    public Properties loadFromFile(String location) throws IOException {
        return load(Resources.loadFileResource(location));
    }

    public Properties loadFromClasspath(String classpath) throws IOException {
        return load(Resources.loadClassPathResource(classpath));
    }

    public Properties loadFromURL(String url) throws IOException {
        return load(Resources.loadUrlResource(url));
    }

    public Properties loadFromURL(URL url) throws IOException {
        return load(Resources.loadUrlResource(url));
    }

    public Properties loadFromString(String string) throws IOException {
        return load(Resources.asByteArrayResource(string.getBytes()));
    }

    public Properties load(Resource resource) throws IOException {
        Preconditions.checkNotNull(resource);
        InputStream inputStream = null;
        try {
            inputStream = resource.getInputStream();
            return load(inputStream);
        } finally {
            IOs.close(inputStream);
        }
    }

    public Properties load(Reader reader) throws IOException {
        Properties props = new Properties();
        props.load(reader);
        return props;
    }

    public Properties load(InputStream inputStream) throws IOException {
        Properties props = new Properties();
        props.load(inputStream);
        return props;
    }

    public Properties loadFromXML(Resource resource) throws IOException {
        Preconditions.checkNotNull(resource);
        InputStream inputStream = null;
        try {
            inputStream = resource.getInputStream();
            return loadFromXML(inputStream);
        } finally {
            IOs.close(inputStream);
        }
    }

    public Properties loadFromXML(InputStream inputStream) throws IOException {
        Properties props = new Properties();
        props.loadFromXML(inputStream);
        return props;
    }
}
