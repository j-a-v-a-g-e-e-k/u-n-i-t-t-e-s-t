package _10_Mock;

public class CalculatorApplication {

	private Adder adder;
	private Subtracter subtracter;
	private Multiplier multiplier;
	private Divisor divisor;
	
	public CalculatorApplication(Adder adder, Subtracter subtracter,
			Multiplier multiplier, Divisor divisor) {
		this.adder = adder;
		this.subtracter = subtracter;
		this.multiplier = multiplier;
		this.divisor = divisor;
	}
	
	public Object add(Object n1, Object n2) {
		Object result = this.adder.add(n1, n2);
		
		return result;
	}

	public Object subtract(Object n1, Object n2) {
		Object result = this.subtracter.subtract(n1, n2);
		
		return result;
	}

	public Object multiply(Object n1, Object n2) {
		Object result = this.multiplier.multiply(n1, n2);
		
		return result;
	}
	
	public Object divide(Object n1, Object n2) throws ArithmeticException {
		Object result = this.divisor.divide(n1, n2);
		
		return result;
	}
}
