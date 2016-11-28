
package cube.world;


import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;
import java.nio.FloatBuffer;
import java.util.Random;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

public class Chunk {

    static final int CHUNK_SIZE = 100;
    static final int CUBE_LENGTH = 2;
    public Cube [][][] Blocks;
    private int VBOVertexHandle;
    private int VBOTextureHandle;
    private Texture texture;
    private int VBOColorHandle;
    private int VBONormalHandle;
    private int StartX, StartY, StartZ;
    private Random r;

    public void render() {
        glPushMatrix();
//        glPushMatrix();  having this twice seemed to overflow things.
        glBindBuffer(GL_ARRAY_BUFFER,VBOVertexHandle);
        glVertexPointer(3, GL_FLOAT, 0, 0L);
        glBindBuffer(GL_ARRAY_BUFFER, VBONormalHandle);
        glNormalPointer(GL_FLOAT,0,0 );
        glBindBuffer(GL_ARRAY_BUFFER,   VBOColorHandle);
        glColorPointer(4, GL_FLOAT, 0, 0L);
        glBindBuffer(GL_ARRAY_BUFFER, VBOTextureHandle);
        glBindTexture(GL_TEXTURE_2D,1);
        glTexCoordPointer(2,GL_FLOAT,0,0L);
        glDrawArrays(GL_QUADS, 0,
                CHUNK_SIZE * CHUNK_SIZE
                * CHUNK_SIZE * 24);
        glPopMatrix();
    }

    public void rebuildMesh( float startX, float startY, float startZ) {
        float height=0f;
        //make a noise object
        SimplexNoise noise = new SimplexNoise(50,.035,r.nextInt());
        
        //initialize things
        VBOColorHandle = glGenBuffers();
        VBOVertexHandle = glGenBuffers();
        VBOTextureHandle = glGenBuffers();
        VBONormalHandle = glGenBuffers();
        
        FloatBuffer VertexTextureData = BufferUtils.createFloatBuffer((CHUNK_SIZE*CHUNK_SIZE*CHUNK_SIZE)*6*12);
        FloatBuffer VertexPositionData = BufferUtils.createFloatBuffer((CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE) * 6 * 12);
        FloatBuffer VertexNormalData = BufferUtils.createFloatBuffer((CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE) * 6 * 12);
        FloatBuffer VertexColorData = BufferUtils.createFloatBuffer((CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE) * 6 * 16);
        
        //iterate through and put data in the buffers as appropriate
        for (float x = 0; x < CHUNK_SIZE; x += 1) {
            for (float z = 0; z < CHUNK_SIZE; z += 1) {
                //calculate the height at each x,z position
                height = (int)(startY + Math.abs((int)(100*noise.getNoise((int)x,(int)z)*CUBE_LENGTH)));
                for (float y = 0; y < CHUNK_SIZE; y++) {
                    if (y<=height){
                        setBlockType(x,y,z,height);
                        VertexPositionData.put(
                                createCube((float) (startX + x* CUBE_LENGTH),
                                        (float) (startY + y *CUBE_LENGTH),
                                        (float) (startZ + z * CUBE_LENGTH)));
                        Blocks[(int) x][(int) y][(int) z].setCoords((float) (startX + x* CUBE_LENGTH),
                                        (float) (startY + y *CUBE_LENGTH),
                                        (float) (startZ + z * CUBE_LENGTH));
                        VertexNormalData.put(generateNormals());
                        VertexColorData.put(
                                createCubeVertexCol(
                                        getCubeColor(
                                                Blocks[(int) x][(int) y][(int) z]))).put(1.0f);
                        VertexTextureData.put(createTexCube((float)0,(float)0,Blocks[(int)x][(int)y][(int)z]));
                    }
                }
            }
            
        }
       
        //add water last because they're gonna be semi-see through
        for (float x = 0; x < CHUNK_SIZE; x += 1) {
                for (float z = 0; z < CHUNK_SIZE; z += 1) {
                    for (float y = 0; y < 3; y++) {
                        if (!Blocks[(int)x][(int)y][(int)z].isActive()){
                            Blocks[(int) x][(int) y][(int) z].setBlockType(Cube.BlockType.BlockType_Water);
                            //Blocks[(int) x][(int) y][(int) z].SetActive(true);
                            Blocks[(int) x][(int) y][(int) z].setCoords((float) (startX + x* CUBE_LENGTH),
                                        (float) (startY + y *CUBE_LENGTH),
                                        (float) (startZ + z * CUBE_LENGTH));
                            VertexPositionData.put(
                                createCube((float) (startX + x* CUBE_LENGTH),
                                        (float) (startY + y *CUBE_LENGTH),
                                        (float) (startZ + z * CUBE_LENGTH)));
                            VertexNormalData.put(generateNormals());
                            VertexColorData.put(
                                createCubeVertexCol(
                                        getCubeColor(
                                                Blocks[(int) x][(int) y][(int) z]))).put(0.3f);
                            VertexTextureData.put(createTexCube((float)0,(float)0,Blocks[(int)x][(int)y][(int)z]));
                        }
                    }
                }
        }
        VertexColorData.flip();
        VertexPositionData.flip();
        VertexNormalData.flip();
        VertexTextureData.flip();
        glBindBuffer(GL_ARRAY_BUFFER,VBOVertexHandle);
        glBufferData(GL_ARRAY_BUFFER,VertexPositionData,GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER,VBONormalHandle);
        glBufferData(GL_ARRAY_BUFFER,VertexNormalData, GL_STATIC_DRAW); 
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        
        glBindBuffer(GL_ARRAY_BUFFER,VBOColorHandle);
        glBufferData(GL_ARRAY_BUFFER, VertexColorData,GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, VBOTextureHandle);
        glBufferData(GL_ARRAY_BUFFER, VertexTextureData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    private float[] createCubeVertexCol(float[] CubeColorArray) {
        float[] cubeColors = new float[CubeColorArray.length * 4 * 6];
        for (int i = 0; i < cubeColors.length; i++) {
            cubeColors[i] = CubeColorArray[i
                    % CubeColorArray.length];
        }
        return cubeColors;
    }

    /**
     * Each vertex needs a Normal, so sayth the OpenGL bible. 
     * This will create the 24 normals that a cube will have, in the same order
     * as the createCube method.
     * 
     * @return 
     */
    public static float[] generateNormals(){
        return new float[]{
        //TOP
        0.0f, -1.0f, 0.0f,
        0.0f, -1.0f, 0.0f,
        0.0f, -1.0f, 0.0f,
        0.0f, -1.0f, 0.0f,
        //Bottom
        0.0f, 1.0f, 0.0f,
        0.0f, 1.0f, 0.0f,
        0.0f, 1.0f, 0.0f,
        0.0f, 1.0f, 0.0f,
        //Front
        0.0f, 0.0f, 1.0f,
        0.0f, 0.0f, 1.0f,
        0.0f, 0.0f, 1.0f,
        0.0f, 0.0f, 1.0f,
        //Back
        0.0f, 0.0f, -1.0f,
        0.0f, 0.0f, -1.0f,
        0.0f, 0.0f, -1.0f,
        0.0f, 0.0f, -1.0f,
        //Left
        1.0f, 0.0f, 0.0f,
        1.0f, 0.0f, 0.0f,
        1.0f, 0.0f, 0.0f,
        1.0f, 0.0f, 0.0f,
        //Right
        -1.0f, 0.0f, 0.0f,
        -1.0f, 0.0f, 0.0f,
        -1.0f, 0.0f, 0.0f,
        -1.0f, 0.0f, 0.0f
        };
    }
    
    public static float[] createCube(float x, float y, float z) {
        int offset = CUBE_LENGTH / 2;
        return new float[]{
            // TOP QUAD
            x + offset, y + offset, z,
            x - offset, y + offset, z,
            x - offset, y + offset, z - CUBE_LENGTH,
            x + offset, y + offset, z - CUBE_LENGTH,
            // BOTTOM QUAD
            x + offset, y - offset, z - CUBE_LENGTH,
            x - offset, y - offset, z - CUBE_LENGTH,
            x - offset, y - offset, z,
            x + offset, y - offset, z,
            // FRONT QUAD
            x + offset, y + offset, z - CUBE_LENGTH,
            x - offset, y + offset, z - CUBE_LENGTH,
            x - offset, y - offset, z - CUBE_LENGTH,
            x + offset, y - offset, z - CUBE_LENGTH,
            // BACK QUAD
            x + offset, y - offset, z,
            x - offset, y - offset, z,
            x - offset, y + offset, z,
            x + offset, y + offset, z,
            // LEFT QUAD
            x - offset, y + offset, z - CUBE_LENGTH,
            x - offset, y + offset, z,
            x - offset, y - offset, z,
            x - offset, y - offset, z - CUBE_LENGTH,
            // RIGHT QUAD
            x + offset, y + offset, z,
            x + offset, y + offset, z - CUBE_LENGTH,
            x + offset, y - offset, z - CUBE_LENGTH,
            x + offset, y - offset, z};
    }

    private float[] getCubeColor(Cube block) {
        return new float[]{0.0f, 0.84f, 0.671f};
    }
    public static float[] createTexCube(float x, float y, Cube block) {
        float offset = (1024f/16)/1024f;
        switch (block.GetID()) {
            case 0:  //GRASS
                return new float[] {
                // BOTTOM QUAD(DOWN=+Y)
                x + offset*3, y + offset*10,
                x + offset*2, y + offset*10,
                x + offset*2, y + offset*9,
                x + offset*3, y + offset*9,
                // TOP!
                x + offset*3, y + offset*1,
                x + offset*2, y + offset*1,
                x + offset*2, y + offset*0,
                x + offset*3, y + offset*0,
                // FRONT QUAD
                x + offset*3, y + offset*0,
                x + offset*4, y + offset*0,
                x + offset*4, y + offset*1,
                x + offset*3, y + offset*1,
                // BACK QUAD
                x + offset*4, y + offset*1,
                x + offset*3, y + offset*1,
                x + offset*3, y + offset*0,
                x + offset*4, y + offset*0,
                // LEFT QUAD
                x + offset*3, y + offset*0,
                x + offset*4, y + offset*0,
                x + offset*4, y + offset*1,
                x + offset*3, y + offset*1,
                // RIGHT QUAD
                x + offset*3, y + offset*0,
                x + offset*4, y + offset*0,
                x + offset*4, y + offset*1,
                x + offset*3, y + offset*1};
            case 1: //SAND
                return new float[] {
                // BOTTOM QUAD(DOWN=+Y)
                x + offset*2, y + offset*1,
                x + offset*3, y + offset*1,
                x + offset*3, y + offset*2,
                x + offset*2, y + offset*2,
                // TOP!
                x + offset*2, y + offset*1,
                x + offset*3, y + offset*1,
                x + offset*3, y + offset*2,
                x + offset*2, y + offset*2,
                // FRONT QUAD
                x + offset*2, y + offset*1,
                x + offset*3, y + offset*1,
                x + offset*3, y + offset*2,
                x + offset*2, y + offset*2,
                // BACK QUAD
                x + offset*2, y + offset*1,
                x + offset*3, y + offset*1,
                x + offset*3, y + offset*2,
                x + offset*2, y + offset*2,
                // LEFT QUAD
                x + offset*2, y + offset*1,
                x + offset*3, y + offset*1,
                x + offset*3, y + offset*2,
                x + offset*2, y + offset*2,
                // RIGHT QUAD
                x + offset*2, y + offset*1,
                x + offset*3, y + offset*1,
                x + offset*3, y + offset*2,
                x + offset*2, y + offset*2};
            case 2: //Water
                return new float[] {
                // BOTTOM QUAD(DOWN=+Y)
                x + offset*13, y + offset*12,
                x + offset*14, y + offset*12,
                x + offset*14, y + offset*13,
                x + offset*13, y + offset*13,
                // TOP!
                x + offset*13, y + offset*12,
                x + offset*14, y + offset*12,
                x + offset*14, y + offset*13,
                x + offset*13, y + offset*13,
                // FRONT QUAD
                x + offset*4, y + offset*11,
                x + offset*5, y + offset*11,
                x + offset*5, y + offset*12,
                x + offset*4, y + offset*12,
                // BACK QUAD
                x + offset*4, y + offset*11,
                x + offset*5, y + offset*11,
                x + offset*5, y + offset*12,
                x + offset*4, y + offset*12,
                // LEFT QUAD
                x + offset*4, y + offset*11,
                x + offset*5, y + offset*11,
                x + offset*5, y + offset*12,
                x + offset*4, y + offset*12,
                // RIGHT QUAD
                x + offset*4, y + offset*11,
                x + offset*5, y + offset*11,
                x + offset*5, y + offset*12,
                x + offset*4, y + offset*12};
            case 3: //Dirt
                return new float[] {
                // BOTTOM QUAD(DOWN=+Y)
                x + offset*3, y + offset*10,
                x + offset*2, y + offset*10,
                x + offset*2, y + offset*9,
                x + offset*3, y + offset*9,
                // TOP!
                x + offset*3, y + offset*1,
                x + offset*2, y + offset*1,
                x + offset*2, y + offset*0,
                x + offset*3, y + offset*0,
                // FRONT QUAD
                x + offset*3, y + offset*0,
                x + offset*4, y + offset*0,
                x + offset*4, y + offset*1,
                x + offset*3, y + offset*1,
                // BACK QUAD
                x + offset*4, y + offset*1,
                x + offset*3, y + offset*1,
                x + offset*3, y + offset*0,
                x + offset*4, y + offset*0,
                // LEFT QUAD
                x + offset*3, y + offset*0,
                x + offset*4, y + offset*0,
                x + offset*4, y + offset*1,
                x + offset*3, y + offset*1,
                // RIGHT QUAD
                x + offset*3, y + offset*0,
                x + offset*4, y + offset*0,
                x + offset*4, y + offset*1,
                x + offset*3, y + offset*1};
            case 4: //Stone
                return new float[] {
                // BOTTOM QUAD(DOWN=+Y)
                x + offset*3, y + offset*10,
                x + offset*2, y + offset*10,
                x + offset*2, y + offset*9,
                x + offset*3, y + offset*9,
                // TOP!
                x + offset*3, y + offset*1,
                x + offset*2, y + offset*1,
                x + offset*2, y + offset*0,
                x + offset*3, y + offset*0,
                // FRONT QUAD
                x + offset*3, y + offset*0,
                x + offset*4, y + offset*0,
                x + offset*4, y + offset*1,
                x + offset*3, y + offset*1,
                // BACK QUAD
                x + offset*4, y + offset*1,
                x + offset*3, y + offset*1,
                x + offset*3, y + offset*0,
                x + offset*4, y + offset*0,
                // LEFT QUAD
                x + offset*3, y + offset*0,
                x + offset*4, y + offset*0,
                x + offset*4, y + offset*1,
                x + offset*3, y + offset*1,
                // RIGHT QUAD
                x + offset*3, y + offset*0,
                x + offset*4, y + offset*0,
                x + offset*4, y + offset*1,
                x + offset*3, y + offset*1};
            case 5: //Bedrock
                return new float[] {
                // BOTTOM QUAD(DOWN=+Y)
                x + offset*2, y + offset*1,
                x + offset*1, y + offset*1,
                x + offset*1, y + offset*2,
                x + offset*2, y + offset*2,
                // TOP!
                x + offset*2, y + offset*1,
                x + offset*1, y + offset*1,
                x + offset*1, y + offset*2,
                x + offset*2, y + offset*2,
                // FRONT QUAD
                x + offset*2, y + offset*1,
                x + offset*1, y + offset*1,
                x + offset*1, y + offset*2,
                x + offset*2, y + offset*2,
                // BACK QUAD
                x + offset*2, y + offset*1,
                x + offset*1, y + offset*1,
                x + offset*1, y + offset*2,
                x + offset*2, y + offset*2,
                // LEFT QUAD
                x + offset*2, y + offset*1,
                x + offset*1, y + offset*1,
                x + offset*1, y + offset*2,
                x + offset*2, y + offset*2,
                // RIGHT QUAD
                x + offset*2, y + offset*1,
                x + offset*1, y + offset*1,
                x + offset*1, y + offset*2,
                x + offset*2, y + offset*2};
            default:
                return new float[] {
                // BOTTOM QUAD(DOWN=+Y)
                x + offset*3, y + offset*10,
                x + offset*2, y + offset*10,
                x + offset*2, y + offset*9,
                x + offset*3, y + offset*9,
                // TOP!
                x + offset*3, y + offset*1,
                x + offset*2, y + offset*1,
                x + offset*2, y + offset*0,
                x + offset*3, y + offset*0,
                // FRONT QUAD
                x + offset*3, y + offset*0,
                x + offset*4, y + offset*0,
                x + offset*4, y + offset*1,
                x + offset*3, y + offset*1,
                // BACK QUAD
                x + offset*4, y + offset*1,
                x + offset*3, y + offset*1,
                x + offset*3, y + offset*0,
                x + offset*4, y + offset*0,
                // LEFT QUAD
                x + offset*3, y + offset*0,
                x + offset*4, y + offset*0,
                x + offset*4, y + offset*1,
                x + offset*3, y + offset*1,
                // RIGHT QUAD
                x + offset*3, y + offset*0,
                x + offset*4, y + offset*0,
                x + offset*4, y + offset*1,
                x + offset*3, y + offset*1};
        }
    }
    public Chunk(int startX, int startY, int startZ ) {
        try{
            texture = TextureLoader.getTexture("PNG",ResourceLoader.getResourceAsStream("terrain.png"));
        }
        catch(Exception e)
        {
        System.out.print("ER-ROAR!");
        }
        r = new Random();
        Blocks = new Cube[CHUNK_SIZE][CHUNK_SIZE][CHUNK_SIZE];
        for (float x = 0; x < CHUNK_SIZE; x += 1) {
            for (float z = 0; z < CHUNK_SIZE; z += 1) {
                for (float y = 0; y < CHUNK_SIZE; y++) {
                    Blocks[(int)x][(int)y][(int)z] = new Cube(Cube.BlockType.BlockType_Default);
                    Blocks[(int)x][(int)y][(int)z].SetActive(false);
                }
            }
        }
        VBOColorHandle = glGenBuffers();
        VBOVertexHandle = glGenBuffers();
        VBOTextureHandle = glGenBuffers();
        StartX = startX;
        StartY = startY;
        StartZ = startZ;
        rebuildMesh(startX, startY, startZ);
    }

    private void setBlockType(float x, float y, float z, float height) {
      
        if( (y == 0) && (height == 0))
            Blocks[(int)x][(int)y][(int)z]= new Cube(Cube.BlockType.BlockType_Bedrock);
        else if (((int)y == (int)height)&&((int)y>2))
            Blocks[(int)x][(int)y][(int)z]= new Cube(Cube.BlockType.BlockType_Grass);
        else if (y < 2)
            Blocks[(int)x][(int)y][(int)z]= new Cube(Cube.BlockType.BlockType_Sand);
        else
            Blocks[(int)x][(int)y][(int)z]= new Cube(Cube.BlockType.BlockType_Dirt);
        Blocks[(int)x][(int)y][(int)z].SetActive(true);
    }
}
