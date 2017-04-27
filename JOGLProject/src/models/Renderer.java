package models;

import com.jogamp.opengl.GL2;

/**
 *
 * @author Gabriel Huertas
 */
public class Renderer {
    
    private GL2 gl;
    
    public void setUp() {
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
        gl.glClearColor(1, 0, 0, 1);        
    }

    /**
     * Renders a raw model that contains a buffer of data that points to a VAO
     * given its ID with a proper amount of vertexes to be drawn
     *
     * @param model
     */
    public void render(RawModel model) {
        gl.glBindVertexArray(model.getVaoID()); //Bind the vertex array to the VAO with the specified ID
        gl.glEnableVertexAttribArray(0); // Grant access to the VBO as an attrib array (Since there are no more attrib arrays, we set the index to 0)
        gl.glDrawElements(GL2.GL_TRIANGLES, model.getVertexCount(), GL2.GL_UNSIGNED_INT, 0); //properly draw arrays in forms of triangles 
        gl.glDisableVertexAttribArray(0); // 
        gl.glBindVertexArray(0);
    }
}
