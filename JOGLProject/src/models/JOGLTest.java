package models;

import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_TRIANGLES;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_AMBIENT;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_DIFFUSE;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_LIGHT0;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_LIGHT1;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_LIGHTING;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_POSITION;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author Gabriel Huertas
 */
public class JOGLTest extends JFrame implements GLEventListener, KeyListener {

    private GLU glu;
    private GLUT glut;
    private float rotationalAngleX;
    private float rotationalAngleY;
    private float rotationalAngleZ;
    private float xCord;
    private float zCord;
    private float zoom;
    private Texture texture;
    private GLProfile glProfile;
    private static final float ROTATIONAL_ANGLE_INCREMENT = 5.0f; // rotational angle in degree for pyramid
    private static final float ZOOM_FACTOR = 0.1f;
    private static final float DISPLACING_DISTANCE = 0.05f;
    private float dlr;
    private float dlg;
    private float dlb;
    private float lpx;
    private float lpy;
    private float lpz;

    public JOGLTest() {
        zoom = 0;
        xCord = 0;
        zCord = -6.0f;
        dlb = 1.0f;
        dlg = 1.0f;
        dlr = 1.0f;
        glu = new GLU();
        glut = new GLUT();
        addKeyListener(this);
        setUpFrame();
    }

    public void setUpFrame() {

        glProfile = GLProfile.get(GLProfile.GL2);

        final GLCapabilities glCapabilities = new GLCapabilities(glProfile);

        GLCanvas glCanvas = new GLCanvas(glCapabilities);

        glCanvas.addGLEventListener((com.jogamp.opengl.GLEventListener) this);
        glCanvas.setSize(1080, 600);

        getContentPane().add(glCanvas);
        setSize(getContentPane().getPreferredSize());
        setTitle("JOGL TEST");
        setVisible(true);

        final FPSAnimator animator = new FPSAnimator(glCanvas, 300, true);
        animator.start();
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        gl.glEnable(GL2.GL_TEXTURE_2D);
        gl.glShadeModel(GL2.GL_SMOOTH);
        gl.glClearColor(0f, 0f, 0f, 0f);
        gl.glClearDepth(1.0f);
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glEnable(GL_LIGHTING); //enable lighting
        gl.glEnable(GL_LIGHT0); //enable LIGHT0 for diffuse light
        gl.glEnable(GL_LIGHT1); //enable LIGHT1 for Ambient Light
        gl.glDepthFunc(GL2.GL_LEQUAL);
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);

        //gl.glLightfv(HEIGHT, WIDTH, params);
        try {
            TextureData textureData = TextureIO.newTextureData(glProfile, getClass().getResource("/util/Jupiter.jpg"), false, TextureIO.JPG);
            texture = TextureIO.newTexture(textureData);

        } catch (IOException ex) {
            Logger.getLogger(JOGLTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }

    @Override
    public void display(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();

        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear color and depth buffers

        gl.glLoadIdentity();  // reset the model-view matrix

        /*
        * POPULATE LIGHTS VERTEX DATA
         */
        float ambientLight[] = {0.5f, 0.5f, 0.5f, 1.0f}; //Halfway between complete darkness (0.0f) and maximum shinness (1.0f)
        float diffuseLight[] = {dlr, dlg, dlb, 1.0f};
        float lightPosition[] = {lpx, lpy, lpz, 1.0f}; // Locate light source 2.0f units into the screen (Positive z-axis)

        gl.glLightfv(GL_LIGHT0, GL_DIFFUSE, FloatBuffer.wrap(diffuseLight));
        gl.glLightfv(GL_LIGHT1, GL_AMBIENT, FloatBuffer.wrap(ambientLight));
        gl.glLightfv(GL_LIGHT0, GL_POSITION, FloatBuffer.wrap(lightPosition));

        glu.gluLookAt(0.0, 0.0, 5.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);

        gl.glTranslatef(0, 0, zoom);

        // ----- Render the Pyramid -----
        gl.glTranslatef(-1.5f, 0.0f, -6.0f); // translate left and into the screen

        gl.glRotatef(rotationalAngleX, 1.0f, 0.0f, 0.0f); // rotate about the x-axis
        gl.glRotatef(rotationalAngleY, 0.0f, 1.0f, 0.0f); // rotate about the y-axis
        gl.glRotatef(rotationalAngleZ, 0.0f, 0.0f, 1.0f); // rotate about the z-axis

        gl.glBegin(GL_TRIANGLES); // of the pyramid

        // Font-face triangle
        gl.glColor3f(1.0f, 0.0f, 0.0f);  // Red
        gl.glVertex3f(0.0f, 1.0f, 0.0f);
        gl.glColor3f(0.0f, 1.0f, 0.0f);  // Green
        gl.glVertex3f(-1.0f, -1.0f, 1.0f);
        gl.glColor3f(0.0f, 0.0f, 1.0f);  // Blue
        gl.glVertex3f(1.0f, -1.0f, 1.0f);

        // Right-face triangle
        gl.glColor3f(1.0f, 0.0f, 0.0f);  // Red
        gl.glVertex3f(0.0f, 1.0f, 0.0f);
        gl.glColor3f(0.0f, 0.0f, 1.0f);  // Blue
        gl.glVertex3f(1.0f, -1.0f, 1.0f);
        gl.glColor3f(0.0f, 1.0f, 0.0f);  // Green
        gl.glVertex3f(1.0f, -1.0f, -1.0f);

        // Back-face triangle
        gl.glColor3f(1.0f, 0.0f, 0.0f);  // Red
        gl.glVertex3f(0.0f, 1.0f, 0.0f);
        gl.glColor3f(0.0f, 1.0f, 0.0f);  // Green
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glColor3f(0.0f, 0.0f, 1.0f);  // Blue
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);

        // Left-face triangle
        gl.glColor3f(1.0f, 0.0f, 0.0f);  // Red
        gl.glVertex3f(0.0f, 1.0f, 0.0f);
        gl.glColor3f(0.0f, 0.0f, 1.0f);  // Blue
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glColor3f(0.0f, 1.0f, 0.0f);  // Green
        gl.glVertex3f(-1.0f, -1.0f, 1.0f);

        gl.glEnd(); // of the pyramid

        //START DRAWING SOLID SPHERE
        gl.glLoadIdentity(); // reset the current model-view matrix
        gl.glTranslatef(0, 0, zoom);
        gl.glTranslatef(xCord, 0.0f, zCord); // translate right and into the screen

        gl.glRotatef(rotationalAngleX, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(rotationalAngleY, 0.0f, 1.0f, 0.0f);
        gl.glRotatef(rotationalAngleZ, 0.0f, 0.0f, 1.0f);

        //NORMALIZE RGB COLORS FOR BETTER TEXTURE DISPLAY
        gl.glColor3f(1.0f, 1.0f, 1.0f);

        texture.enable(gl);
        texture.bind(gl);

        GLUquadric sphere = glu.gluNewQuadric();
        glu.gluQuadricTexture(sphere, true);
        glu.gluQuadricDrawStyle(sphere, GLU.GLU_FILL);
        glu.gluQuadricNormals(sphere, GLU.GLU_FLAT);
        glu.gluQuadricOrientation(sphere, GLU.GLU_OUTSIDE);

        glu.gluSphere(sphere, 1.0f, 100, 100);    // glu.gluSphere(quadricObj,radius,slices,stacks);
        glu.gluDeleteQuadric(sphere);

    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();

        if (height == 0) {
            height = 1;   // prevent divide by zero
        }
        float aspect = (float) width / height;

        // Set the view port (display area) to cover the entire window
        gl.glViewport(0, 0, width, height);

        // Setup perspective projection, with aspect ratio matches viewport
        gl.glMatrixMode(GL_PROJECTION);  // choose projection matrix
        gl.glLoadIdentity();             // reset projection matrix
        glu.gluPerspective(45.0f, aspect, 1.0f, 200.0); // fovy, aspect, zNear, zFar

        // Enable the model-view transform
        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity(); // reset*/
    }

    /**
     * ENABLED KEYBOARD EVENTS AND ROLLING SIMULATION
     *
     * @param e
     */
    @Override
    public void keyPressed(java.awt.event.KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_LEFT:
                rotationalAngleZ += ROTATIONAL_ANGLE_INCREMENT; //CLOCK-WISE ROTATION OVER Z-AXIS
                xCord -= DISPLACING_DISTANCE;
                break;
            case KeyEvent.VK_RIGHT:
                rotationalAngleZ -= ROTATIONAL_ANGLE_INCREMENT; //ANTI-CLOCK-WISE ROTATION OVER Z-AXIS
                xCord += DISPLACING_DISTANCE;
                break;
            case KeyEvent.VK_DOWN:
                rotationalAngleX += ROTATIONAL_ANGLE_INCREMENT; // ANTI-CLOCK-WISE ROTATION OVER X-AXIS
                zCord += DISPLACING_DISTANCE;
                break;
            case KeyEvent.VK_UP:
                rotationalAngleX -= ROTATIONAL_ANGLE_INCREMENT; //CLOCK-WISE ROTATION OVER X-AXIS
                zCord -= DISPLACING_DISTANCE;
                break;
            case KeyEvent.VK_PAGE_UP:
                rotationalAngleX += ROTATIONAL_ANGLE_INCREMENT; //CLOCK-WISE ROTATION OVER X-AXIS
                break;
            case KeyEvent.VK_BACK_SPACE:
                System.exit(0);
                break;
            case KeyEvent.VK_R:
                dlr = 1.0f; //change light to red
                dlg = 0.0f;
                dlb = 0.0f;
                System.out.println("CHANGED TO RED");
                break;
            case KeyEvent.VK_G:
                dlr = 0.0f; //change light to green
                dlg = 1.0f;
                dlb = 0.0f;
                break;
            case KeyEvent.VK_B:
                dlr = 0.0f; //change light to blue
                dlg = 0.0f;
                dlb = 1.0f;
                break;
            case KeyEvent.VK_W:
                lpy += 2.0f; //change light to red

                break;
            case KeyEvent.VK_S:
                lpy -= 2.0f; //change light to red
                break;
            case KeyEvent.VK_A:
                lpx -= 2.0f; //change light to red
                break;
            case KeyEvent.VK_D:
                lpx += 2.0f; //change light to red
                break;
            case KeyEvent.VK_M:
                zoom += ZOOM_FACTOR;
                break;
            case KeyEvent.VK_N:
                zoom -= ZOOM_FACTOR;
                break;
            default:
                break;
        }

    }

    @Override
    public void keyReleased(java.awt.event.KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyTyped(java.awt.event.KeyEvent e) {
        // TODO Auto-generated method stub

    }

    public static float RGBToFloat(int number) {
        return (float) (1 / number);
    }

    public static FloatBuffer colorToFloatBuffer(Color color) {
        return FloatBuffer.wrap(new float[]{RGBToFloat(color.getRed()), RGBToFloat(color.getGreen()), RGBToFloat(color.getBlue())}, 3, 0);
    }

}
