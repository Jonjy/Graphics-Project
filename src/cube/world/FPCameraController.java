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
    public boolean noClip ;
    public Vector3f newPosition = null;
    private Vector3f velocity;
    private Vector3f lDirection = null;
    private Vector3f lPosition = null;
    //the rotation around the Y axis of the camera
    public float yaw = 0.0f;
    //the rotation around the X axis of the camera
    private float pitch = 0.0f;
    private Vector3Float me;
    FloatBuffer lightPosition;
    FloatBuffer lightDirection;
    
    
    
    public FPCameraController(float x, float y, float z, boolean noClip) {
    //instantiate position Vector3f to the x y z params.
        position = new Vector3f(x, y, z);
        velocity = new Vector3f(0,0,0);
        newPosition = new Vector3f(x,y,z);
        lPosition = new Vector3f(x, y, z);
        lDirection = new Vector3f(0,0 ,-1.0f);
        lPosition.x = 0.0f;
        lPosition.y= 15.0f;
        lPosition.z=0.0f;
        this.noClip = noClip;
       
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
    
    public void toggleNoClip(){
        if(noClip){
            noClip=false;
            System.out.println(" NO CLIPPING DISABLED!!!");
        }
        else{
            noClip=true;
            System.out.println(" NO CLIPPING ENABLED!!!");
        }
    }
    
   
    
    public void walkForward(float distance) {
        Vector3f direction = new Vector3f(0,0,0);
        direction.x = distance * (float) -Math.sin(Math.toRadians(yaw));
        System.out.println(direction.x);
        direction.z = distance * (float) Math.cos(Math.toRadians(yaw));
        Collision collision = Collision.checkCollision(new Vector3f(position.x,position.y,position.z),direction,noClip);
        
        if(collision.getType()!=Collision.CollisionType.NONE){
            System.out.println(" Collision Type is :" +collision.getNormal());
        }
        position.x+= direction.x +(-collision.getNormal().x*direction.x);
        position.z+= direction.z +(-collision.getNormal().z*direction.z);
       
        updateLight();
    }
    
    public void walkBackwards(float distance) {
         Vector3f direction = new Vector3f(0,0,0);
        direction.x = distance * (float)  Math.sin(Math.toRadians(yaw));
        direction.z = distance * (float) -Math.cos(Math.toRadians(yaw));
        Collision collision = Collision.checkCollision(new Vector3f(position.x,position.y,position.z),direction,noClip);
       
        if(collision.getType()!=Collision.CollisionType.NONE){
            System.out.println(" Collision Type is :" +collision.getType());
        }
        position.x+= direction.x +(-collision.getNormal().x*direction.x);
        position.z+= direction.z +(-collision.getNormal().z*direction.z);
        
        
        updateLight();
    }
    
    //strafes the camera left relative to its current rotation (yaw)
    public void strafeLeft(float distance) {
        Vector3f direction = new Vector3f(0,0,0);
        direction.x = distance * (float) -Math.sin(Math.toRadians(yaw-90));
        direction.z = distance * (float) Math.cos(Math.toRadians(yaw-90));
        Collision collision = Collision.checkCollision(new Vector3f(position.x,position.y,position.z),direction,noClip);
     
        position.x+= direction.x +(-collision.getNormal().x*direction.x);
        position.z+= direction.z +(-collision.getNormal().z*direction.z);
       
        
        updateLight();
    }
    
    //strafes the camera right relative to its current rotation (yaw)
    public void strafeRight(float distance) {
        Vector3f direction = new Vector3f(0,0,0);
        direction.x = distance * (float) -Math.sin(Math.toRadians(yaw+90));
        direction.z = distance * (float) Math.cos(Math.toRadians(yaw+90));
        Collision collision = Collision.checkCollision(new Vector3f(position.x,position.y,position.z),direction,noClip);
        
        position.x+= direction.x +(-collision.getNormal().x*direction.x);
        position.z+= direction.z +(-collision.getNormal().z*direction.z);
        
        
        updateLight();
    }
    
    //moves the camera up relative to its current rotation (yaw)
    public void moveUp(float distance) {
        Vector3f direction = new Vector3f(0,0,0);
        direction.y = distance;
        Collision collision = Collision.checkCollision(new Vector3f(position.x,position.y,position.z),direction,noClip);
        
        position.y -= distance -(collision.getNormal().y*distance);    
        
        updateLight();
    }
    //moves the camera down
    public void setVerticalVelocity(float velocity){
        this.velocity.y = velocity;
    }
    
    public void applyGravity(){
        moveDown(velocity.y);
        if (velocity.y < 2)
            velocity.y += .1f;
    }

    public void moveDown(float distance) {
        Vector3f direction = new Vector3f(0,0,0);
        direction.y = distance;
        Collision collision = Collision.checkCollision(new Vector3f(position.x,position.y,position.z),direction,noClip);
        
        position.y += distance -(-collision.getNormal().y*distance);   
       
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
        glLight(GL_LIGHT0, GL_SPOT_DIRECTION, lightDirection); 
        glLight(GL_LIGHT1, GL_SPOT_DIRECTION, lightDirection); 
        
        
    }
    
    
}
