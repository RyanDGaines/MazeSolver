import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.*;

public class Maze extends JFrame implements ChangeListener, ActionListener{
    private int width, height;
    private int speedFactor;
    private JPanel searchPanel, black;
    private JLabel visitedLabel, statusLabel;
    private MazeMap map;
    private Container c;
    private Timer time;
    private boolean fullySolved, hasBeenGenerated, willAnimateGeneration, willAnimateSolution, fullyanimatiedGeneration, isGenerating, isSolving;
    private sidePanel side;

    Maze(){
        super("Maze Game");
        //initialize the main ints and bool flags
        width = 50; height = 50;
        hasBeenGenerated = false; willAnimateGeneration = false; willAnimateSolution = false; isGenerating = false; isSolving = false;
        speedFactor = 500;
        fullyanimatiedGeneration = false;

        // set up timer and side panel, and listeners
        time = new Timer(1000,this);
        time.setActionCommand("time");
        side = new sidePanel(width,height);
        side.addActionListener(this);
        side.addChangeListener(this);

        //set up blank black panel for aesthetic purposes
        black = new JPanel();
        black.setSize(new Dimension(500,500));
        black.setLocation(new Point(0,0));
        black.setBackground(Color.BLACK);

        //set up visit label
        visitedLabel = new JLabel("Visited: 0%");
        visitedLabel.setHorizontalAlignment(SwingConstants.CENTER);
        visitedLabel.setForeground(Color.WHITE);

        //set up status label
        statusLabel = new JLabel("Maze Not Made");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setForeground(Color.WHITE);

        //set up search panel, contains status and visit label
        searchPanel = new JPanel();
        searchPanel.setBackground(Color.BLUE);
        searchPanel.setSize(new Dimension(500,85));
        searchPanel.setLocation(new Point(0,500));
        searchPanel.setLayout(new GridLayout(1,2));
        searchPanel.add(statusLabel);
        searchPanel.add(visitedLabel);

        //set up container
        getContentPane().setLayout(null);
        c = getContentPane();
        c.add(black);
        c.add(searchPanel);
        side.addToContainer(c);

        //set up frame
        setResizable(false);
        setSize(700,600);
        setVisible(true);
    }


    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Generate") && !willAnimateGeneration){
            //generate with no animate
            if(!hasBeenGenerated) {
                c.remove(black);
                hasBeenGenerated = true;
                statusLabel.setText("Maze generated!");
            }
            else{
                map.removeFromContainer(c);
                statusLabel.setText("New maze generated!");
            }
            map = new MazeMap(width, height);
            c.repaint();
            map.generateNoAnimate(c);
            visitedLabel.setText("Visited: 0%");
        }

        else if(e.getActionCommand().equals("Generate") && willAnimateGeneration){
            //generate with animate
            if(!hasBeenGenerated) {
                c.remove(black);
            }
            else{
                map.removeFromContainer(c);
                time.restart();
            }
            c.repaint();
            map = new MazeMap(width, height);
            map.generateAnimate();
            map.addToContainer(c);
            time.start();
            isGenerating = true;
            statusLabel.setText("Generating...");
            visitedLabel.setText("Visited: 0%");
        }

        else if (e.getActionCommand().equals("Solve") && hasBeenGenerated && !willAnimateSolution && !isGenerating){
            //solve with no animate
            map.solveNoAnimate(c);
            statusLabel.setText("Maze Solved!");
            double visited = map.numberVisited();
            double total = width * height;
            double percent = visited / total;
            percent = percent * 100;
            visitedLabel.setText("Visited: " + (int) percent + "%");
            c.repaint();
        }

        else if (e.getActionCommand().equals("Solve") && hasBeenGenerated && willAnimateSolution && !isGenerating){
            //solve with animation
            map.solveAnimate(c);
            time.restart();
            c.repaint();
            isSolving = true;
            fullySolved = false;
            statusLabel.setText("Solving...");
        }
        else if (e.getActionCommand().equals("solveAnimate")){
            //checkbox is checked
            willAnimateSolution = !willAnimateSolution;

        }
        else if (e.getActionCommand().equals("generateAnimate")){
            //checkbox is checked
            willAnimateGeneration = !willAnimateGeneration;
        }
        else if (e.getActionCommand().equals("Stop")){
            //stop timer, and stop animation
            time.stop();
            if (isGenerating){
                hasBeenGenerated = false;
                isGenerating = false;
                map.removeFromContainer(c);
                c.repaint();
                c.add(black);
            }
            if (isSolving){
                isSolving = false;
            }
            statusLabel.setText("Stopped!");
        }
        else if (e.getActionCommand().equals("time")){
            //for every delay, update map
            if (!fullyanimatiedGeneration && isGenerating) {
                fullyanimatiedGeneration = map.updateAnimate(c);
            }
            else if(fullyanimatiedGeneration && isGenerating){
                //finished generation
                c.remove(black);
                map.removeFromContainer(c);
                map.addToContainer(c);
                time.stop();
                statusLabel.setText("Fully Generated Maze");
                fullyanimatiedGeneration = false; isGenerating = false;
                hasBeenGenerated = true;

            }
            else if(isSolving && !fullySolved){
                fullySolved = map.updateSolve(c);
                double visited = map.numberVisited();
                double total = width * height;
                double percent = visited / total;
                percent = percent * 100;
                visitedLabel.setText("Visited: " + (int) percent + "%");
            }
            else if (isSolving && fullySolved){
                //finished solving
                time.stop();
                map.removeFromContainer(c);
                map.addToContainer(c);
                statusLabel.setText("Fully Solved Maze");
                map.setEnd(c);
            }

        }

    }

    public void stateChanged(ChangeEvent e) {
        //get data from sliders
        JSlider slided = (JSlider) e.getSource();
        double value = slided.getValue();
        if (slided.getName().equals("speed")){
            speedFactor = (int) (1000 - value);
            time.setDelay(speedFactor);
        }
        else if (slided.getName().equals("height")){
            height = (int) value;
            side.setHeight(height);
        }
        else if (slided.getName().equals("width")){
            width = (int) value;
            side.setWidth(width);
        }

    }

    public static void main(String args[])
    {
        Maze M = new Maze();
        M.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { System.exit(0); }
        });
    }

}
