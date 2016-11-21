/***************************************************************
* file: FPCameraController.java
* author: Jonathan Little, Andre Le, Phil Sloan
* class: CS 445 - Computer Graphics
*
* assignment: CheckPoint 1
* date last modified: 11/1/2016
*
* purpose: Run the program
* 
****************************************************************/
package cube.world;



import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.Sys;
import org.lwjgl.util.glu.GLU;
import java.nio.FloatBuffer;


public class CubeWorld {

    private FPCameraController fp = new FPCameraController(0f,0f,0f);
    private DisplayMode displayMode;
    private FloatBuffer lightPosition;
    private FloatBuffer whiteLight;
    private FloatBuffer diffuseLight;
    /**
     * method: start
     * purpose: calls the methods to make the window, start openGL
     * and render the graphics
     */
    public void start(){
        try {
            createWindow();
            initGL();
            gameLoop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * method: createWindow
     * purpose: sets up the parameters of the window we will be drawing in  
     */
    private void createWindow() throws Exception{
        Display.setFullscreen(false);
        DisplayMode d[] = Display.getAvailableDisplayModes();
        for (int i = 0; i < d.length; i++) {
            if (d[i].getWidth() == 640
                    && d[i].getHeight() == 480
                    && d[i].getBitsPerPixel() == 32) {
                displayMode = d[i];
                break;
            }
        }
        Display.setDisplayMode(displayMode);
        Display.setTitle("Cube World");
        Display.create();

    }

    /**
     * method:initGL 
     * purpose: sets up Open GL
     */
    private void initGL() {
        
        glClearColor(0.0f, 0.8f, 0.9f, 0.0f);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        initLightArrays();
        GLU.gluPerspective(100.0f, (float) displayMode.getWidth() / (float) displayMode.getHeight(), 0.1f, 300.0f);
        glMatrixMode(GL_MODELVIEW);
        glShadeModel(GL_SMOOTH);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
        glEnable(GL_TEXTURE_2D);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_NORMALIZE);
        glLight(GL_LIGHT0, GL_POSITION, lightPosition);
        //glLight(GL_LIGHT0, GL_SPOT_DIRECTION, (FloatBuffer)BufferUtils.createFloatBuffer(4).put(new float[]{0,-0.3f,-1.0f,0}).flip());
        glLight(GL_LIGHT0,GL_SPECULAR, whiteLight);
        glLight(GL_LIGHT0, GL_DIFFUSE, whiteLight);
        glLight(GL_LIGHT0, GL_AMBIENT, diffuseLight);
        glLightf(GL_LIGHT0, GL_SPOT_CUTOFF, 45f); 
        glLightf(GL_LIGHT0, GL_SPOT_EXPONENT, 2.0f);
        glEnable(GL_LIGHTING);
        glEnable(GL_LIGHT0);

    }

    /**
     * method: render
     * purpose: call the objects to draw
     */
    private void render(){  
        try {
            
            
        } catch (Exception e) {
        }
    }
    
    private void initLightArrays(){
        lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(0f).put(0f).put(2f).put(1f).flip();
        
        
        whiteLight = BufferUtils.createFloatBuffer(4);
        whiteLight.put(1.0f).put(1.0f).put(1.0f).put(0.0f).flip();
        
        diffuseLight = BufferUtils.createFloatBuffer(4);
        diffuseLight.put(.5f).put(.5f).put(.5f).put(1.0f).flip();
    }
    
    /**
     * method:gameLoop 
     * purpose: runs the camera calls the render method
     */
    public void gameLoop() throws Exception {
        FPCameraController camera = new FPCameraController(-30,-40, -30);
        double tick = 0;
        float dx = 0.0f;
        float dy = 0.0f;
        float dt = 0.0f; //length of frame
        float lastTime = 0.0f; // when the last frame was
        long time = 0;
        float mouseSensitivity = 0.09f;
        float movementSpeed = .35f;
        
        //hide the mouse
        Mouse.setGrabbed(true);
        Chunk assHatt = new Chunk(0,0,0);
        // keep looping till the display window is closed the ESC key is down
        while (!Display.isCloseRequested()
                && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            time = Sys.getTime();
            tick++;
            if (tick == 50){
                System.out.println("X: "+ camera.position.x + " Y:"+ camera.position.y + " Z:"+ camera.position.z);
                tick = 0;
            }
            lastTime = time;
            //distance in mouse movement
            //from the last getDX() call.
            dx = Mouse.getDX();
            //distance in mouse movement
            //from the last getDY() call.
            dy = Mouse.getDY();
            //controll camera yaw from x movement fromt the mouse
            camera.yaw(dx * mouseSensitivity);
            //controll camera pitch from y movement fromt the mouse
            camera.pitch(dy * mouseSensitivity);
            //when passing in the distance to move
            //we times the movementSpeed with dt this is a time scale
            //so if its a slow frame u move more then a fast frame
            //so on a slow computer you move just as fast as on a fast computer
            if (Keyboard.isKeyDown(Keyboard.KEY_W))//move forward
            {
                camera.walkForward(movementSpeed);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_S))//move backwards
            {
                camera.walkBackwards(movementSpeed);
            }
            
            if (Keyboard.isKeyDown(Keyboard.KEY_A))//strafe left {
            {
                camera.strafeLeft(movementSpeed);
            }       
            if (Keyboard.isKeyDown(Keyboard.KEY_D))//strafe right {
            {
                camera.strafeRight(movementSpeed);
            }
            
            if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))//move up {
            {
                camera.moveUp(movementSpeed);
            }

            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                camera.moveDown(movementSpeed);
            }
            //set the modelview matrix back to the identity
            glLoadIdentity();
            //look through the camera before you draw anything
            camera.lookThrough();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            //you would draw your scene here.
            
            assHatt.render();
            //draw the buffer to the screen
            Display.update();
            Display.sync(60);
        }

    Display.destroy ();
    }
        
    /**
     * method:main 
     * purpose: provide an entry point for the program
     */
    public static void main(String[] args) {
        CubeWorld Runner = new CubeWorld();
        Runner.start();
    }
    
}
