package com.jn.langx.util.io;


import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Preconditions;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.util.IdentityHashMap;
import java.util.Map;


public final class Charsets {
    private static final ThreadLocal<Map<Charset, CharsetEncoder>> encoderCache = new ThreadLocal<Map<Charset, CharsetEncoder>>() {
        @Override
        protected Map<Charset, CharsetEncoder> initialValue() {
            return new IdentityHashMap<Charset, CharsetEncoder>();
        }
    };
    private static final ThreadLocal<Map<Charset, CharsetDecoder>> decoderCache = new ThreadLocal<Map<Charset, CharsetDecoder>>() {
        @Override
        protected Map<Charset, CharsetDecoder> initialValue() {
            return new IdentityHashMap<Charset, CharsetDecoder>();
        }
    };

    private Charsets() {
    }

    public static final Charset GBK = Charset.forName("GBK");
    public static final Charset GB2312 = Charset.forName("GB2312");

    public static final Charset UTF_8 = Charset.forName("UTF-8");
    public static final Charset UTF_16BE = Charset.forName("UTF-16BE");
    public static final Charset UTF_16LE = Charset.forName("UTF-16LE");
    public static final Charset UTF_16 = Charset.forName("UTF-16");

    public static final Charset US_ASCII = Charset.forName("US-ASCII");
    public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");

    public static Charset getCharset(@Nullable final Charset charset) {
        return charset == null ? Charset.defaultCharset() : charset;
    }

    public static Charset getCharset(@Nullable String name) {
        return name == null ? Charset.defaultCharset() : Charset.forName(name);
    }

    public static Charset getCharset(@Nullable String name, @Nullable String defaultName) {
        return name == null ? (defaultName == null ? Charset.defaultCharset() : Charset.forName(defaultName)) : Charset.forName(name);
    }


    /**
     * Returns a new {@link CharsetEncoder} for the {@link Charset} with specified error actions.
     *
     * @param charset                   The specified charset
     * @param malformedInputAction      The encoder's action for malformed-input errors
     * @param unmappableCharacterAction The encoder's action for unmappable-character errors
     * @return The encoder for the specified {@code charset}
     */
    public static CharsetEncoder encoder(@NonNull Charset charset, @NonNull CodingErrorAction malformedInputAction,
                                         @NonNull CodingErrorAction unmappableCharacterAction) {
        Preconditions.checkNotNull(charset);
        CharsetEncoder e = charset.newEncoder();
        e.onMalformedInput(malformedInputAction).onUnmappableCharacter(unmappableCharacterAction);
        return e;
    }

    /**
     * Returns a new {@link CharsetEncoder} for the {@link Charset} with the specified error action.
     *
     * @param charset           The specified charset
     * @param codingErrorAction The encoder's action for malformed-input and unmappable-character errors
     * @return The encoder for the specified {@code charset}
     */
    public static CharsetEncoder encoder(@NonNull Charset charset, @NonNull CodingErrorAction codingErrorAction) {
        return encoder(charset, codingErrorAction, codingErrorAction);
    }

    /**
     * Returns a cached thread-local {@link CharsetEncoder} for the specified {@link Charset}.
     *
     * @param charset The specified charset
     * @return The encoder for the specified {@code charset}
     */
    public static CharsetEncoder encoder(@NonNull Charset charset) {
        Preconditions.checkNotNull(charset, "charset");

        Map<Charset, CharsetEncoder> map = encoderCache.get();
        CharsetEncoder e = map.get(charset);
        if (e != null) {
            e.reset().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
            return e;
        }

        e = encoder(charset, CodingErrorAction.REPLACE, CodingErrorAction.REPLACE);
        map.put(charset, e);
        return e;
    }


    /**
     * Returns a new {@link CharsetDecoder} for the {@link Charset} with specified error actions.
     *
     * @param charset                   The specified charset
     * @param malformedInputAction      The decoder's action for malformed-input errors
     * @param unmappableCharacterAction The decoder's action for unmappable-character errors
     * @return The decoder for the specified {@code charset}
     */
    public static CharsetDecoder decoder(@NonNull Charset charset, @NonNull CodingErrorAction malformedInputAction,
                                         @NonNull CodingErrorAction unmappableCharacterAction) {
        Preconditions.checkNotNull(charset, "charset");
        CharsetDecoder d = charset.newDecoder();
        d.onMalformedInput(malformedInputAction).onUnmappableCharacter(unmappableCharacterAction);
        return d;
    }

    /**
     * Returns a new {@link CharsetDecoder} for the {@link Charset} with the specified error action.
     *
     * @param charset           The specified charset
     * @param codingErrorAction The decoder's action for malformed-input and unmappable-character errors
     * @return The decoder for the specified {@code charset}
     */
    public static CharsetDecoder decoder(@NonNull Charset charset, @NonNull CodingErrorAction codingErrorAction) {
        return decoder(charset, codingErrorAction, codingErrorAction);
    }

    /**
     * Returns a cached thread-local {@link CharsetDecoder} for the specified {@link Charset}.
     *
     * @param charset The specified charset
     * @return The decoder for the specified {@code charset}
     */
    public static CharsetDecoder decoder(@NonNull Charset charset) {
        Preconditions.checkNotNull(charset, "charset");

        Map<Charset, CharsetDecoder> map = decoderCache.get();
        CharsetDecoder d = map.get(charset);
        if (d != null) {
            d.reset().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
            return d;
        }

        d = decoder(charset, CodingErrorAction.REPLACE, CodingErrorAction.REPLACE);
        map.put(charset, d);
        return d;
    }
}