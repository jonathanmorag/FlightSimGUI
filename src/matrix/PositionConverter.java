package matrix;

public class PositionConverter {
	//32.0231110   ,  35.0352310
	public static Position convert(double x, double y,double startX,double startY) {
//		System.out.println(x + ", "+ y + " has converted to: " + (int)Math.floor((x-startX)*100) + ", " + (int)Math.floor((y-startY)*100));
		return new Position((int)Math.floor((x-startX)*100)+1,(int)Math.floor((y-startY)*100)); // 0.01->0.05/0.05 , 0.16->0.8/0.05
	}
	
}
