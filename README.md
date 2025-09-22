# Photo-Watermark

一个基于Java的图片水印添加工具，支持EXIF信息读取和自定义水印样式。

## ✨ 功能特性

- 📸 **EXIF信息读取**：自动从图片中读取拍摄时间作为水印文本
- 🎨 **自定义水印样式**：支持字体大小、颜色、位置、透明度配置
- 🔄 **批量处理**：支持处理多张图片
- 💾 **智能备份**：自动备份原始文件
- 🛡️ **错误处理**：完善的异常处理和日志记录

## 🚀 快速开始

### 编译项目
```bash
mvn clean package
```

### 运行程序（处理单个文件）
```bash
java -jar target/PhotoWatermark-jar-with-dependencies.jar image.jpg
```

### 运行程序（处理文件夹）
```bash
java -jar target/PhotoWatermark-jar-with-dependencies.jar images_folder
```

### 完整选项
```bash
java -jar target/PhotoWatermark-jar-with-dependencies.jar \
  images_folder \
  -t "自定义水印文本" \
  -s 24 \
  -c "#FF0000" \
  -p BOTTOM_RIGHT \
  -a 80
```

## 📋 命令行选项

| 选项 | 全称 | 说明 | 默认值 |
|------|------|------|--------|
| `-t` | `--text` | 水印文本 | EXIF拍摄时间 |
| `-s` | `--size` | 字体大小 | 14 |
| `-c` | `--color` | 字体颜色 | "black" |
| `-p` | `--position` | 水印位置 | BOTTOM_RIGHT |
| `-a` | `--opacity` | 透明度 (0-100) | 100 |

### 位置选项
- `TOP_LEFT` - 左上角
- `TOP_RIGHT` - 右上角  
- `BOTTOM_LEFT` - 左下角
- `BOTTOM_RIGHT` - 右下角
- `CENTER` - 中心

## 🏗️ 项目结构

```
src/main/java/com/watermark/
├── PhotoWatermark.java      # 主程序入口
├── WatermarkPosition.java   # 水印位置枚举
└── ExifUtils.java           # EXIF工具类
```

## 📦 依赖管理

项目使用Maven管理依赖，主要依赖包括：
- Apache Commons Imaging - EXIF数据处理
- Commons CLI - 命令行参数解析
- SLF4J - 日志记录

## 📖 详细文档

- [使用说明](USAGE.md) - 详细的使用指南和示例
- [需求文档](需求文档.md) - 完整的功能需求和技术方案

## 🐛 问题修复

### 已修复的编译错误
1. ✅ **找不到符号 EXIF_TAG_DATE_TIME** - 已更正为正确的常量名称 `TIFF_TAG_DATE_TIME`
2. ✅ **类型转换错误** - 已修复 `Object` 到 `String` 的类型转换问题

### 功能增强
3. ✅ **文件夹处理支持** - 现在支持输入单个文件或整个文件夹，自动创建输出目录

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。