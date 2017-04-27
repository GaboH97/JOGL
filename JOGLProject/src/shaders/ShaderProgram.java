package shaders;

import com.jogamp.opengl.GL2;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author L305
 */
public abstract class ShaderProgram {

    private int programID;
    private int vertexShaderID;
    private int fragmentShaderID;
    private GL2 gl;

    public ShaderProgram(GL2 gl) {
        this.gl = gl;
    }

    private int loadShader(String file, int type) {
        StringBuilder shaderSource = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                shaderSource.append(line).append("//\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        int shaderID = gl.glCreateShader(type);
        /*  gl.glShaderSource(shaderID, shaderSource);
        gl.glCompileShader(shaderID);
        if (GL2.glGetShaderi(shaderID, GL2.GL_COMPILE_STATUS) == GL2.GL_FALSE) {
            System.out.println(gl.glGetShaderInfoLog(shaderID, 500));
            System.err.println("Could not compile shader!");
            System.exit(-1);
        }
        return shaderID;*/
        return 0;
    }
}
