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
                normal.x = 1;
                break;
            case FRONT:
                normal.z = 1;
                break;
            case BACK:
                normal.z = 1;
                break;
        }
    }
    
    /**
     * TODO :  FINISH this.  It will check for collisions and return
     * @param position
     * @param direction
     * @param noClip
     * 
     * @return 
     */
    public static Collision checkCollision(Vector3f position,Vector3f direction, Boolean noClip){
        int x, y, z;
        x = (int)Math.floor(-(position.x+1)/2);
        y = (int)Math.floor(-(position.y+1)/2);
        z = (int)Math.floor(-(position.z-2)/2);
        System.out.println( x+" "+y+" "+z +" "+position.z);
        //short circuit if no clipping is on
        if(noClip) 
            return new Collision(Collision.CollisionType.NONE);
        
        Collision result = new Collision(CollisionType.OTHER);
        
        if(direction.z>0){
            if((z-1<0)||(CubeWorld.chunk.Blocks[x][y][z-1].isActive())||(CubeWorld.chunk.Blocks[x][y-1][z-1].isActive())){
                result.addCollision(CollisionType.BACK);
            }
        }
        if(direction.z<0){
            if((z+1>99)||(CubeWorld.chunk.Blocks[x][y-1][z+1].isActive())||(CubeWorld.chunk.Blocks[x][y][z+1].isActive())){
                result.addCollision(CollisionType.FRONT);
            }
        }
        if(direction.x>0){
            if(((x-1)<0)||(CubeWorld.chunk.Blocks[x-1][y-1][z].isActive())||(CubeWorld.chunk.Blocks[x-1][y][z].isActive())){
                result.addCollision(CollisionType.RIGHT); 
            }
        }
        if(direction.x<0){
            if(((x+1)>99)||(CubeWorld.chunk.Blocks[x+1][y-1][z].isActive())||(CubeWorld.chunk.Blocks[x+1][y][z].isActive())){
                 result.addCollision(CollisionType.LEFT);  
            }
        }
        if(direction.y<0){
            if(((y+1)>99)||(CubeWorld.chunk.Blocks[x][y+1][z].isActive())){
                result.addCollision(CollisionType.BOTTOM);
            }
        }
        if(direction.y>0){
            if(((y-1)<0)||(CubeWorld.chunk.Blocks[x][y-2][z].isActive())){
                result.addCollision(CollisionType.TOP);
            }
        }
        
        
        
        return result;
    }  
    
    public CollisionType getType(){
            return collision;
        }
        
}
