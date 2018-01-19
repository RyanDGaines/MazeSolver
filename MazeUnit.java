import javax.swing.*;
import java.awt.*;

public class MazeUnit extends JPanel {
    public int north, south, east, west;
    private int hLocation, wLocation;
    private boolean visited;
    public MazeUnit[] surrounding; //n,e,s,w

    MazeUnit(int h, int w) {
        super();
        hLocation = h; wLocation = w;
        north = 1; //is a wall
        south = 1; //is a wall
        east = 1; //is a wall
        west = 1; //is a wall
        visited = false; //visit is fales
        this.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.white));
        this.setBackground(Color.BLACK);
        this.surrounding = new MazeUnit[4];


    }

    public void isVisited() {
        this.visited = true;
    }
    public boolean hasBeenVisited(){
        return visited;
    }
    public int random() {
        //returns a random number 0-3 0 being north, 1 being east, 2 being south, 3 being west
        // makes sure surrounding isnt a wall or been visited
        int notNull = 0;
        int[] applicableNodes = new int[4];
        for (int i = 0; i < this.surrounding.length; i++){
            if ((this.surrounding[i]!=null) && (!this.surrounding[i].hasBeenVisited())){
                applicableNodes[notNull] = i;
                notNull++;
            }
        }
        if (notNull == 0){
            return -1;
        }
        int randomChoice = (int) (Math.random()*notNull);
        return applicableNodes[randomChoice];
    }

    public void setNorthPath() {
        this.north = 0;
        this.isVisited();
        this.surrounding[0].setSouth();
    }
    private void setSouth(){
        this.south = 0;
        this.isVisited();
    }
    public void setSouthPath() {
        this.south = 0;
        this.isVisited();
        this.surrounding[2].setNorth();
    }
    private void setNorth(){
        this.north = 0;
        this.isVisited();
    }
    public void setWestPath() {
        this.west = 0;
        this.isVisited();
        this.surrounding[3].setEast();
    }
    private void setEast(){
        this.east = 0;
        this.isVisited();
    }
    public void setEastPath() {
        this.east = 0;
        this.isVisited();
        this.surrounding[1].setWest();
    }
    private void setWest(){
        this.west = 0;
        this.isVisited();
    }
    public void updateBorder(){
        this.setBorder(BorderFactory.createMatteBorder(north,west,south,east,Color.WHITE));
    }
    public int getH(){
        return hLocation;
    }
    public int getW(){
        return wLocation;
    }

    public void unVisit() {
        visited = false;
    }

}


