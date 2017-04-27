package models.skybox;

/**
 * @author L305
 */
public class SkyboxRenderer {

    /**
     * IN ORDER TO MAKE A DYNAMIC SIZE CUBE MAP, A STATIC SIZE IS SET AND THEN
     * PASSED TO THE VERTEX DATA THAT RENDERS THE CUBE.
     */
    private static final float SIZE = 500f;

    private static final float[] VERTICES = {
        -SIZE, SIZE, -SIZE,
        -SIZE, -SIZE, -SIZE,
        SIZE, -SIZE, -SIZE,
        SIZE, -SIZE, -SIZE,
        SIZE, SIZE, -SIZE,
        -SIZE, SIZE, -SIZE,
        -SIZE, -SIZE, SIZE,
        -SIZE, -SIZE, -SIZE,
        -SIZE, SIZE, -SIZE,
        -SIZE, SIZE, -SIZE,
        -SIZE, SIZE, SIZE,
        -SIZE, -SIZE, SIZE,
        SIZE, -SIZE, -SIZE,
        SIZE, -SIZE, SIZE,
        SIZE, SIZE, SIZE,
        SIZE, SIZE, SIZE,
        SIZE, SIZE, -SIZE,
        SIZE, -SIZE, -SIZE,
        -SIZE, -SIZE, SIZE,
        -SIZE, SIZE, SIZE,
        SIZE, SIZE, SIZE,
        SIZE, SIZE, SIZE,
        SIZE, -SIZE, SIZE,
        -SIZE, -SIZE, SIZE,
        -SIZE, SIZE, -SIZE,
        SIZE, SIZE, -SIZE,
        SIZE, SIZE, SIZE,
        SIZE, SIZE, SIZE,
        -SIZE, SIZE, SIZE,
        -SIZE, SIZE, -SIZE,
        -SIZE, -SIZE, -SIZE,
        -SIZE, -SIZE, SIZE,
        SIZE, -SIZE, -SIZE,
        SIZE, -SIZE, -SIZE,
        -SIZE, -SIZE, SIZE,
        SIZE, -SIZE, SIZE
    };

    private static String TEXTURE_SUFFIXES[] = {"right", "left", "top", "bottom", "back", "front"};
    /*private RawModel cube; Here goes the raw model*/
    private int texture;
    private SkyboxShader shader;

    /* public SkyboxRenderer(Loader loader, Matrix4f projectionMatrix){
        cube = loader.loadToVAO(VERTICES,3);
        texture = loader.loadCubeMap(TEXTURE_FILES);
        shader = new SkyboxShader();
        shader.start();
        shader.loadProjectionMatrix(this);
     */
}
