package models;

import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_TRIANGLES;
import com.jogamp.opengl.GL2;
import static com.jogamp.opengl.GL2ES3.GL_QUADS;
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
import com.jogamp.opengl.math.VectorUtil;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import java.nio.FloatBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author Gabriel Huertas
 */
public class JOGLTest extends JFrame implements GLEventListener, KeyListener {

    // JOGL ATTRIBUTES
    private GLU glu;
    private GLUT glut;
    private Texture texture1;
    private Texture texture2;
    private GLProfile glProfile;

    // OBJECTS 3D POSITION VARIABLES
    private float rotationalAngleX;
    private float rotationalAngleY;
    private float rotationalAngleZ;
    private float posX;
    private float posY;
    private float zoom;

    // AMBIENT FEATURES 
    private float dlr;
    private float dlg;
    private float dlb;
    private float lpx;
    private float lpy;
    private float lpz;

    // AMBIENT CONSTANTS
    private static final float MAX_ANGLE = 90;
    private static final float ROTATIONAL_ANGLE_INCREMENT = 5.0f; // rotational angle in degree for pyramid
    private static final float ZOOM_FACTOR = 2.5f;
    private static final float DISPLACING_DISTANCE = 0.05f;
    private static final float GRAVITY_ACCELERATION = -9.8f; // -9.8m/s^2 towards the negative y axis
    private static final float MASS = 0.1f; // Equal to 1 Kg
    //TIME STAMP FOR THE START OF TIME IN THE VIRTUAL WORLD
    private static long INITIAL_TIME;

    // TIME VARIABLES
    private float timestampZero;
    private float timestampOne;
    private float dt; // Differential time between timestamp1 and timestamp0

    // BALL ATTRIBUTES
    private float sphereRadius;
    private float positionX;
    private float positionY;
    private float velocityX;
    private float velocityY;
    private float accelerationX;
    private float accelerationY;
    private float angularAcceleration;
    private float angularVelocity;
    private float rotationalAngle;

    public JOGLTest() {
        rotationalAngleX = 0;
        rotationalAngle = 0;

        zoom = -40;
        sphereRadius = 1.0f;
        posX = 1.0f;
        posY = 1.0f;
        dlb = 1.0f;
        dlg = 1.0f;
        dlr = 1.0f;
        timestampZero = 0;
        timestampOne = 0;
        velocityX = 0; // 0.5 METERS PER SECOND
        velocityY = 0;
        positionX = 0; //POSITION OVER THE X AXIS
        positionY = 0;
        accelerationX = 0;
        accelerationY = 0;

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
        addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                System.exit(0);
            }

            public void windowClosing(WindowEvent e) {
                windowClosed(e);
            }
        });
        glCanvas.requestFocusInWindow();
        setVisible(true);

        final FPSAnimator animator = new FPSAnimator(glCanvas, 300, true);
        animator.start();
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        INITIAL_TIME = System.nanoTime();
        final GL2 gl = drawable.getGL().getGL2();
        glu = new GLU();
        glut = new GLUT();
        gl.glEnable(GL2.GL_TEXTURE_2D);
        gl.glShadeModel(GL2.GL_SMOOTH);
        gl.glClearColor(0f, 0f, 0f, 0f);

        gl.glClearDepth(1.0f);
        gl.glEnable(GL2.GL_DEPTH_TEST);
        /* gl.glEnable(GL_LIGHTING); //enable lighting
        gl.glEnable(GL_LIGHT0); //enable LIGHT0 for diffuse light
        gl.glEnable(GL_LIGHT1); //enable LIGHT1 for Ambient Light*/
        gl.glDepthFunc(GL2.GL_LEQUAL);
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);

        //gl.glLightfv(HEIGHT, WIDTH, params);
        try {
            TextureData textureData1 = TextureIO.newTextureData(glProfile, getClass().getResource("/util/earth.jpg"), false, TextureIO.JPG);
            texture1 = TextureIO.newTexture(textureData1);
            TextureData textureData2 = TextureIO.newTextureData(glProfile, getClass().getResource("/util/stoneGridTexture.jpg"), false, TextureIO.JPG);
            texture2 = TextureIO.newTexture(textureData2);

        } catch (IOException ex) {
            Logger.getLogger(JOGLTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }

    public Texture loadTexture(String filePath) throws Exception {
        Texture texture = null;
        File file = new File(filePath);
        if (!file.exists()) {
            throw new Exception("File wasn´t found");
        } else {
            texture = TextureIO.newTexture(file, true);
        }
        return texture;
    }

    @Override
    public void display(GLAutoDrawable drawable) {

        setTimestampZero((float) ((System.nanoTime() - INITIAL_TIME) / 1E9));  // CALCULATE TIME IN SECONDS FROM THE BEGINNING
        setDt(timestampZero - timestampOne);  //SET INTERVAL BETWEEN TIMESTAMP ONE AND TIMESTIME ZERO
        setTimestampOne(timestampZero);

        System.out.println("Angle is: " + rotationalAngleZ + " Acceleration in X axis is " + GRAVITY_ACCELERATION * sin(Math.toRadians(rotationalAngleZ)) + ", Acceleration in Y axis is " + GRAVITY_ACCELERATION * cos(Math.toRadians(rotationalAngleZ)));
        accelerationX = (float) (GRAVITY_ACCELERATION * sin(Math.toRadians(rotationalAngleZ)));
        accelerationY = (float) (GRAVITY_ACCELERATION * cos(Math.toRadians(rotationalAngleZ)));

        velocityX += accelerationX * dt;
        velocityY += accelerationY * dt;
        posX += velocityX * dt;
        if (posY > 1 && posY <= 100) {
            posY += velocityY * dt;
        }

        angularAcceleration += (float) (GRAVITY_ACCELERATION * sin(Math.toRadians(rotationalAngleZ)) / sphereRadius * (1 + (2 / 5)));
        System.out.println("Angular acceleration = " + angularAcceleration);
        System.out.println("Position of the ball " + posX + ", " + posY);
        angularVelocity += angularAcceleration * dt;
        rotationalAngle -= angularVelocity * dt;

        final GL2 gl = drawable.getGL().getGL2();

        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear color and depth buffers

        gl.glLoadIdentity();  // reset the model-view matrix


        /*
        * POPULATE LIGHTS VERTEX DATA
         */
 /*  float ambientLight[] = {0.5f, 0.5f, 0.5f, 1.0f}; //Halfway between complete darkness (0.0f) and maximum shinness (1.0f)
        float diffuseLight[] = {dlr, dlg, dlb, 1.0f};
        float lightPosition[] = {lpx, lpy, lpz, 1.0f}; // Locate light source 2.0f units into the screen (Positive z-axis)

        gl.glLightfv(GL_LIGHT0, GL_DIFFUSE, FloatBuffer.wrap(diffuseLight));
        gl.glLightfv(GL_LIGHT1, GL_AMBIENT, FloatBuffer.wrap(ambientLight));
        gl.glLightfv(GL_LIGHT0, GL_POSITION, FloatBuffer.wrap(lightPosition));

        // glu.gluLookAt(0.0, 0.0, 5.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);*/
        // ----- Render the Pyramid -----
        gl.glPushMatrix();
        gl.glTranslatef(-1.5f, 0.0f, -6.0f); // translate left and into the screen

        //gl.glRotatef(rotationalAngleX, 1.0f, 0.0f, 0.0f); // rotate about the x-axis
        //gl.glRotatef(rotationalAngleY, 0.0f, 1.0f, 0.0f); // rotate about the y-axis
        //gl.glRotatef(rotationalAngleZ, 0.0f, 0.0f, 1.0f); // rotate about the z-axis
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

        gl.glFlush();
        gl.glEnd(); // of the pyramid
        gl.glPopMatrix();

        //START DRAWING SOLID SPHERE
        gl.glLoadIdentity(); // reset the current model-view matrix
        gl.glPushMatrix();
        gl.glTranslatef(posX, posY, zoom);
        gl.glRotatef((float) rotationalAngle, 0.0f, 0.0f, 1.0f);
        //NORMALIZE RGB COLORS FOR BETTER TEXTURE DISPLAY
        gl.glColor3f(1.0f, 1.0f, 1.0f);

        texture1.enable(gl);
        texture1.bind(gl);

        GLUquadric sphere = glu.gluNewQuadric();
        glu.gluQuadricTexture(sphere, true);
        glu.gluQuadricDrawStyle(sphere, GLU.GLU_FILL);
        glu.gluQuadricNormals(sphere, GLU.GLU_FLAT);
        glu.gluQuadricOrientation(sphere, GLU.GLU_OUTSIDE);

        glu.gluSphere(sphere, sphereRadius, 100, 100);    // glu.gluSphere(quadricObj,radius,slices,stacks);
        glu.gluDeleteQuadric(sphere);
        gl.glPopMatrix();

        gl.glPushMatrix();
        texture2.enable(gl);
        texture2.bind(gl);
        gl.glTranslatef(-sphereRadius, -sphereRadius, zoom);
        gl.glRotatef(rotationalAngleX, 1.0f, 0.0f, 0.0f); // rotate about the x-axis
        gl.glRotatef(rotationalAngleY, 0.0f, 1.0f, 0.0f); // rotate about the y-axis
        gl.glRotatef(rotationalAngleZ, 0.0f, 0.0f, 1.0f); // rotate about the z-axis
        drawGround(20, 20, gl);
        gl.glPopMatrix();

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
        glu.gluPerspective(45.0f, aspect, 1.0f, 600.0f); // fovy, aspect, zNear, zFar

        // Enable the model-view transform
        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity(); // reset*/
    }

    //=============== CLASS METHODS ====================
    public void loadTextures() {

    }

    /**
     *
     * @param width
     * @param depth
     * @param gl
     */
    public void drawGround(int width, int depth, GL2 gl) {
        for (int i = 0; i < depth; i++) {
            for (int j = 0; j < width; j++) {
                drawGrid(j, i, gl);
            }
            gl.glTranslatef(-2 * width, 0.0f, -2.0f);
        }

    }

    /**
     * DRAW A SINGLE GRID GIVEN X AND Z CORDINATES
     *
     */
    public void drawGrid(float x, float z, GL2 gl) {
        gl.glTranslatef(2.0f, 0.0f, 0.0f);
        gl.glBegin(GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, 1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, 1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, 1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, 1.0f);

        // Back Face
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);

        // Top Face
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, 1.0f, 1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, 1.0f, 1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);

        // Bottom Face
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, 1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, 1.0f);

        // Right face
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, 1.0f);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, 1.0f);

        // Left Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, 1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, 1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);

        gl.glEnd(); // Done Drawing The Quad
        gl.glFlush();
    }

    public static float RGBToFloat(int number) {
        return (float) (1 / number);
    }

    public static FloatBuffer colorToFloatBuffer(Color color) {
        return FloatBuffer.wrap(new float[]{RGBToFloat(color.getRed()), RGBToFloat(color.getGreen()), RGBToFloat(color.getBlue())}, 3, 0);
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
            case KeyEvent.VK_RIGHT:
                if (Math.abs(rotationalAngleZ) < 90) {
                    rotationalAngleZ += ROTATIONAL_ANGLE_INCREMENT;
                }
                break;
            case KeyEvent.VK_LEFT:
                if (Math.abs(rotationalAngleZ) < 90) {
                    rotationalAngleZ -= ROTATIONAL_ANGLE_INCREMENT;
                }
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

    //============== GETTERS AND SETTERS ================
    public double getTimestampZero() {
        return timestampZero;
    }

    public void setTimestampZero(float timestampZero) {
        this.timestampZero = timestampZero;
    }

    public double getTimestampOne() {
        return timestampOne;
    }

    public void setTimestampOne(float timestampOne) {
        this.timestampOne = timestampOne;
    }

    public double getDt() {
        return dt;
    }

    public void setDt(float dt) {
        this.dt = dt;
    }

}
