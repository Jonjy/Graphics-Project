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
        BOTTOM(6);
        
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
        switch(type){
        case NONE:
            normal = new Vector3f(1,1,1);
            break;
        case TOP:
            normal = new Vector3f(0,-1,0);
            break;
        case BOTTOM:
            normal = new Vector3f(0,1,0);
            break;
        case LEFT:
            normal = new Vector3f(1,0,0);
            break;
        case RIGHT:
            normal = new Vector3f(-1,0,0);
            break;
        case FRONT:
            normal = new Vector3f(0,0,-1);
            break;
        case BACK:
            normal = new Vector3f(0,0,1);
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
        int truX, truY, truZ;
        truX = (int)Math.floor((-position.x+1)/2);
        truY = (int)Math.floor((-position.y+1)/2);
        truZ = (int)Math.floor((-position.x-2)/2)
        if(noClip) 
            return new Collision(Collision.CollisionType.NONE);
        Vector3f[] boundingBox= new Vector3f[8];
        for (int i = 0;i<boundingBox.length;i++){
            boundingBox[i] = new Vector3f();
        }
        boundingBox[0].x = (int)Math.floor((-position.x+2)/2);boundingBox[0].y = (int)Math.floor((-position.y)/2);boundingBox[0].z = (int)Math.floor((-position.z-1)/2);
        boundingBox[1].x = (int)Math.floor((-position.x+2)/2);boundingBox[1].y = (int)Math.floor((-position.y)/2);boundingBox[1].z = (int)Math.floor((-position.z-3)/2);
        boundingBox[2].x = (int)Math.floor((-position.x+2)/2);boundingBox[2].y = (int)Math.floor((-position.y+2)/2);boundingBox[2].z = (int)Math.floor((-position.z-1)/2);
        boundingBox[3].x = (int)Math.floor((-position.x+2)/2);boundingBox[3].y = (int)Math.floor((-position.y+2)/2);boundingBox[3].z = (int)Math.floor((-position.z-3)/2);
        boundingBox[4].x = (int)Math.floor((-position.x)/2);boundingBox[4].y = (int)Math.floor((-position.y)/2);boundingBox[4].z = (int)Math.floor((-position.z-1)/2);
        boundingBox[5].x = (int)Math.floor((-position.x)/2);boundingBox[5].y = (int)Math.floor((-position.y)/2);boundingBox[5].z = (int)Math.floor((-position.z-3)/2);
        boundingBox[6].x = (int)Math.floor((-position.x)/2);boundingBox[6].y = (int)Math.floor((-position.y+2)/2);boundingBox[6].z = (int)Math.floor((-position.z-1)/2);
        boundingBox[7].x = (int)Math.floor((-position.x)/2);boundingBox[7].y = (int)Math.floor((-position.y+2)/2);boundingBox[7].z = (int)Math.floor((-position.z-3)/2);
        int x,y,z;
        //Put them in array indexform
        for(int i = 0;i<boundingBox.length;i++){
            x = (int)boundingBox[i].x;
            y = (int)boundingBox[i].y;
            z = (int)boundingBox[i].z;
            //check out of bounds first
            if ((x<0)||(y<0)||(z<0)||(x>99)||(y>99)||(z>99)){
                return true;
            }
            else if(CubeWorld.assHatt.Blocks[x][y][z].isActive()){
                //Check Which Direction
            }
        }
        
        return new Collision(Collision.CollisionType.NONE);
    }  
    
    public CollisionType getType(){
            return collision;
        }
        
}
