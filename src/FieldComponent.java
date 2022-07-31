import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JComponent;

public class FieldComponent extends JComponent{
	private final int DIMENSIONS = 2;

	private int cellsWide;
	private int cellsHigh;
	private int cellSize;
	private Cell[][] cells;
	private Equation fieldEquation;
	// private double[][] cellPositions;

	private double[][] magnitudes;
	private double[][][] directions;
	private double[][] angles;
	private double[][] normVal;
	private double[][][] hslVal;	
	private int[][][] rgbVal;

	public FieldComponent(int cellsWide, int cellsHigh, double cellScale, int cellSize, Equation fieldEquation, double xFirst, double yFirst, double xLast, double yLast){
		this.cellsWide = cellsWide;
		this.cellsHigh = cellsHigh;
		this.cellSize = cellSize;
		this.fieldEquation = fieldEquation;

		this.cells = new Cell[this.cellsHigh][this.cellsWide];
		for(int i = 0;i<this.cellsHigh;i++){
			for(int j = 0;j<this.cellsWide;j++)
				cells[i][j] = new Cell(j*this.cellSize,i*this.cellSize,this.cellSize+1,this.cellSize+1);
		}

		//double[] test = this.toUnitVector(new double[]{0,1}, magnitude);

		this.setCellPositions(xFirst, yFirst, xLast, yLast);
		this.setCellValues();
		this.normVal = this.normalize(this.magnitudes);
		// this.rgbVal = this.calculateRGB(this.normVal);
		this.hslVal = this.calculateHSL(this.normVal,this.directions);
		// this.rgbVal = this.calculateRGB(this.normVal,this.directions);
		this.rgbVal = this.HSLtoRGB(this.hslVal);
		this.setCellColors(rgbVal);
	}

	public double[][] normalize(double[][] input){
		double[][] normalized = new double[input.length][input[0].length];
		double[] ext = getMinMax(input); 
		for(int i = 0;i<input.length;i++){
			for(int j = 0;j<input[0].length;j++)
				normalized[i][j] = normalizeValue(input[i][j],ext[0],ext[1],"log");
		}
		return normalized;
	}

	public int[][][] calculateRGB(double[][] norm, double[][][] dir){
		int[][][] output = new int[norm.length][norm[0].length][3];
		for(int i = 0;i<norm.length;i++){
			for(int j = 0;j<norm[0].length;j++){
				output[i][j][0] = (int)(255 * norm[i][j]);
				output[i][j][1] = (int)(255 * norm[i][j]);
				output[i][j][2] = (int)(255 * norm[i][j]);
			}
		}
		return output;
	}

	public double[][][] calculateHSL(double[][] norm, double[][][] dir){
		double[][][] output = new double[norm.length][norm[0].length][3];
		for(int i = 0;i<norm.length;i++){
			for(int j = 0;j<norm[0].length;j++){
				output[i][j][0] = this.angles[i][j];
				output[i][j][1] = 100;
				output[i][j][2] = 50 * norm[i][j];
				// output[i][j][2] = 100 * norm[i][j];
			}
		}
		return output;
	}

	public double normalizeValue(double input, double min, double max, String... args){
		boolean isLog = false;
		for(String a : args){
			if(a.equals("log"))
				isLog = true;
		}
		if(isLog)
			return (Math.log(input)-Math.log(min))/(Math.log(max)-Math.log(min));
		return (input-min)/(max-min);
	}

	public double[] getMinMax(double[][] input){
		double highest = Double.MIN_VALUE;
		double lowest = Double.MAX_VALUE;
		double[] extrema = new double[2];
		for(int i = 0;i<input.length;i++){
			for(int j = 0;j<input[0].length;j++){
				if(input[i][j] > highest)
					highest = input[i][j];
				if(input[i][j] < lowest)
					lowest = input[i][j];
			}
		}
		extrema[0] = lowest;
		extrema[1] = highest;
		return extrema;
	}

	public void setCellColors(int[][][] rgb){
		for(int i = 0;i<this.cellsHigh;i++){
			for(int j = 0;j<this.cellsWide;j++)
				this.cells[i][j].setColor(new Color(rgb[i][j][0],rgb[i][j][1],rgb[i][j][2]));
		}
	}

	public void setCellPositions(double xFirst, double yFirst, double xLast, double yLast){
		double xRange = xLast-xFirst;
        double yRange = yLast-yFirst;
		double[] position = new double[this.DIMENSIONS];
		for(int i = 0;i<this.cellsHigh;i++){
			for(int j = 0;j<this.cellsWide;j++){
				position[0] = xFirst+xRange*j/(this.cellsWide-1);
				position[1] = yFirst+yRange*i/(this.cellsHigh-1);
				this.cells[i][j].setPosition(position);	
			}
		}
	}

	public void setCellValues(){
		this.magnitudes = new double[this.cellsHigh][this.cellsWide];
		this.directions = new double[this.cellsHigh][this.cellsWide][this.DIMENSIONS];
		this.angles = new double[this.cellsHigh][this.cellsWide];
		for(int i = 0;i<this.cellsHigh;i++){
			for(int j = 0;j<this.cellsWide;j++){
				this.cells[i][j].setValue(this.fieldEquation.evaluate(this.cells[i][j].getPosition()));
				this.magnitudes[i][j] = this.vectorMag(this.cells[i][j].getValue());
				this.directions[i][j] = this.toUnitVector(this.cells[i][j].getValue(),magnitudes[i][j]);
				this.angles[i][j] = this.toAngle(this.directions[i][j]);
			}
		}
	}

	public double vectorMag(double[] vector){
		double sum = 0;
		for(double v : vector)
			sum+=(v*v);
		return Math.sqrt(sum);
	}

	public double toAngle(double[] unit){
		double x = unit[0];
		double y = unit[1];
		if(x == 0)
			return 90.0;
		double tan = Math.atan(y/x);
		double degree = 180+180*tan/Math.PI;
		if(x<0)
			return (180+degree)%360;
		else
			return (360+degree)%360;
	}

	public double[] toUnitVector(double[] vector,double magnitude){
		double[] unit = new double[vector.length];
		for(int i = 0;i<vector.length;i++){
			unit[i] = vector[i]/magnitude;
		}
		return unit;
	}

	public int[][][] HSLtoRGB(double[][][] hsl){
		int[][][] rgb = new int[this.cellsHigh][this.cellsWide][3];
		double h;
		double s;
		double l;
		double C;
		double X;
		double m;
		double R;
		double G;
		double B;
		double r;
		double g;
		double b;
		for(int i = 0;i<this.cellsHigh;i++){
			for(int j = 0;j<this.cellsWide;j++){
				h = hsl[i][j][0];
				s = hsl[i][j][1]/100.0;
				l = hsl[i][j][2]/100.0;
				C = (1-Math.abs(2*l-1)*s);
				X = C*(1-Math.abs(((h/60)%2)-1));
				m = l-C/2;
				if(h>=0 && h<60){
					R = C;
					G = X;
					B = 0.0;
				}
				else if(h>=60 && h<120){
					R = X;
					G = C;
					B = 0.0;
				}
				else if(h>=120 && h<180){
					R = 0.0;
					G = C;
					B = X;
				}
				else if(h>=180 && h<240){
					R = 0.0;
					G = X;
					B = C;
				}
				else if(h>=240 && h<300){
					R = X;
					G = 0.0;
					B = C;
				}
				else{
					R = C;
					G = 0.0;
					B = X;
				}
				r = (R+m)*255;
				g = (G+m)*255;
				b = (B+m)*255;
				// System.out.println("h = " + h + " s = " + s + " l = " + l);
				// System.out.println("r = " + r + " g = " + g + " b = " + b);
				rgb[i][j][0] = (int)r;
				rgb[i][j][1] = (int)g;
				rgb[i][j][2] = (int)b;
			}
		}
		return rgb;
	}

    @Override
	protected void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D graphics2 = (Graphics2D) graphics;
		for(int i = 0;i<this.cellsHigh;i++){
			for(int j = 0;j<this.cellsWide;j++)
				cells[i][j].drawOn(graphics2);
		}
	}
}