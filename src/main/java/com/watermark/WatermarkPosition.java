package com.watermark;

public enum WatermarkPosition {
    TOP_LEFT,
    TOP_RIGHT,
    BOTTOM_LEFT,
    BOTTOM_RIGHT,
    CENTER;
    
    public static WatermarkPosition fromString(String position) {
        if (position == null) {
            return BOTTOM_RIGHT;
        }
        
        try {
            return WatermarkPosition.valueOf(position.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}