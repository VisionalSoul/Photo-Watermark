@echo off
echo ===== 图片水印程序测试脚本 =====
echo.
echo 1. 检查编译结果...
if exist target\PhotoWatermark-jar-with-dependencies.jar (
    echo ✓ 编译成功！JAR文件位置: target\PhotoWatermark-jar-with-dependencies.jar
    echo.
    echo 2. 使用方法：
    echo    java -jar target\PhotoWatermark-jar-with-dependencies.jar [选项] <图片路径或文件夹>
    echo.
    echo 3. 示例命令（处理单个文件）：
    echo    java -jar target\PhotoWatermark-jar-with-dependencies.jar -i test.jpg -o output.jpg -t "自定义水印文本" -s 24 -c "#FF0000" -p BOTTOM_RIGHT -a 80
    echo.
    echo 4. 测试中文字符显示（日期中的斜杠）：
    echo    java -jar target\PhotoWatermark-jar-with-dependencies.jar 测试图片.jpg
    echo.
    echo 5. 示例命令（处理文件夹）：
    echo    java -jar target\PhotoWatermark-jar-with-dependencies.jar images_folder -s 20 -c "#FFFFFF" -p BOTTOM_RIGHT -a 70
    echo.
    echo 5. 可用选项：
    echo    -s, --size       字体大小 (可选，默认14)
    echo    -c, --color      字体颜色 (可选，默认"black")
    echo    -p, --position   水印位置 (可选，默认BOTTOM_RIGHT)
    echo    -a, --opacity    透明度 (可选，默认100)
    echo.
    echo 6. 位置选项：
    echo    TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT, CENTER
    echo.
    echo 7. 输出说明：
    echo    - 单个文件：在原文件目录下创建"原目录名_watermark"子目录
    echo    - 文件夹：在原文件夹目录下创建"原文件夹名_watermark"子目录
) else (
    echo ✗ 编译失败，请先运行 mvn package
)
echo.
echo ===== 测试完成 =====
pause