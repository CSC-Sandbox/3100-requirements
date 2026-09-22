public class RobotPose {
    private double J1, J2, J3, J4, J5, J6, X, Y, Z;
    public RobotPose(double J1, double J2, double J3, double J4, double J5, double J6, double X, double Y, double Z) {
        this.J1 = J1;
        this.J2 = J2;
        this.J3 = J3;
        this.J4 = J4;
        this.J5 = J5;
        this.J6 = J6;
        this.X = X;
        this.Y = Y;
        this.Z = Z;
    }

    public double[] getValues() {
        return new double[]{J1, J2, J3, J4, J5, J6, X, Y, Z};
    }

    public void setValues(double[] values) {
        if(values.length != 9) {
            throw new IllegalArgumentException("Values array length out of range");
        }
        this.J1 = values[0];
        this.J2 = values[1];
        this.J3 = values[2];
        this.J4 = values[3];
        this.J5 = values[4];
        this.J6 = values[5];
        this.X = values[6];
        this.Y = values[7];
        this.Z = values[8];
    }

    @Override
    public String toString() {
        double[] vals = getValues();
        String valsString = "ROBOT,";
        for(int i = 0; i < vals.length; i++){
            valsString = valsString + vals[i];
            if(i < vals.length-1){
                valsString = valsString + ",";
            }
        }
        return valsString;
    }
}
