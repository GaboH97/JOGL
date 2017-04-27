/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import com.jogamp.opengl.GL2;
import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

/**
 *
 * @author Invitado
 */
public class TextureLoader {

    private GL2 gl;
    private IntBuffer skyBoxTextures;
    private ArrayList<Integer> textures;
    private static String[] TEXTURE_PATHS = {"right", "left", "top", "bottom", "back", "front"};

    public TextureLoader(GL2 gl) {
        this.gl = gl;
        textures = new ArrayList<>();
    }

    public int loadCubeMap() {
        int textID = 0;
        gl.glActiveTexture(GL2.GL_TEXTURE0);
        gl.glBindTexture(GL2.GL_TEXTURE_CUBE_MAP, textID);

        for (int i = 0; i < TEXTURE_PATHS.length; i++) {
            TextureDataBuffer dataBuffer = decodeTextureFile("util.skyboxImages/" + TEXTURE_PATHS[i] + ".png");

            /**
             * AS FOR THE CUBE TEXTURE, GIVEN THE CUBE MAP TEXTURE'S IDS ARE
             * CONSECUTIVE, WE FIRST CALL ID FOR NEGATIVE X AND ADD CURRENT
             * VALUE OF "i" INSTEAD OF CALLING ALL IDS
             */
            gl.glTexImage2D(GL2.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL2.GL_RGBA,
                    dataBuffer.getWidth(), dataBuffer.getHeight(), 0, GL2.GL_RGBA,
                    GL2.GL_UNSIGNED_BYTE, dataBuffer.getBuffer());
        }

        /**
         * MAKE TEXTURE SMOOTHER
         */
        gl.glTexParameteri(GL2.GL_TEXTURE_CUBE_MAP, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
        gl.glTexParameteri(GL2.GL_TEXTURE_CUBE_MAP, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
        gl.glTexParameteri(GL2.GL_TEXTURE_CUBE_MAP, GL2.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP_TO_EDGE);
        gl.glTexParameteri(GL2.GL_TEXTURE_CUBE_MAP, GL2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP_TO_EDGE);
        gl.glTexParameteri(GL2.GL_TEXTURE_CUBE_MAP, GL2.GL_TEXTURE_WRAP_R, GL2.GL_CLAMP_TO_EDGE);
        gl.glBindTexture(GL2.GL_TEXTURE_CUBE_MAP, 0);
        textures.add(textID);
        return textID;
    }

    /**
     *
     * @param fileName
     * @return A Texture data buffer with the width/height of the image plus its
     * data.
     */
    private TextureDataBuffer decodeTextureFile(String fileName) {
        int width = 0;
        int height = 0;
        ByteBuffer buffer = null;
        try {
            FileInputStream in = new FileInputStream(fileName);
            PNGDecoder decoder = new PNGDecoder(in);
            width = decoder.getWidth();
            height = decoder.getHeight();
            buffer = ByteBuffer.allocateDirect(4 * width * height);
            decoder.decode(buffer, width * 4, Format.RGBA);
            buffer.flip();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Tried to load texture " + fileName + ", didn't work");
            System.exit(-1);
        }
        return new TextureDataBuffer(width, height, buffer);
    }
}
