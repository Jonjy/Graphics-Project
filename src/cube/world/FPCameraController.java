/***************************************************************
* file: FPCameraController.java
* author: Jonathan Little
* class: CS 445 - Computer Graphics
*
* assignment: CheckPoint 1
* date last modified: 11/1/2016
*
* purpose: Creates a basic 3D camera with simple controls 
* 
****************************************************************/ 
package cube.world;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.Sys;
import org.lwjgl.opengl.GLContext;


public class FPCameraController {
    //3d vector to store the camera's position in

    public Vector3f position = null;
    private Vector3f lDirection = null;
    private Vector3f lPosition = null;
    //the rotation around the Y axis of the camera
    private float yaw = 0.0f;
    //the rotation around the X axis of the camera
    private float pitch = 0.0f;
    private Vector3Float me;
    FloatBuffer lightPosition;
    FloatBuffer lightDirection;
    
    
    
    public FPCameraController(float x, float y, float z) {
    //instantiate position Vector3f to the x y z params.
        position = new Vector3f(x, y, z);
        lPosition = new Vector3f(x, y, z);
        lDirection = new Vector3f(0,0 ,-1.0f);
        lPosition.x = 0.0f;
        lPosition.y= 15.0f;
        lPosition.z=0.0f;
       
    }
    
    //increment the camera's current yaw rotation
    public void yaw(float amount) {
    //increment the yaw by the amount param
        yaw += amount;
        yaw %= 360;
        updateLight();
    }

    
    public void pitch(float amount) {
    //increment the pitch by the amount param
        pitch -= amount;
        pitch %=360; 
        updateLight();
    }
    
    public void walkForward(float distance) {
        lightPosition = BufferUtils.createFloatBuffer(4);
        float xOffset = distance * (float) Math.sin(Math.toRadians(yaw));
        float zOffset = distance * (float) Math.cos(Math.toRadians(yaw));
        position.x -= xOffset;
        position.z += zOffset;
        updateLight();
    }
    
    public void walkBackwards(float distance) {
        lightPosition = BufferUtils.createFloatBuffer(4);
        float xOffset = distance * (float) Math.sin(Math.toRadians(yaw));
        float zOffset = distance * (float) Math.cos(Math.toRadians(yaw));
        position.x += xOffset;
        position.z -= zOffset;
        updateLight();
    }
    
    //strafes the camera left relative to its current rotation (yaw)
    public void strafeLeft(float distance) {
        lightPosition = BufferUtils.createFloatBuffer(4);
        float xOffset = distance * (float) Math.sin(Math.toRadians(yaw - 90));
        float zOffset = distance * (float) Math.cos(Math.toRadians(yaw - 90));
        position.x -= xOffset;
        position.z += zOffset;
        updateLight();
    }
    
    //strafes the camera right relative to its current rotation (yaw)
    public void strafeRight(float distance) {
        lightPosition = BufferUtils.createFloatBuffer(4);
        float xOffset = distance * (float) Math.sin(Math.toRadians(yaw + 90));
        float zOffset = distance * (float) Math.cos(Math.toRadians(yaw + 90));
        position.x -= xOffset;
        position.z += zOffset;
        updateLight();
    }
    
    //moves the camera up relative to its current rotation (yaw)
    public void moveUp(float distance) {
        lightPosition = BufferUtils.createFloatBuffer(4);
        position.y -= distance;
        updateLight();
    }
    //moves the camera down

    public void moveDown(float distance) {
        lightPosition = BufferUtils.createFloatBuffer(4);
        position.y += distance;
        updateLight();
    }
    
    //translates and rotate the matrix so that it looks through the camera
    //this does basically what gluLookAt() does
    int tick;
    
    public void lookThrough() {
        tick++;
        
        //updateLight();
       
        //glLight(GL_LIGHT0, GL_SPOT_DIRECTION, lightDirection);
        //roatate the pitch around the X axis
        glRotatef(pitch, 1.0f, 0.0f, 0.0f);
        //roatate the yaw around the Y axis
        glRotatef(yaw, 0.0f, 1.0f, 0.0f);
        //translate to the position vector's location
        glTranslatef(position.x, position.y, position.z);
        //lightPosition.put(0.0f).put(0.0f).put(2.0f).put(1.0f).flip();
        //glLight(GL_LIGHT0, GL_POSITION, lightPosition);
        
        updateLight();
        
        
        if (tick%100==0){
            System.out.println("Light X:" + position.x + "y: "+ position.y +" z" +position.z);
            System.out.println("yaw : "+ yaw + "pitch :" + pitch);
            System.out.println("LDIR : " + lDirection.toString());
        }
        
        
    }   
    
    
    /**
     * This method is a helper to avoid repeating the same code everywhere
     * Makes the thing a little bit more readable.  This will update the light's
     * position and more importantly, it's direction.
     * 
     * The GL_SPOT_DIRECETION attributes take in a normal vector in the direction
     * the light is pointing.  This is calculated by applying rotation matricies
     * using the yaw and the pitch about the Y and X axis, respectively.
     */
    public void updateLight(){
        //FLOATS
        Float cosYaw, cosPitch, sinYaw, sinPitch;
        
        cosYaw = (float)Math.cos(Math.toRadians(yaw));
        cosPitch = (float)Math.cos(Math.toRadians(pitch));
        sinYaw = (float)Math.sin(Math.toRadians(yaw));
        sinPitch = (float)Math.sin(Math.toRadians(pitch));
        
        
        lDirection.x =  cosPitch*sinYaw ;
        lDirection.y = -sinPitch;
        lDirection.z = -cosPitch*cosYaw; 
        lDirection.normalise();
        
        
        lightDirection = BufferUtils.createFloatBuffer(4);  //the fourth will be 0, it's not used.
        lightDirection.put(lDirection.x).put(lDirection.y).put(lDirection.z).put(0.0f).flip();
        
        lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(position.x).put(position.y).put(position.z).put(1.0f).flip();
        //glLight(GL_LIGHT0, GL_POSITION, lightPosition);
        glLight(GL_LIGHT0, GL_SPOT_DIRECTION, lightDirection); 
        
        
        
    }
    
    
}
