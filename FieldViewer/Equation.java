package FieldViewer;

import java.lang.Math;

public class Equation {
    private double sourceX;
    private double sourceY;

    public Equation(double x, double y){
        this.sourceX = x;
        this.sourceY = y;
    }

    public double[] evaluate(double[] position){
        double x = position[0];
        double y = position[1];
        // System.out.println("x = " + x + " y = " + y);
        double[] vE = new double[2];
        double q = -16.0217663;//10^-15
        double E = Math.abs(q/(4*(3.14159265358979)*(8.854)))/Math.pow(Math.sqrt(Math.pow(x-this.sourceX,2)+Math.pow(y-this.sourceY,2)),3)*Math.pow(10,7);
        vE[0] = E*(x-this.sourceX);
        vE[1] = E*(y-this.sourceY);

        double E2 = Math.abs(q/(4*(3.14159265358979)*(8.854)))/Math.pow(Math.sqrt(Math.pow(x-100,2)+Math.pow(y-100,2)),3)*Math.pow(10,7);
        vE[0] = vE[0] + E2*(x-100);
        vE[1] = vE[1] + E2*(y-100);

        double E3 = Math.abs(-q/(4*(3.14159265358979)*(8.854)))/Math.pow(Math.sqrt(Math.pow(x-900,2)+Math.pow(y-500,2)),3)*Math.pow(10,7);
        vE[0] = vE[0] + E3*(x-900);
        vE[1] = vE[1] + E3*(y-500);
        return vE;
    }
}

