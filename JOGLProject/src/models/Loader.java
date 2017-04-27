package models;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.sun.prism.impl.BufferUtil;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

/**
 * THIS CLASS IS USED TO LOAD 3D MODELS INTO MEMORY LIKE VAOS, VBOS, SHADERS AND
 * TEXTURES
 *
 * @author Gabriel Huertas
 */
public class Loader {

    private GL2 gl;
    private IntBuffer vertexArray; //TO BE CHECKED
    private ArrayList<Integer> vaos;
    private ArrayList<Integer> vbos;

    public Loader(GL2 gl) {
        this.gl = gl;
        this.vertexArray = vertexArray;
        this.vaos = new ArrayList<>();
        this.vbos = new ArrayList<>();
    }

    /**
     * Enters a float array of positions and returns a Raw model-like
     * representation of the stored data
     *
     * @param pos
     * @return Raw model-like representation of the stored data
     */
    public RawModel toVAO(float[] pos, int[] indices) {
        int ID = createVAO();
        bindIndicesBuffer(indices);
        vaos.add(ID); //Add the created vao ID to the list of vaos
        storeData(ID, pos);
        unbindVAO();
        return new RawModel(ID, pos.length / 3); // Divided by three since we're going to store x,y,z points 
    }

    public void bindIndicesBuffer(int[] indices) {
        int vboID = 0;//gl.glGenBuffers(0, vertexArray);
        vbos.add(vboID);
        gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, vboID);
        IntBuffer buffer = intArrayToIntBuffer(indices);
        // gl.glBufferData(GL2.GL_ARRAY_BUFFER, buffer, buffer.limit(), GL2.GL_STATIC_DRAW);
    }

    /**
     * Creates an empty VAO
     *
     * @return The ID of the created VAO
     */
    private int createVAO() {
        /**
         * MUST CREATE AN EMPTY VAO AND RETURN ITS ID (FOUND WITH LWJGL)
         */
        int vaoID = 0;//gl.glGenVertexArrays(0, vertexArray)
        vaos.add(vaoID); //Add the created vao ID to the list of vaos

        gl.glBindVertexArray(vaoID); //Allows to allocate data into the proper VAO identified with the given ID
        return vaoID;
    }

    /**
     * This method stores the data in the specified ID in the atrributes array
     * of the VAO
     *
     * @param id
     * @param data
     */
    private void storeData(int ID, float[] data) {
        int vboID = 0;//gl.glGenBuffers(id, vertexArray); //Search the VAO ID to be used and bind it to use it
        vbos.add(ID); //Add the created vao ID to the list of vaos
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vboID); // (Type of VBO, ID of the VAO)
        FloatBuffer buffer = floatArrayToFloatBuffer(data);
        /*
           STORE THE DATA INTO DE BUFFER
        * (The type of array to use, the buffer that contains the data, the use mode of the Buffer data)
            
         */
        //gl.glBufferData(GL2.GL_ARRAY_BUFFER, buffer, buffer.limit(), GL2.GL_STATIC_DRAW);
        /**
         * (The ID of to localize the data} ,The length of the vertex ,The type
         * of data to be stored , is data normalized? , gap between every vertex
         * data , offset (Default is zero))
         */
        gl.glVertexAttribPointer(ID, 3, GL2.GL_FLOAT, false, 0, 0);
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0); //Unbind used buffer
    }

    private void unbindVAO() {
        gl.glBindVertexArray(0);
    }

    private FloatBuffer floatArrayToFloatBuffer(float[] data) {
        /**
         * Create a Float buffer with the same length of the incoming data
         * array, then write the data itself into the buffer, clip it when it's
         * done and return the buffer
         */
        FloatBuffer buffer = BufferUtil.newFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private IntBuffer intArrayToIntBuffer(int[] data) {
        /**
         * Create a Float buffer with the same length of the incoming data
         * array, then write the data itself into the buffer, clip it when it's
         * done and return the buffer
         */
        IntBuffer buffer = BufferUtil.newIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    /**
     * Clear up all the vaos and vbos of the Loader
     */
    private void clearAll() {
        for (Integer vao : vaos) {
            gl.glDeleteVertexArrays(vao, vertexArray);
        }
        for (Integer vbo : vbos) {
            gl.glDeleteBuffers(0, vertexArray);
        }
    }
}
