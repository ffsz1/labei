package com.tongdaxing.xchat_core.utils;


import com.tongdaxing.xchat_framework.util.util.StringUtils;
import com.tongdaxing.xchat_framework.util.util.file.JXFileUtils;

/**
 * Created by lijun3 on 2016/3/15.
 *
 * copy from ImageViewUrl.java in mobile-bs2
 *
 * <dependency>
    <groupId>com.yy.ent.mobile</groupId>
    <artifactId>mobile-bs2</artifactId>
    <version>1.1.3</version>
   </dependency>
 *
 * image.yy.com 图片处理服务 参数工具类
 *
 */
public class ImgServiceUrlUtil {

    public static final String IMP_SERVICE_HEADER = "http://image.yy.com/";

    private static final String DEFAULT_WIDTH_PLACEHOLDER = "{w}";
    private static final String DEFAULT_HEIGHT_PLACEHOLDER = "{h}";
    private Pattern pattern;
    private CutModel cm;
    private int w;
    private int h;
    private boolean exif;
    private String widthPlaceholder;
    private String heightPlaceholder;
    private boolean useBlur;
    private double blur;

    public static ImgServiceUrlUtil atMostEdge() {
        return new ImgServiceUrlUtil(Pattern.AT_MOST_EDGE);
    }

    public static ImgServiceUrlUtil atLeastEdge() {
        return new ImgServiceUrlUtil(Pattern.AT_LEAST_EDGE);
    }

    public static ImgServiceUrlUtil atMostSize() {
        return new ImgServiceUrlUtil(Pattern.AT_MOST_SIZE);
    }

    public static ImgServiceUrlUtil atLeastSize() {
        return new ImgServiceUrlUtil(Pattern.AT_LEAST_SIZE);
    }

    public static ImgServiceUrlUtil cutBySize(CutModel cm) {
        return new ImgServiceUrlUtil(Pattern.CUT_BY_SIZE, cm);
    }

    public static ImgServiceUrlUtil cutBySize() {
        return cutBySize(CutModel.CENTER_TOP);
    }

    public static ImgServiceUrlUtil cutByEdge(CutModel cm) {
        return new ImgServiceUrlUtil(Pattern.CUT_BY_EDGE, cm);
    }

    public static ImgServiceUrlUtil cutByEdge() {
        return cutByEdge(CutModel.CENTER_TOP);
    }

    private ImgServiceUrlUtil(Pattern pattern) {
        this(pattern, null);
    }

    private ImgServiceUrlUtil(Pattern pattern, CutModel cm) {
        this.pattern = pattern;
        this.cm = (cm == null ? CutModel.CENTER_TOP : cm);
    }

    /**
     * 长边
     * @param w
     * @return
     */
    public final ImgServiceUrlUtil width(int w) {
        this.w = w;
        return this;
    }

    /**
     * 短边
     * @param h
     * @return
     */
    public final ImgServiceUrlUtil height(int h) {
        this.h = h;
        return this;
    }

    public final ImgServiceUrlUtil widthPlaceholder() {
        return widthPlaceholder(DEFAULT_WIDTH_PLACEHOLDER);
    }

    public final ImgServiceUrlUtil widthPlaceholder(String widthPlaceholder) {
        this.widthPlaceholder = widthPlaceholder;
        return this;
    }

    public final ImgServiceUrlUtil heightPlaceholder() {
        return heightPlaceholder(DEFAULT_HEIGHT_PLACEHOLDER);
    }

    public final ImgServiceUrlUtil heightPlaceholder(String heightPlaceholder) {
        this.heightPlaceholder = heightPlaceholder;
        return this;
    }

    public final ImgServiceUrlUtil dropExif() {
        this.exif = true;
        return this;
    }

    public final ImgServiceUrlUtil blur(double blur) {
        this.useBlur = true;
        this.blur = blur;
        return this;
    }

    public final String on(String bs2Url) {
        if (StringUtils.isEmpty(bs2Url)) {
            throw new IllegalArgumentException("bs2Url is blank");
        }
        String bucket = StringUtils.substringBetween(bs2Url, "://", ".bs2");
        if (StringUtils.isEmpty(bucket)) {
            throw new IllegalArgumentException("bucket is blank");
        }
        String filename = JXFileUtils.getFileName(StringUtils.trim(bs2Url));
        if (StringUtils.isEmpty(filename)) {
            throw new IllegalArgumentException("filename is blank");
        }

        StringBuffer buffer = new StringBuffer();
        buffer.append(IMP_SERVICE_HEADER).append(bucket).append('/').append(filename).append("?imageview/").append(this.pattern.value);

        if ((this.pattern == Pattern.CUT_BY_SIZE) || (this.pattern == Pattern.CUT_BY_EDGE)) {
            buffer.append('/').append(this.cm.value);
        }
        if (this.w > 0)
            buffer.append("/w/").append(this.w);
        else if (!StringUtils.isEmpty(this.widthPlaceholder)) {
            buffer.append("/w/").append(StringUtils.trim(this.widthPlaceholder));
        }
        if (this.h > 0)
            buffer.append("/h/").append(this.h);
        else if (!StringUtils.isEmpty(this.heightPlaceholder)) {
            buffer.append("/h/").append(StringUtils.trim(this.heightPlaceholder));
        }
        if (this.exif) {
            buffer.append("/exif/0");
        }
        if (this.useBlur) {
            buffer.append("/blur/").append(this.blur);
        }
        return buffer.toString();
    }

    public static enum CutModel {
        CENTER_CENTER("0"), CENTER_TOP("1"), CENTER_BOTTOM("2"),
        LEFT_CENTER("3"), LEFT_TOP("4"), LEFT_BOTTOM("5"),
        RIGHT_CENTER("6"), RIGHT_TOP("7"), RIGHT_BOTTOM("8");

        private String value;

        private CutModel(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    public static enum Pattern {
        AT_MOST_EDGE("0"), AT_LEAST_EDGE("1"),
        AT_MOST_SIZE("2"), AT_LEAST_SIZE("3"),
        CUT_BY_SIZE("4"), CUT_BY_EDGE("5");

        private String value;

        private Pattern(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }
}
