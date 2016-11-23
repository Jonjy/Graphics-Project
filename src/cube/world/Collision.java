/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cube.world;

import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Phil
 */
public class Collision {
    CollisionType collision;
    Vector3f normal;;
    
    public enum CollisionType{
        NONE(0),
        TOP(1),
        LEFT(2),
        RIGHT(3),
        FRONT(4),
        BACK(5),
        BOTTOM(6),
        OTHER(7);
        
        int collisionID;
                
        CollisionType(int i){
            collisionID = i;
        }
        
        public int GetID() {
            return collisionID;
        }
    }
    
    public Collision(CollisionType type){
        collision = type;
        
        normal = new Vector3f(0,0,0);
        
    }
    
    
    public Vector3f getNormal(){
        return normal;
    }
    
    public void addCollision(CollisionType type){
        switch(type){
            case TOP:
                normal.y = -1;
                break;
            case BOTTOM:
                normal.y = 1;
                break;
            case LEFT:
                normal.x = 1;
                break;
            case RIGHT:
                normal.x = -1;
                break;
            case FRONT:
                normal.z = -1;
                break;
            case BACK:
                normal.z = 1;
                break;
        }
    }
    
    /**
     * TODO :  FINISH this.  It will check for collisions and return
     * @param position
     * @param noClip
     * @return 
     */
    public static Collision checkCollision(Vector3f position, Boolean noClip){
        int x, y, z;
        x = (int)Math.floor(-(position.x+1)/2);
        y = (int)Math.floor(-(position.y+1)/2);
        z = (int)Math.floor(-(position.z-2)/2);
        System.out.println( x+" "+y+" "+z +" "+position.z);
        //short circuit if no clipping is on
        if(noClip) 
            return new Collision(Collision.CollisionType.NONE);
        
        Collision result = new Collision(CollisionType.OTHER);
             
        if(((x-1)<0)||(CubeWorld.assHatt.Blocks[x-1][y][z].isActive())){
            result.addCollision(CollisionType.RIGHT);
        }
        if(((x+1)>99)||(CubeWorld.assHatt.Blocks[x+1][y][z].isActive())){
            result.addCollision(CollisionType.LEFT);
        }
        if(((y+1)>99)||(CubeWorld.assHatt.Blocks[x][y+1][z].isActive())){
            result.addCollision(CollisionType.BOTTOM);
        }
        if(((y-1)<0)||(CubeWorld.assHatt.Blocks[x][y-1][z].isActive())){
            result.addCollision(CollisionType.TOP);
        }
        if(((z+1)>99)||(CubeWorld.assHatt.Blocks[x][y][z+1].isActive())){
            result.addCollision(CollisionType.FRONT);
        }
        if(((z-1)<0)||(CubeWorld.assHatt.Blocks[x][y][z-1].isActive())){
            result.addCollision(CollisionType.BACK);
        }
 
        
        
        return result;
    }  
    
    public CollisionType getType(){
            return collision;
        }
        
}
