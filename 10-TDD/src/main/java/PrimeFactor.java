import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import junit.framework.Assert;

public class PrimeFactor {

	@SuppressWarnings("deprecation")
	@Test
	public void publ() {
		Assert.assertEquals(getPrimeFactors(1), Arrays.asList());
		Assert.assertEquals(getPrimeFactors(2), Arrays.asList(2));
		Assert.assertEquals(getPrimeFactors(3), Arrays.asList(3));
		Assert.assertEquals(getPrimeFactors(4), Arrays.asList(2,2));
		Assert.assertEquals(getPrimeFactors(5), Arrays.asList(5));
		Assert.assertEquals(getPrimeFactors(6), Arrays.asList(2,3));
		Assert.assertEquals(getPrimeFactors(7), Arrays.asList(7));
		Assert.assertEquals(getPrimeFactors(8), Arrays.asList(2,2,2));
		Assert.assertEquals(getPrimeFactors(9), Arrays.asList(3,3));
		Assert.assertEquals(getPrimeFactors(10), Arrays.asList(2,5));
		Assert.assertEquals(getPrimeFactors(25), Arrays.asList(5,5));
		Assert.assertEquals(getPrimeFactors(49), Arrays.asList(7,7));
		Assert.assertEquals(getPrimeFactors(2*2*3*3*5*5*9*9*13), Arrays.asList(2,2,3,3,3,3,3,3,5,5,13));
	}

	public List<Integer> getPrimeFactors(int i) {
		List<Integer> factorList = new ArrayList<Integer>(); 

		//			while (i%2==0){
		//				factorList.add(2);
		//				i=i/2;
		//			}
		//			while (i%3==0){
		//				factorList.add(3);
		//				i=i/3;
		//			}
		//			while (i%5==0){
		//				factorList.add(5);
		//				i=i/5;
		//			}
		//			if(i>1)
		//				factorList.add(i);

		// after refactoring the code
		int j=2;
		while(i>1){
			while (i%j==0){
				factorList.add(j);
				i=i/j;
			}
			j++;
		}		
		return factorList;
	}

}
