/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package com.tongdaxing.xchat_framework.http_image.http.form.content;

import com.tongdaxing.xchat_framework.http_image.http.form.MIME;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author zhongyongsheng
 */
public class FileDataBody extends AbstractContentBody {

    private final byte[] data;
    private final String filename;
    private final String charset;

    /**
     * @since 4.1
     */
    public FileDataBody(final byte[] data,
                        final String filename,
                        final String mimeType,
                        final String charset) {
        super(mimeType);
        if (data == null) {
            throw new IllegalArgumentException("File data may not be null");
        }
        this.data = data;
        if (filename != null)
            this.filename = filename;
        else
            this.filename = "";
        this.charset = charset;
    }

    /**
     * @since 4.1
     */
    public FileDataBody(final byte[] data,
                        final String mimeType,
                        final String charset) {
        this(data, null, mimeType, charset);
    }

    public FileDataBody(final byte[] data, final String mimeType) {
        this(data, mimeType, null);
    }

    public FileDataBody(final byte[] data) {
        this(data, "application/octet-stream");
    }

    /**
     * @deprecated use {@link #writeTo(OutputStream)}
     */
    @Deprecated
    public void writeTo(final OutputStream out, int mode) throws IOException {
        writeTo(out);
    }

    public void writeTo(final OutputStream out) throws IOException {
        if (out == null) {
            throw new IllegalArgumentException("Output stream may not be null");
        }
        InputStream in = new ByteArrayInputStream(this.data);
        try {
            byte[] tmp = new byte[4096];
            int l;
            while ((l = in.read(tmp)) != -1) {
                out.write(tmp, 0, l);
            }
            out.flush();
        } finally {
            in.close();
        }
    }

    public String getTransferEncoding() {
        return MIME.ENC_BINARY;
    }

    public String getCharset() {
        return charset;
    }

    public long getContentLength() {
        return this.data.length;
    }

    public String getFilename() {
        return filename;
    }

    public byte[] getData() {
        return data;
    }

}
