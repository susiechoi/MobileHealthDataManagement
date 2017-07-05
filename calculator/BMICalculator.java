import javax.swing.JOptionPane;

public class BMICalculator {

	public static double calculate(double lbs, int ft, int inch){
		double kg = lbs*0.453592;
		System.out.println("kg is "+kg);
		double cm = ((ft*12)+inch)*2.54;
		System.out.println("cm is "+cm);
		return (kg/(cm*cm))*10000;
	}

	public static void main(String[] args){
		int weight = Integer.parseInt(JOptionPane.showInputDialog("Weight in pounds", "200"));
		String height = JOptionPane.showInputDialog("Height in feet'inch format", "5'3");
		String[] ftInch = height.split("'");
		int heightFt = Integer.parseInt(ftInch[0]);
		int heightInch = Integer.parseInt(ftInch[1]);
		System.out.println(calculate(weight, heightFt, heightInch));
	}

}
