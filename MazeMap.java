import java.awt.*;
import java.util.*;

public class MazeMap {
    private int width, height;
    private static int MAX_WIDTH = 500;
    private static int MAX_HEIGHT = 500;
    private MazeUnit unit[][];
    private Stack<MazeUnit> mazeUnitStack;
    private MazeUnit current, end;
    private int heightI, widthI;

    public MazeMap(int w, int h) {
        //set up maze map, which is matrix of maze units
        width = w;
        height = h;
        unit = new MazeUnit[h][w];
        int hSpace = 0, wSpace = 0;
        //spaces even boxes
        if (width == height) {
            hSpace = MAX_HEIGHT / height;
            wSpace = MAX_WIDTH / width;
        }
        else if (height > width){
            hSpace = MAX_HEIGHT / height;
            wSpace = hSpace;
        }
        else if (width > height){
            wSpace = MAX_WIDTH / width;
            hSpace = wSpace;
        }

        //height = y, width = x in terms of coordinates
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                unit[i][j] = new MazeUnit(i,j);
                unit[i][j].setLocation(new Point(j*wSpace,i*hSpace));
                unit[i][j].setSize(wSpace,hSpace);
                unit[i][j].setVisible(true);
                }
            }
            //set up surrounding points
        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                if (i == 0){
                    unit[i][j].surrounding[0] = null;
                }
                else{
                    unit[i][j].surrounding[0] = unit[i-1][j];
                }
                if (j == 0){
                    unit[i][j].surrounding[3] = null;
                }
                else{
                    unit[i][j].surrounding[3] = unit[i][j-1];
                }
                if (i+1 == height){
                    unit[i][j].surrounding[2] = null;
                }
                else{
                    unit[i][j].surrounding[2] = unit[i+1][j];
                }
                if (j+1 == width){
                    unit[i][j].surrounding[1] = null;
                }
                else{
                    unit[i][j].surrounding[1] = unit[i][j+1];
                }
            }
        }
    }

    public void solveNoAnimate(Container C) {
        //solve no animation, which just combines the two animate functions into one
        solveAnimate(C);
        while (end != current) {
            updateSolve(C);
        }
        setEnd(C);
    }

    public void removeFromContainer(Container C){
        //clear map from contianer
        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                C.remove(unit[i][j]);
            }
        }
    }
    public void addToContainer(Container C){
        //add map to container
        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                C.add(unit[i][j]);
            }
        }
    }
    public void generateAnimate(){
        //animates first unit, sets up stack
        mazeUnitStack = new Stack<>();
        mazeUnitStack.push(unit[0][0]);
        unit[0][0].isVisited();
        heightI = 0 ; widthI = 0;
    }

    public void generateNoAnimate(Container c) {
        //which just combines the two animate functions into one
        generateAnimate();
        while (!mazeUnitStack.empty()) {
            updateAnimate(c);
        }
    }

    public int numberVisited() {
        // gets number visited
        int numVisited = 0;
        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++)
                if (unit[i][j].hasBeenVisited()){
                    numVisited++;
                }
        }
        return numVisited;
    }

    public boolean updateAnimate(Container c) {
        //get next random maze unit, add to stack, pop if -1
        int random = unit[heightI][widthI].random();
        if (random == 0){
            unit[heightI][widthI].setNorthPath();
            unit[heightI][widthI].updateBorder();
            heightI--;
            unit[heightI][widthI].updateBorder();
            mazeUnitStack.push(unit[heightI][widthI]);
        }
        else if (random == 1){
            unit[heightI][widthI].setEastPath();
            unit[heightI][widthI].updateBorder();
            widthI++;
            unit[heightI][widthI].updateBorder();
            mazeUnitStack.push(unit[heightI][widthI]);
        }
        else if (random == 2){
            unit[heightI][widthI].setSouthPath();
            unit[heightI][widthI].updateBorder();
            heightI++;
            unit[heightI][widthI].updateBorder();
            mazeUnitStack.push(unit[heightI][widthI]);
        }
        else if (random == 3){
            unit[heightI][widthI].setWestPath();
            unit[heightI][widthI].updateBorder();
            widthI--;
            unit[heightI][widthI].updateBorder();
            mazeUnitStack.push(unit[heightI][widthI]);
        }
        else if (random == -1){
            mazeUnitStack.pop();
            if (!mazeUnitStack.empty()) {
                heightI = mazeUnitStack.peek().getH();
                widthI = mazeUnitStack.peek().getW();
            }
        }
        if (!mazeUnitStack.empty()) {
            MazeUnit temp = mazeUnitStack.peek();
            c.add(temp);
        }
        return mazeUnitStack.empty();
    }

    public void solveAnimate(Container c) {
        //mark all unvisited
        for(int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                unit[i][j].unVisit();
            }
        }
        //visit first one, add to stack
        mazeUnitStack = new Stack<>();
        heightI = 0; widthI = 0;
        mazeUnitStack.push(unit[heightI][widthI]);
        unit[heightI][widthI].isVisited();
        unit[heightI][widthI].setBackground(Color.GREEN);
        end = unit[height - 1][width - 1];
        current = unit[heightI][widthI];
        c.add(current);
    }

    public boolean updateSolve(Container c) {
        // go to next maze unit, add to stack, pop if all surrounding are visited
        boolean foundNew = false;
        if (current.east == 0){
            if (!unit[heightI][widthI + 1].hasBeenVisited()) {
                widthI++;
                foundNew = true;
            }
        }
        if (current.south == 0 && !foundNew){
            if (!unit[heightI + 1][widthI].hasBeenVisited()) {
                heightI++;
                foundNew = true;
            }
        }
        if (current.west == 0 && !foundNew){
            if (!unit[heightI][widthI - 1].hasBeenVisited()){
                widthI--;
                foundNew = true;
            }
        }
        if (current.north == 0 && !foundNew){
            if (!unit[heightI - 1][widthI].hasBeenVisited()){
                heightI--;
                foundNew = true;
            }
        }
        if (foundNew == true){
            unit[heightI][widthI].isVisited();
            unit[heightI][widthI].setBackground(Color.PINK);
            mazeUnitStack.push(unit[heightI][widthI]);
        }
        else{
            mazeUnitStack.pop();
            unit[heightI][widthI].setBackground(Color.GRAY);
            heightI = mazeUnitStack.peek().getH();
            widthI = mazeUnitStack.peek().getW();
        }
        current = mazeUnitStack.peek();
        if (current == end){
            return true;
        }
        else{
           c.add(current);
            return false;
        }
    }


    public void setEnd(Container c) {
        //sets end unit to red
        end.setBackground(Color.RED);
        c.add(end);
    }
}
