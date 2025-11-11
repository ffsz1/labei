package com.tongdaxing.xchat_framework.http_image.http.form;


import com.tongdaxing.xchat_framework.http_image.http.HttpLog;
import com.tongdaxing.xchat_framework.http_image.http.MultipartPostRequest;
import com.tongdaxing.xchat_framework.http_image.http.form.content.AbstractContentBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This class can help upload file with progress notification
 *
 * @author <a href="mailto:itvincent@gmail.com">Vincent</a>
 *         <p/>
 *         Build at 2013-9-5
 */
public class ProgressFileBody extends AbstractContentBody {

    private static final long DEFAULT_PROGRESS_PERCENT = 100;
    private static final int DEFAULT_BUFFER_SIZE = 4096;

    private File file;
    private String filename;
    private String charset;

    private long progressPercent = DEFAULT_PROGRESS_PERCENT;
    private MultipartPostRequest request;
    private long progressStep = 0;

    public ProgressFileBody(final File file, final String filename,
                            final String mimeType, final String charset, MultipartPostRequest request) {
        super(mimeType);
        if (file == null) {
            throw new IllegalArgumentException("File may not be null");
        }
        this.file = file;
        if (filename != null) {
            this.filename = filename;
        } else {
            this.filename = file.getName();
        }
        this.charset = charset;
        this.request = request;
    }

    /**
     * @since 4.1
     */
    public ProgressFileBody(final File file, final String filename,
                            final String mimeType, final String charset) {
        super(mimeType);
        if (file == null) {
            throw new IllegalArgumentException("File may not be null");
        }
        this.file = file;
        if (filename != null) {
            this.filename = filename;
        } else {
            this.filename = file.getName();
        }
        this.charset = charset;
    }

    /**
     * @since 4.1
     */
    public ProgressFileBody(final File file, final String mimeType,
                            final String charset) {
        this(file, null, mimeType, charset);
    }

    public ProgressFileBody(final File file, final String mimeType) {
        this(file, mimeType, null);
    }

    public ProgressFileBody(final File file) {
        this(file, "application/octet-stream");
    }

    public InputStream getInputStream() throws IOException {
        return new FileInputStream(this.file);
    }

    public void writeTo(final OutputStream out) throws IOException {
        if (out == null) {
            throw new IllegalArgumentException("Output stream may not be null");
        }
        InputStream in = new FileInputStream(this.file);
        try {
            byte[] tmp = new byte[DEFAULT_BUFFER_SIZE];
            int l;
            long progress = 0;
            long total = this.file.length();
            HttpLog.v("Upload write total = " + total);

            while ((l = in.read(tmp)) != -1) {
                if (request.isCanceled()) {
                    HttpLog.v("Upload canceled.");
                    request.cancel();
                    break;
                }
                out.write(tmp, 0, l);
                progress += l;

                if (needProgress(l, total)) {
                    request.pushProgress(progress, total);
                }
            }
            out.flush();
        } finally {
            in.close();
        }
    }

    boolean needProgress(long progress, long total) {
        this.progressStep += progress;
        if (this.progressStep > total / progressPercent || progress >= total) {
            this.progressStep = 0;
            return true;
        }
        return false;
    }

    public String getTransferEncoding() {
        return MIME.ENC_BINARY;
    }

    public String getCharset() {
        return charset;
    }

    public long getContentLength() {
        return this.file.length();
    }

    public String getFilename() {
        return filename;
    }

    public File getFile() {
        return this.file;
    }

}
