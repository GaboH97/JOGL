
package shapes;

/**
 *
 * @author Gabriel Huertas
 */
public class Square extends Shape {

    private float[] positionData = {
        -0.9f, 0.2f, 0.0f,
        -0.5f, 0.2f, 0.0f,
        -0.5f, 0.9f, 0.0f,
        -0.9f, 0.9f, 0.0f
    };
    
    private float[] colorData = {
        1.0f,0.0f,0.0f,
        0.0f,1.0f,0.0f,
        0.0f,0.0f,1.0f,
        1.0f,1.0f,1.0f
    };

    @Override
    public float[] getPositionData() {
        return this.positionData;
    }

    @Override
    public float[] getColorData() {
        return this.colorData;
    }

}
