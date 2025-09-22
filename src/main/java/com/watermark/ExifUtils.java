package com.watermark;

import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import org.apache.commons.imaging.formats.tiff.constants.TiffTagConstants;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExifUtils {
    
    private static final SimpleDateFormat EXIF_DATE_FORMAT = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
    private static final SimpleDateFormat OUTPUT_DATE_FORMAT = new SimpleDateFormat("yyyy年MM月dd日");
    
    public static String getCaptureTimeFromExif(File imageFile) throws IOException {
        try {
            ImageMetadata metadata = Imaging.getMetadata(imageFile);
            if (metadata instanceof JpegImageMetadata) {
                JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
                TiffImageMetadata exif = jpegMetadata.getExif();
                
                if (exif != null) {
                    // 尝试读取DateTimeOriginal（拍摄时间）
                    String dateTimeOriginal = getExifValue(exif, ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL);
                    if (dateTimeOriginal != null) {
                        return formatDate(dateTimeOriginal);
                    }
                    
                    // 如果没有DateTimeOriginal，尝试读取DateTime（修改时间）
                    String dateTime = getExifValue(exif, TiffTagConstants.TIFF_TAG_DATE_TIME);
                    if (dateTime != null) {
                        return formatDate(dateTime);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("读取EXIF信息失败: " + e.getMessage());
        }
        
        return null;
    }
    
    private static String getExifValue(TiffImageMetadata exif, TagInfo tag) {
        try {
            Object value = exif.getFieldValue(tag);
            return value != null ? value.toString() : null;
        } catch (Exception e) {
            return null;
        }
    }
    
    private static String formatDate(String exifDateString) {
        try {
            Date date = EXIF_DATE_FORMAT.parse(exifDateString);
            return OUTPUT_DATE_FORMAT.format(date);
        } catch (ParseException e) {
            System.err.println("解析EXIF日期格式失败: " + exifDateString);
            return null;
        }
    }
}