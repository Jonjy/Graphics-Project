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
import org.lwjgl.opengl.GLContext;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.Sys;
import org.lwjgl.util.glu.GLU;
import java.nio.FloatBuffer;
import org.lwjgl.util.vector.Vector3f;


public class CubeWorld {
    double sumLength=0;
    public static Chunk assHatt; //this needs to be a class variable , because reasons
    private FPCameraController fp = new FPCameraController(0f,0f,0f, false);
    private DisplayMode displayMode;
    private FloatBuffer lightPosition;
    private FloatBuffer whiteLight;
    private FloatBuffer redLight;
    private FloatBuffer diffuseLight;
    private int lightMode = 0; //0 Equals no light // 1 Equals White //2 Equals Red
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
            if (d[i].getWidth() == 1024
                    && d[i].getHeight() == 768
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
        initLightArrays();
        glClearColor(0.0f, 0.8f, 0.9f, 0.0f);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition);
        glLight(GL_LIGHT1, GL_POSITION, lightPosition);
        GLU.gluPerspective(100.0f, (float) displayMode.getWidth() / (float) displayMode.getHeight(), 0.1f, 300.0f);
        glMatrixMode(GL_MODELVIEW);
        glShadeModel(GL_SMOOTH);
        glLightModelf(GL_LIGHT_MODEL_LOCAL_VIEWER, 1.0f);
        glLightModelf(GL_LIGHT_MODEL_TWO_SIDE, 1.0f);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
        glEnable(GL_TEXTURE_2D);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        glEnableClientState(GL_NORMAL_ARRAY); //this line was soooooo important
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_NORMALIZE);
        glEnable(GL_BLEND);
        glBlendFunc (GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glLight(GL_LIGHT0,GL_SPECULAR, whiteLight);
        glLight(GL_LIGHT0, GL_DIFFUSE, whiteLight);
        glLight(GL_LIGHT0, GL_AMBIENT, whiteLight);
        glLight(GL_LIGHT1,GL_SPECULAR, redLight);
        glLight(GL_LIGHT1, GL_DIFFUSE, redLight);
        glLight(GL_LIGHT1, GL_AMBIENT, whiteLight);
        glLightf(GL_LIGHT1, GL_SPOT_CUTOFF, 45.0f); 
        glLightf(GL_LIGHT1, GL_SPOT_EXPONENT, 4.0f);
        glLightf(GL_LIGHT0, GL_SPOT_CUTOFF, 60.0f); 
        glLightf(GL_LIGHT0, GL_SPOT_EXPONENT, 4.0f);
        glEnable(GL_LIGHTING);
        glEnable(GL_LIGHT0);
        lightMode = 1;

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
        lightPosition.put(0f).put(0f).put(0f).put(1f).flip();
        
        
        whiteLight = BufferUtils.createFloatBuffer(4);
        whiteLight.put(1.0f).put(1.0f).put(1.0f).put(0.0f).flip();
        
        redLight = BufferUtils.createFloatBuffer(4);
        redLight.put(1.0f).put(0.0f).put(1.0f).put(1.0f).flip();
        
        
        
        diffuseLight = BufferUtils.createFloatBuffer(4);
        diffuseLight.put(.5f).put(.5f).put(.5f).put(1.0f).flip();
    }
    
    /**
     * method:gameLoop 
     * purpose: runs the camera calls the render method
     */
    public void gameLoop() throws Exception {
        FPCameraController camera = new FPCameraController(-50,-20,-50, false);
        double tick = 0;
        float dx = 0.0f;
        float dy = 0.0f;
        float dt = 0.0f; //length of frame
        long time;
        float lastTime = 0.0f; // when the last frame was
        
        float mouseSensitivity = 0.09f;
        float movementSpeed = .35f;
        
        
        //hide the mouse
        Mouse.setGrabbed(true);
        assHatt = new Chunk(0,0,0);
        // keep looping till the display window is closed the ESC key is down
        while (!Display.isCloseRequested()
                && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            time = Sys.getTime();
            tick++;
            if (tick == 50){
                System.out.println("X: "+ camera.position.x + " Y:"+ camera.position.y + " Z:"+ camera.position.z);
                System.out.println("X: "+ Math.floor((-camera.position.x+1)/2) + " Y:"+Math.floor((-camera.position.y+1)/2) + " Z:"+ Math.floor((-(camera.position.z-2))/2));
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
            while(Keyboard.next()){
                if (Keyboard.getEventKey() == Keyboard.KEY_F){
                    if(!Keyboard.getEventKeyState()){
                        if (lightMode == 0){
                            lightMode++;
                            glEnable(GL_LIGHT0);
                        }
                        else if (lightMode == 1){
                            lightMode++;
                            glDisable(GL_LIGHT0);
                            glEnable(GL_LIGHT1);
                        }
                        else if (lightMode ==2){
                            lightMode = 0;
                            glDisable(GL_LIGHT1);
                        }
                    }
                }
                if(Keyboard.getEventKey() == Keyboard.KEY_P){
                    if(!Keyboard.getEventKeyState())
                        camera.toggleNoClip();
                }
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
