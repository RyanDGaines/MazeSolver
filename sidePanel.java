import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionListener;

public class sidePanel {
    private JButton generate, solve, stop;
    private JCheckBox generateAnimate, solveAnimate;
    private JLabel widthLabel, heightLabel, speedLabel;
    private JSlider widthSlider, heightSlider, speedSlider;
    private JPanel buttonPanel, sliderPanel;

    public sidePanel(int width, int height){
        speedSlider = new JSlider();
        widthLabel = new JLabel("Width: " + String.valueOf(width));
        widthLabel.setHorizontalAlignment(SwingConstants.CENTER);
        heightLabel = new JLabel("Height: "+ String.valueOf(height));
        heightLabel.setHorizontalAlignment(SwingConstants.CENTER);
        speedLabel = new JLabel("Speed:");
        speedLabel.setHorizontalAlignment(SwingConstants.CENTER);

        heightSlider = new JSlider();
        heightSlider.setMinimum(10);
        heightSlider.setMaximum(50);
        heightSlider.setMajorTickSpacing(10);
        heightSlider.setMinorTickSpacing(5);
        heightSlider.setPaintTicks(true);
        heightSlider.setValue(height);
        heightSlider.setName("height");

        widthSlider = new JSlider();
        widthSlider.setMinimum(10);
        widthSlider.setMaximum(50);
        widthSlider.setMajorTickSpacing(10);
        widthSlider.setMinorTickSpacing(5);
        widthSlider.setPaintTicks(true);
        widthSlider.setValue(width);
        widthSlider.setName("width");

        speedSlider.setMinimum(0);
        speedSlider.setMaximum(1000);
        speedSlider.setMajorTickSpacing(100);
        speedSlider.setPaintTicks(true);
        speedSlider.setValue(500);
        speedSlider.setName("speed");

        generate = new JButton("Generate");
        generateAnimate = new JCheckBox("Animate");
        generateAnimate.setActionCommand("generateAnimate");
        solve = new JButton("Solve");
        solveAnimate = new JCheckBox("Animate");
        solveAnimate.setActionCommand("solveAnimate");

        buttonPanel = new JPanel();
        buttonPanel.setLocation(new Point(500,0));
        buttonPanel.setSize(new Dimension(200,100));
        buttonPanel.setLayout(new GridLayout(2,2));
        buttonPanel.add(generate);
        buttonPanel.add(generateAnimate);
        buttonPanel.add(solve);
        buttonPanel.add(solveAnimate);

        sliderPanel = new JPanel();
        sliderPanel.setLocation(new Point(500,150));
        sliderPanel.setSize(new Dimension(200,300));
        sliderPanel.setLayout(new GridLayout(6,1,0,-25));
        sliderPanel.add(widthLabel);
        sliderPanel.add(widthSlider);
        sliderPanel.add(heightLabel);
        sliderPanel.add(heightSlider);
        sliderPanel.add(speedLabel);
        sliderPanel.add(speedSlider);

        stop = new JButton("Stop");
        stop.setSize(new Dimension(200,75));
        stop.setLocation(new Point(500,500));


    }

    public void addActionListener(ActionListener L){
        stop.addActionListener(L);
        generate.addActionListener(L);
        generateAnimate.addActionListener(L);
        solve.addActionListener(L);
        solveAnimate.addActionListener(L);
    }

    public void addChangeListener(ChangeListener L){
        heightSlider.addChangeListener(L);
        widthSlider.addChangeListener(L);
        speedSlider.addChangeListener(L);
    }

    public void addToContainer(Container C){
        C.add(sliderPanel);
        C.add(buttonPanel);
        C.add(stop);
    }

    public void setHeight(int height) {
        heightLabel.setText("Height: " + height);
    }

    public void setWidth(int width) {
        widthLabel.setText("Width: " + width);
    }
}
