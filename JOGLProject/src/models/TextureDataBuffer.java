
package models;

import java.nio.ByteBuffer;

/**
 *
 * @author GABRIEL HUERTAS
 */
public class TextureDataBuffer {
    
    private int width; //WIDTH OF THE 2D TEXTURE
    private int height; // HEIGHT OF THE 2D TEXTURE 
    private ByteBuffer buffer; // BUFFER TO STORE THE TEXTURE DATA

    public TextureDataBuffer(int width, int height, ByteBuffer buffer) {
        this.width = width;
        this.height = height;
        this.buffer = buffer;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }

    public void setBuffer(ByteBuffer buffer) {
        this.buffer = buffer;
    }
    
    
    
    
    
}
