package matrix;

public class PositionConverter {
	//32.0231110   ,  35.0352310
	public static Position convert(double x, double y) {
		return new Position((int)Math.floor((x-32.0131110)*100),(int)Math.floor((y-34.8752310)*100)); // 0.01->0.05/0.05 , 0.16->0.8/0.05
	}
	
}
