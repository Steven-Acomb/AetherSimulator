package FieldViewer;

import javax.swing.JFrame;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.Dimension;
import java.awt.Toolkit;


public class FieldViewer {
    private int[] dims = this.getMaxFrameDimensions();
    private Equation fieldEquation;
    private int windowWidth;
    private int windowHeight;
    private int cellsWide;
    private int cellsHigh;
    private double cellScale;
    private int cellSize;
    private int div;
    private double xFirst;
    private double yFirst;
    private double xLast;
    private double yLast;
    
    public FieldViewer(Equation fieldEquation, int maxWindowSize, int div){
        this.fieldEquation = fieldEquation;
        this.div = div;
        this.windowWidth = maxWindowSize+dims[2];
        this.windowHeight = maxWindowSize+dims[3];
        this.cellSize = (maxWindowSize/this.div);
        this.cellsWide = this.cellSize*this.div;
        this.cellsHigh = this.cellSize*this.div;
        this.xFirst = 0;
        this.yFirst = 0;
        this.xLast = cellsWide;
        this.yLast = cellsHigh;
    }

    public FieldViewer(Equation fieldEquation, double xFirst, double yFirst, double xLast, double yLast){
        this.fieldEquation = fieldEquation;
        this.xFirst = xFirst;
        this.yFirst = yFirst;
        double xRange = Math.abs(xLast-this.xFirst);
        double yRange = Math.abs(yLast-this.yFirst);
        int div = 1;

        if( (yRange/xRange) > ((double)(this.dims[1])/((double)this.dims[0])) ){ //y limited
            this.cellsHigh = (this.dims[1]/div)*div;
            this.cellScale = yRange/(double)(this.cellsHigh-1.0);
            this.cellsWide = (int)(1.0+Math.ceil(xRange/this.cellScale));
            this.xLast = ((double)cellsWide-1.0)*this.cellScale - this.xFirst;
            this.yLast = yLast;
        }
        else{ //x limited
            this.cellsWide = (this.dims[0]/div)*div;
            this.cellScale = xRange/((double)this.cellsWide-1.0);
            this.cellsHigh = (int)(1.0+Math.ceil(yRange/this.cellScale));
            this.xLast = xLast;
            this.yLast = ((double)cellsHigh-1.0)*this.cellScale - this.yFirst;
        }
        this.windowWidth = this.cellsWide+this.dims[2];
        this.windowHeight = this.cellsHigh+this.dims[3];
        this.cellSize = 1;
    }
    
    public void show(){
        JFrame frame = new JFrame();
        frame.setSize(this.windowWidth,this.windowHeight);
        // frame.setSize(1500,750);
        frame.setTitle("Field");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FieldComponent fc = new FieldComponent(this.cellsWide,this.cellsHigh,this.cellScale,this.cellSize,this.fieldEquation,this.xFirst, this.yFirst, this.xLast, this.yLast);
        // FieldComponent fc = new FieldComponent(3,3,750/3,750/3,this.fieldEquation,0,0,1500,750);
        frame.add(fc);
        // frame.pack();
        frame.setVisible(true);
    }

    public int[] getMaxFrameDimensions(){
        int[] dims = new int[4];
        JFrame test = new JFrame();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        test.setSize(screenSize.width,screenSize.height);
        test.setTitle("Test");
        test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        test.pack();
        Insets insets = test.getInsets();

        dims[0] = screenSize.width-(insets.left+insets.right);
        dims[1] = screenSize.height-(insets.top+insets.bottom);
        dims[2] = insets.left+insets.right;
        dims[3] = insets.top+insets.bottom;
        test.dispose();
        return dims;
    }

    public static void main(String args[]) {
        Equation eq = new Equation(2*314.15926535,420.69);
        
        // int div = 2;
        // int maxFrameSize = 630;
        // FieldViewer fv = new FieldViewer(eq, maxFrameSize, div);

        double xFirst = 0.0;
        double yFirst = 0.0;
        double xLast = 1521.0;
        double yLast = 826.0;
        // double xLast = 1521.0;
        // double yLast = 826.0;
        FieldViewer fv = new FieldViewer(eq, xFirst, yFirst, xLast, yLast);
        fv.show();
        // System.out.println(Math.atan(1));
    }
}