package com.jn.langx.io.resource;

import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Preconditions;

import java.io.IOException;
import java.io.InputStream;

public class InputStreamResource extends AbstractResource<InputStream> {

    private final InputStream inputStream;

    private final String description;

    private boolean read = false;

    @Override
    public InputStream getRealResource() {
        return inputStream;
    }

    /**
     * Create a new InputStreamResource.
     *
     * @param inputStream the InputStream to use
     */
    public InputStreamResource(InputStream inputStream) {
        this(inputStream, "resource loaded through InputStream");
    }

    /**
     * Create a new InputStreamResource.
     *
     * @param inputStream the InputStream to use
     * @param description where the InputStream comes from
     */
    public InputStreamResource(InputStream inputStream, @Nullable String description) {
        Preconditions.checkNotNull(inputStream, "InputStream must not be null");
        this.inputStream = inputStream;
        this.description = (description != null ? description : "");
    }

    /**
     * This implementation always returns {@code true}.
     */
    @Override
    public boolean exists() {
        return true;
    }


    @Override
    public long contentLength() {
        return -1;
    }

    /**
     * This implementation throws IllegalStateException if attempting to
     * read the underlying stream multiple times.
     */
    @Override
    public InputStream getInputStream() throws IOException, IllegalStateException {
        if (this.read) {
            throw new IllegalStateException("InputStream has already been read - " +
                    "do not use InputStreamResource if a stream needs to be read multiple times");
        }
        this.read = true;
        return this.inputStream;
    }

    /**
     * This implementation returns a description that includes the passed-in
     * description, if any.
     */
    @Override
    public String toString() {
        return "InputStream resource [" + this.description + "]";
    }


    /**
     * This implementation compares the underlying InputStream.
     */
    @Override
    public boolean equals(Object other) {
        return (this == other || (other instanceof InputStreamResource &&
                ((InputStreamResource) other).inputStream.equals(this.inputStream)));
    }

    /**
     * This implementation returns the hash code of the underlying InputStream.
     */
    @Override
    public int hashCode() {
        return this.inputStream.hashCode();
    }

}
