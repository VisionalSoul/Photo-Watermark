package com.watermark;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class PhotoWatermark {
    
    private static final List<String> SUPPORTED_EXTENSIONS = List.of(".jpg", ".jpeg", ".png", ".bmp", ".gif");
    
    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            return;
        }
        
        Config config = parseArguments(args);
        if (config == null) {
            return;
        }
        
        try {
            processImages(config);
            System.out.println("处理完成！");
        } catch (Exception e) {
            System.err.println("处理过程中发生错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static Config parseArguments(String[] args) {
        Config config = new Config();
        
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            
            switch (arg) {
                case "-h":
                case "--help":
                    printUsage();
                    return null;
                case "-s":
                case "--size":
                    if (i + 1 < args.length) {
                        try {
                            config.fontSize = Integer.parseInt(args[++i]);
                        } catch (NumberFormatException e) {
                            System.err.println("字体大小必须是数字");
                            return null;
                        }
                    }
                    break;
                case "-c":
                case "--color":
                    if (i + 1 < args.length) {
                        config.fontColor = parseColor(args[++i]);
                    }
                    break;
                case "-p":
                case "--position":
                    if (i + 1 < args.length) {
                        config.position = WatermarkPosition.fromString(args[++i]);
                        if (config.position == null) {
                            System.err.println("无效的水印位置");
                            return null;
                        }
                    }
                    break;
                case "-o":
                case "--opacity":
                    if (i + 1 < args.length) {
                        try {
                            config.opacity = Integer.parseInt(args[++i]);
                            if (config.opacity < 0 || config.opacity > 100) {
                                System.err.println("透明度必须在0-100之间");
                                return null;
                            }
                        } catch (NumberFormatException e) {
                            System.err.println("透明度必须是数字");
                            return null;
                        }
                    }
                    break;
                default:
                    if (!arg.startsWith("-")) {
                        config.inputPath = arg;
                    }
                    break;
            }
        }
        
        if (config.inputPath == null) {
            System.err.println("请指定图片路径");
            printUsage();
            return null;
        }
        
        File inputFile = new File(config.inputPath);
        if (!inputFile.exists()) {
            System.err.println("指定的路径不存在: " + config.inputPath);
            return null;
        }
        
        return config;
    }
    
    private static void printUsage() {
        System.out.println("用法: java -jar PhotoWatermark.jar [选项] <图片路径或文件夹>");
        System.out.println("选项:");
        System.out.println("  -s, --size <大小>        字体大小（默认：14）");
        System.out.println("  -c, --color <颜色>       字体颜色（默认：black）");
        System.out.println("  -p, --position <位置>    水印位置（默认：BOTTOM_RIGHT）");
        System.out.println("  -o, --opacity <透明度>   水印透明度（0-100，默认：100）");
        System.out.println("  -h, --help              显示帮助信息");
        System.out.println("\n支持的位置: TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT, CENTER");
        System.out.println("\n说明:");
        System.out.println("  - 输入可以是单个图片文件或包含图片的文件夹");
        System.out.println("  - 输出文件将保存在原目录下的'原目录名_watermark'子目录中");
    }
    
    private static Color parseColor(String colorStr) {
        switch (colorStr.toLowerCase()) {
            case "black": return Color.BLACK;
            case "white": return Color.WHITE;
            case "red": return Color.RED;
            case "green": return Color.GREEN;
            case "blue": return Color.BLUE;
            default:
                try {
                    return Color.decode(colorStr);
                } catch (NumberFormatException e) {
                    System.err.println("无法识别的颜色: " + colorStr + "，使用默认黑色");
                    return Color.BLACK;
                }
        }
    }
    
    private static void processImages(Config config) throws IOException {
        File inputFile = new File(config.inputPath);
        List<File> imageFiles = new ArrayList<>();
        
        if (inputFile.isFile()) {
            if (isSupportedImageFile(inputFile)) {
                imageFiles.add(inputFile);
            } else {
                System.err.println("不支持的文件格式: " + inputFile.getName());
                return;
            }
        } else if (inputFile.isDirectory()) {
            imageFiles = findImageFiles(inputFile);
            if (imageFiles.isEmpty()) {
                System.err.println("目录中没有找到支持的图片文件");
                return;
            }
        }
        
        File outputDir = createOutputDirectory(inputFile);
        
        for (File imageFile : imageFiles) {
            try {
                processSingleImage(imageFile, outputDir, config);
                System.out.println("已处理: " + imageFile.getName());
            } catch (Exception e) {
                System.err.println("处理文件失败: " + imageFile.getName() + " - " + e.getMessage());
            }
        }
    }
    
    private static List<File> findImageFiles(File directory) {
        List<File> imageFiles = new ArrayList<>();
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && isSupportedImageFile(file)) {
                    imageFiles.add(file);
                }
            }
        }
        return imageFiles;
    }
    
    private static boolean isSupportedImageFile(File file) {
        String fileName = file.getName().toLowerCase();
        return SUPPORTED_EXTENSIONS.stream().anyMatch(fileName::endsWith);
    }
    
    private static File createOutputDirectory(File inputFile) throws IOException {
        String outputDirName;
        if (inputFile.isFile()) {
            outputDirName = inputFile.getParentFile().getName() + "_watermark";
        } else {
            outputDirName = inputFile.getName() + "_watermark";
        }
        
        File outputDir = new File(inputFile.getParentFile(), outputDirName);
        if (!outputDir.exists()) {
            if (!outputDir.mkdir()) {
                throw new IOException("无法创建输出目录: " + outputDir.getAbsolutePath());
            }
        }
        return outputDir;
    }
    
    private static void processSingleImage(File imageFile, File outputDir, Config config) throws IOException {
        BufferedImage originalImage = ImageIO.read(imageFile);
        if (originalImage == null) {
            throw new IOException("无法读取图片文件: " + imageFile.getName());
        }
        
        String watermarkText = getWatermarkText(imageFile);
        
        BufferedImage watermarkedImage = addWatermark(originalImage, watermarkText, config);
        
        String outputFileName = getOutputFileName(imageFile.getName());
        File outputFile = new File(outputDir, outputFileName);
        
        String formatName = getFormatName(imageFile.getName());
        ImageIO.write(watermarkedImage, formatName, outputFile);
    }
    
    private static String getWatermarkText(File imageFile) throws IOException {
        // 首先尝试从EXIF信息中读取拍摄时间
        String captureTime = ExifUtils.getCaptureTimeFromExif(imageFile);
        if (captureTime != null) {
            return captureTime;
        }
        
        // 如果没有EXIF信息，使用文件修改时间作为备选
        BasicFileAttributes attrs = Files.readAttributes(imageFile.toPath(), BasicFileAttributes.class);
        Date lastModified = new Date(attrs.lastModifiedTime().toMillis());
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        return dateFormat.format(lastModified);
    }
    
    private static BufferedImage addWatermark(BufferedImage originalImage, String text, Config config) {
        BufferedImage watermarkedImage = new BufferedImage(
            originalImage.getWidth(), 
            originalImage.getHeight(), 
            BufferedImage.TYPE_INT_RGB
        );
        
        Graphics2D g2d = watermarkedImage.createGraphics();
        g2d.drawImage(originalImage, 0, 0, null);
        
        // 设置字体和颜色
        Font font = new Font("SimSun", Font.PLAIN, config.fontSize);
        g2d.setFont(font);
        g2d.setColor(config.fontColor);
        
        // 设置透明度
        AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, config.opacity / 100.0f);
        g2d.setComposite(alpha);
        
        // 设置抗锯齿
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // 计算水印位置
        FontMetrics metrics = g2d.getFontMetrics();
        int textWidth = metrics.stringWidth(text);
        int textHeight = metrics.getHeight();
        
        Point position = calculatePosition(config.position, originalImage.getWidth(), 
                                         originalImage.getHeight(), textWidth, textHeight);
        
        // 绘制水印
        g2d.drawString(text, position.x, position.y);
        g2d.dispose();
        
        return watermarkedImage;
    }
    
    private static Point calculatePosition(WatermarkPosition position, int imageWidth, 
                                          int imageHeight, int textWidth, int textHeight) {
        int margin = 10;
        
        switch (position) {
            case TOP_LEFT:
                return new Point(margin, margin + textHeight);
            case TOP_RIGHT:
                return new Point(imageWidth - textWidth - margin, margin + textHeight);
            case BOTTOM_LEFT:
                return new Point(margin, imageHeight - margin);
            case BOTTOM_RIGHT:
                return new Point(imageWidth - textWidth - margin, imageHeight - margin);
            case CENTER:
                return new Point((imageWidth - textWidth) / 2, (imageHeight + textHeight) / 2);
            default:
                return new Point(margin, imageHeight - margin);
        }
    }
    
    private static String getOutputFileName(String originalName) {
        int dotIndex = originalName.lastIndexOf('.');
        if (dotIndex > 0) {
            String nameWithoutExt = originalName.substring(0, dotIndex);
            String extension = originalName.substring(dotIndex);
            return nameWithoutExt + "_watermark" + extension;
        }
        return originalName + "_watermark";
    }
    
    private static String getFormatName(String fileName) {
        if (fileName.toLowerCase().endsWith(".jpg") || fileName.toLowerCase().endsWith(".jpeg")) {
            return "JPEG";
        } else if (fileName.toLowerCase().endsWith(".png")) {
            return "PNG";
        } else if (fileName.toLowerCase().endsWith(".bmp")) {
            return "BMP";
        } else if (fileName.toLowerCase().endsWith(".gif")) {
            return "GIF";
        }
        return "JPEG";
    }
    
    static class Config {
        String inputPath;
        int fontSize = 14;
        Color fontColor = Color.BLACK;
        WatermarkPosition position = WatermarkPosition.BOTTOM_RIGHT;
        int opacity = 100;
    }
}