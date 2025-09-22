# Photo Watermark 使用说明

## 项目简介
这是一个基于Java的命令行图片水印添加工具，能够自动读取图片的EXIF拍摄时间信息，并将其作为水印添加到图片上。

## 功能特性
- 📷 自动读取图片EXIF拍摄时间信息
- 🎨 支持自定义字体大小、颜色、位置和透明度
- 📁 支持批量处理目录中的所有图片
- 🖼️ 支持多种图片格式：JPG、JPEG、PNG、BMP、GIF
- 🔄 自动创建输出目录，保持原文件结构

## 快速开始

### 1. 编译项目
```bash
mvn clean package
```

### 2. 运行程序
```bash
java -jar target/PhotoWatermark-jar-with-dependencies.jar [选项] <图片路径>
```

### 3. 示例用法

**基本用法（添加默认水印）**
```bash
java -jar PhotoWatermark.jar /path/to/photos
```

**自定义字体大小和颜色**
```bash
java -jar PhotoWatermark.jar -s 20 -c red /path/to/image.jpg
```

**指定水印位置和透明度**
```bash
java -jar PhotoWatermark.jar -p TOP_LEFT -o 80 /path/to/folder
```

**完整参数示例**
```bash
java -jar PhotoWatermark.jar --size 18 --color "#FF0000" --position BOTTOM_RIGHT --opacity 70 /path/to/images
```

## 命令行选项

| 选项 | 缩写 | 参数 | 默认值 | 描述 |
|------|------|------|---------|------|
| `--size` | `-s` | 数字 | 14 | 字体大小（pt） |
| `--color` | `-c` | 颜色值 | black | 字体颜色（支持颜色名称或RGB值） |
| `--position` | `-p` | 位置 | BOTTOM_RIGHT | 水印位置 |
| `--opacity` | `-o` | 0-100 | 100 | 水印透明度 |
| `--help` | `-h` | 无 | - | 显示帮助信息 |

### 支持的水印位置
- `TOP_LEFT` - 左上角
- `TOP_RIGHT` - 右上角  
- `BOTTOM_LEFT` - 左下角
- `BOTTOM_RIGHT` - 右下角
- `CENTER` - 居中

### 支持的颜色格式
- 颜色名称：`black`, `white`, `red`, `green`, `blue`等
- RGB值：`#FF0000`, `#00FF00`, `#0000FF`等
- 十进制RGB：`rgb(255,0,0)`

## 输出说明

程序会在原图片所在目录下创建名为`原目录名_watermark`的子目录，处理后的图片将保存到此目录中。

### 示例文件结构

**输入目录结构：**
```
photos/
  ├── vacation.jpg
  ├── family.png
  └── landscape.jpeg
```

**输出目录结构：**
```
photos/
  ├── vacation.jpg
  ├── family.png
  ├── landscape.jpeg
  └── photos_watermark/
        ├── vacation_watermark.jpg
        ├── family_watermark.png
        └── landscape_watermark.jpeg
```

## 技术依赖

- Java 11+
- Apache Commons Imaging - EXIF信息读取
- Java AWT - 图像处理和水印绘制

## 常见问题

### 1. 图片没有EXIF信息怎么办？
程序会自动使用文件的修改时间作为水印文本。

### 2. 支持哪些图片格式？
支持JPG、JPEG、PNG、BMP、GIF格式。

### 3. 如何处理大图片？
程序会按原尺寸处理图片，建议确保有足够的内存来处理大尺寸图片。

### 4. 水印位置不合适怎么办？
可以调整`--position`参数选择合适的位置，或调整`--size`参数改变字体大小。

## 错误处理

程序会提供详细的错误信息，包括：
- 文件不存在或路径错误
- 不支持的图片格式
- EXIF读取失败
- 图片处理错误

## 许可证

MIT License

## 贡献

欢迎提交Issue和Pull Request来改进这个项目。