package models;

/**
 *
 * @author Cesar Cardozo
 */
public class RawModel {

    private int vaoID; // An identifier among other VAOs 
    private int vertexCount; /* Since a VAO consist of (X,Y,Z) vectors, its pretty useful to know the amount of vertexes}
                            we're going to use to store the data
    */
    

    public RawModel(int vaoID, int vertexCount) {
        this.vaoID = vaoID;
        this.vertexCount = vertexCount;
    }

    public int getVaoID() {
        return vaoID;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public void setVaoID(int vaoID) {
        this.vaoID = vaoID;
    }

    public void setVertexCount(int vertexCount) {
        this.vertexCount = vertexCount;
    }

}
