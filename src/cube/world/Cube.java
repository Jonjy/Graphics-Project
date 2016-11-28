/***************************************************************
* file: FPCameraController.java
* author: Jonathan Little, Andre Le 
* class: CS 445 - Computer Graphics
*
* assignment: CheckPoint 1
* date last modified: 11/1/2016
*
* purpose: Draws a multicolored cube
* 
****************************************************************/ 
package cube.world;

import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LINE_LOOP;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glNormal3f;
import static org.lwjgl.opengl.GL11.glVertex3f;

public class Cube {
    
    private float x, y, z;
    private BlockType Type;    
    private boolean active;
   
    
    public enum BlockType {

        BlockType_Grass(0),
        BlockType_Sand(1),
        BlockType_Water(2),
        BlockType_Dirt(3),
        BlockType_Stone(4),
        BlockType_Bedrock(5),
        BlockType_Default(6);
        
       
        BlockType(int i) {
            BlockID = i;
        }
    
        private int BlockID;

        public int GetID() {
            return BlockID;
        }

        public void SetID(int i) {
            BlockID = i;
        }
    }
    
    public Cube(BlockType type){
        Type = type;
    }
    
    public void setBlockType(BlockType type){
        Type = type;
    }
    
    public void setCoords(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public boolean isActive(){
        return active;
    }
    
    public void SetActive( boolean active){
        this.active=active;
    }
    public int GetID() {
        return Type.GetID();
    }

    
    // old code
    
    public void draw(){
        glEnable(GL_DEPTH_TEST);
        glBegin(GL_QUADS);

        //Top
        
        glColor3f(0.0f, 0.0f, 1.0f);
        glVertex3f(1.0f, 1.0f, -1.0f);
        glVertex3f(-1.0f, 1.0f, -1.0f);
        glVertex3f(-1.0f, 1.0f, 1.0f);
        glVertex3f(1.0f, 1.0f, 1.0f);
        //Bottom
        
        glColor3f(1.0f, 1.0f, 0.0f);
        glVertex3f(1.0f, -1.0f, 1.0f);
        glVertex3f(-1.0f, -1.0f, 1.0f);
        glVertex3f(-1.0f, -1.0f, -1.0f);
        glVertex3f(1.0f, -1.0f, -1.0f);
        //Front
        
        glColor3f(0.0f, 1.0f, 0.0f);
        glVertex3f(1.0f, 1.0f, 1.0f);
        glVertex3f(-1.0f, 1.0f, 1.0f);
        glVertex3f(-1.0f, -1.0f, 1.0f);
        glVertex3f(1.0f, -1.0f, 1.0f);
        //Back
        
        glColor3f(1.0f, 0.0f, 1.0f);
        glVertex3f(1.0f, -1.0f, -1.0f);
        glVertex3f(-1.0f, -1.0f, -1.0f);
        glVertex3f(-1.0f, 1.0f, -1.0f);
        glVertex3f(1.0f, 1.0f, -1.0f);
        //left
       
        glColor3f(1.0f, 0.0f, 0.0f);
        glVertex3f(-1.0f, 1.0f, 1.0f);
        glVertex3f(-1.0f, 1.0f, -1.0f);
        glVertex3f(-1.0f, -1.0f, -1.0f);
        glVertex3f(-1.0f, -1.0f, 1.0f);
        //Right
        
        glColor3f(0.0f, 1.0f, 1.0f);
        glVertex3f(1.0f, 1.0f, -1.0f);
        glVertex3f(1.0f, 1.0f, 1.0f);
        glVertex3f(1.0f, -1.0f, 1.0f);
        glVertex3f(1.0f, -1.0f, -1.0f);

        glEnd();
    }
        
    
        public void draw(float x, float y, float z){
        glEnable(GL_DEPTH_TEST);
        glBegin(GL_QUADS);

        //Top
        glColor3f(0.0f, 0.0f, 1.0f);
        glNormal3f(0.0f, -1.0f,0.0f);
        glVertex3f(x+1.0f, y+1.0f, z-1.0f);
        glVertex3f(x-1.0f, y+1.0f, z-1.0f);
        glVertex3f(x-1.0f, y+1.0f, z+1.0f);
        glVertex3f(x+1.0f, y+1.0f, z+1.0f);
        //Bottom
        glColor3f(1.0f, 1.0f, 0.0f);
        glNormal3f(0.0f,-1.0f,0.0f);
        glVertex3f(x+1.0f, y-1.0f, z+1.0f);
        glVertex3f(x-1.0f, y-1.0f, z+1.0f);
        glVertex3f(x-1.0f, y-1.0f, z-1.0f);
        glVertex3f(x+1.0f, y-1.0f, z-1.0f);
        //Front
        glColor3f(0.0f, 1.0f, 0.0f);
        glNormal3f(0.0f,0.0f,1.0f);
        glVertex3f(x+1.0f, y+1.0f, z+1.0f);
        glVertex3f(x-1.0f, y+1.0f, z+1.0f);
        glVertex3f(x-1.0f, y-1.0f, z+1.0f);
        glVertex3f(x+1.0f, y-1.0f, z+1.0f);
        //Back
        glColor3f(1.0f, 0.0f, 1.0f);
        glNormal3f(0.0f,0.0f,-1.0f);
        glVertex3f(x+1.0f, y-1.0f, z-1.0f);
        glVertex3f(x-1.0f, y-1.0f, z-1.0f);
        glVertex3f(x-1.0f, y+1.0f, z-1.0f);
        glVertex3f(x+1.0f, y+1.0f, z-1.0f);
        //left
        glColor3f(1.0f, 0.0f, 0.0f);
        glNormal3f(-1.0f,0.0f,0.0f);
        glVertex3f(x-1.0f, y+1.0f, z+1.0f);
        glVertex3f(x-1.0f, y+1.0f, z-1.0f);
        glVertex3f(x-1.0f, y-1.0f, z-1.0f);
        glVertex3f(x-1.0f, y-1.0f, z+1.0f);
        //Right
        glColor3f(0.0f, 1.0f, 1.0f);
        glNormal3f(1.0f,0.0f,0.0f);
        glVertex3f(x+1.0f, y+1.0f, z-1.0f);
        glVertex3f(x+1.0f, y+1.0f, z+1.0f);
        glVertex3f(x+1.0f, y-1.0f, z+1.0f);
        glVertex3f(x+1.0f, y-1.0f, z-1.0f);

        glEnd();   
    }
    
    public void traceEdge(){
        glBegin(GL_LINE_LOOP);
        //left
        glNormal3f(-1.0f,0.0f,0.0f);
        glVertex3f(-1.0f, 1.0f, 1.0f);
        glVertex3f(-1.0f, 1.0f, -1.0f);
        glVertex3f(-1.0f, -1.0f, -1.0f);
        glVertex3f(-1.0f, -1.0f, 1.0f);

        glEnd();
        glBegin(GL_LINE_LOOP);
        //Right
        glNormal3f(1.0f,0.0f,0.0f);
        glVertex3f(1.0f, 1.0f, -1.0f);
        glVertex3f(1.0f, 1.0f, 1.0f);
        glVertex3f(1.0f, -1.0f, 1.0f);
        glVertex3f(1.0f, -1.0f, -1.0f);

        glEnd();
        glBegin(GL_LINE_LOOP);
        //Top
        glNormal3f(0.0f,1.0f,0.0f);
        glColor3f(0.0f, 0.0f, 0.0f);
        glVertex3f(1.0f, 1.0f, -1.0f);
        glVertex3f(-1.0f, 1.0f, -1.0f);
        glVertex3f(-1.0f, 1.0f, 1.0f);
        glVertex3f(1.0f, 1.0f, 1.0f);

        glEnd();
        glBegin(GL_LINE_LOOP);
        //bottom
        glNormal3f(0.0f,-1.0f,0.0f);
        glVertex3f(1.0f, -1.0f, 1.0f);
        glVertex3f(-1.0f, -1.0f, 1.0f);
        glVertex3f(-1.0f, -1.0f, -1.0f);
        glVertex3f(1.0f, -1.0f, -1.0f);

        glEnd();
        glBegin(GL_LINE_LOOP);
        //Front
        glNormal3f(0.0f,0.0f,1.0f);
        glVertex3f(1.0f, 1.0f, 1.0f);
        glVertex3f(-1.0f, 1.0f, 1.0f);
        glVertex3f(-1.0f, -1.0f, 1.0f);
        glVertex3f(1.0f, -1.0f, 1.0f);

        glEnd();
        glBegin(GL_LINE_LOOP);
        //Back
        glNormal3f(0.0f,0.0f,-1.0f);
        glVertex3f(1.0f, -1.0f, -1.0f);
        glVertex3f(-1.0f, -1.0f, -1.0f);
        glVertex3f(-1.0f, 1.0f, -1.0f);
        glVertex3f(1.0f, 1.0f, -1.0f);

        glEnd();

    }
    public void traceEdge(float x, float y, float z){
        glBegin(GL_LINE_LOOP);
        //left
        glColor3f(0.0f, 0.0f, 0.0f);
        glNormal3f(-1.0f,0.0f,0.0f);
        glVertex3f(x-1.0f, y+1.0f, z+1.0f);
        glVertex3f(x-1.0f, y+1.0f, z-1.0f);
        glVertex3f(x-1.0f, y-1.0f, z-1.0f);
        glVertex3f(x-1.0f, y-1.0f, z+1.0f);

        glEnd();
        glBegin(GL_LINE_LOOP);
        //Right
        glColor3f(0.0f, 0.0f, 0.0f);
        glNormal3f(1.0f,0.0f,0.0f);
        glVertex3f(x+1.0f, y+1.0f, z-1.0f);
        glVertex3f(x+1.0f, y+1.0f, z+1.0f);
        glVertex3f(x+1.0f, y-1.0f, z+1.0f);
        glVertex3f(x+1.0f, y-1.0f, z-1.0f);

        glEnd();
        glBegin(GL_LINE_LOOP);
        //Top
        glColor3f(0.0f, 0.0f, 0.0f);
        glNormal3f(0.0f,1.0f,0.0f);
        glVertex3f(x+1.0f, y+1.0f, z-1.0f);
        glVertex3f(x-1.0f, y+1.0f, z-1.0f);
        glVertex3f(x-1.0f, y+1.0f, z+1.0f);
        glVertex3f(x+1.0f, y+1.0f, z+1.0f);

        glEnd();
        glBegin(GL_LINE_LOOP);
        //bottom
        glColor3f(0.0f, 0.0f, 0.0f);
        glNormal3f(0.0f,-1.0f,0.0f);
        glVertex3f(x+1.0f, y+-1.0f, z+1.0f);
        glVertex3f(x-1.0f, y-1.0f, z+1.0f);
        glVertex3f(x-1.0f, y-1.0f, z-1.0f);
        glVertex3f(x+1.0f, y-1.0f, z-1.0f);

        glEnd();
        glBegin(GL_LINE_LOOP);
        //Front
        glColor3f(0.0f, 0.0f, 0.0f);
        glNormal3f(0.0f,0.0f,-1.0f);
        glVertex3f(x+1.0f, y+1.0f, z+1.0f);
        glVertex3f(x-1.0f, y+1.0f, z+1.0f);
        glVertex3f(x-1.0f, y-1.0f, z+1.0f);
        glVertex3f(x+1.0f, y-1.0f, z+1.0f);

        glEnd();
        glBegin(GL_LINE_LOOP);
        //Back
        glColor3f(0.0f, 0.0f, 0.0f);
        glNormal3f(0.0f,0.0f,1.0f);
        glVertex3f(x+1.0f, y-1.0f, z-1.0f);
        glVertex3f(x-1.0f, y-1.0f, z-1.0f);
        glVertex3f(x-1.0f, y+1.0f, z-1.0f);
        glVertex3f(x+1.0f, y+1.0f, z-1.0f);

        glEnd();

    }
    
    
}
