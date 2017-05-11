/*
ArgumentCaptor class is used to capture argument values for further assertions. Mockito verifies argument values in natural java style: by using an equals() method. This is also the 
recommended way of matching arguments because it makes tests clean & simple. In some situations though, it is helpful to assert on certain arguments after the actual verification.

It is recommended to use ArgumentCaptor with verification but not with stubbing. Using ArgumentCaptor with stubbing may decrease test readability because captor is created outside of 
assert (aka verify or �then�) block. Also it may reduce defect localization because if stubbed method was not called then no argument is captured.
In a way ArgumentCaptor is related to custom argument matchers. Both techniques can be used for making sure certain arguments where passed to mocks. However, ArgumentCaptor may be a 
better fit if:
custom argument matcher is not likely to be reused
you just need it to assert on argument values to complete verification
Custom argument matchers via ArgumentMatcher are usually better for stubbing.

3.1 Methods
In this section we will describe the methods defined in the ArgumentCaptor class.
3.1.1 public T capture()

Use it to capture the argument. This method must be used inside of verification. Internally, this method registers a special implementation of an ArgumentMatcher. This argument matcher 
stores the argument value so that you can use it later to perform assertions.
3.1.2 public T getValue()

Returns the captured value of the argument. If the method was called multiple times then it returns the latest captured value.
3.1.3 public java.util.List<T> getAllValues()

Returns all captured values. Use it in case the verified method was called multiple times.

If you would have notice you will see that the UserMaintenanceService class is annotated with @InjectMocks. This marks a field on which injection should be performed. It minimizes 
repetitive mock and spy injection. Mockito will try to inject mocks only either by constructor injection, setter injection, or property injection in order and as described below. If 
any of the following strategy fail, then Mockito won�t report failure; i.e. you will have to provide dependencies yourself.
1. Constructor injection: the biggest constructor is chosen, then arguments are resolved with mocks declared in the test only. Note: If arguments can not be found, then null is passed. 
If non-mockable types are wanted, then constructor injection won�t happen. In these cases, you will have to satisfy dependencies yourself.
2. Property setter injection: mocks will first be resolved by type, then, if there is several property of the same type, by the match of the property name and the mock name. Note: If you 
have properties with the same type (or same erasure), it�s better to name all @Mock annotated fields with the matching properties, otherwise Mockito might get confused and injection 
won�t happen. If @InjectMocks instance wasn�t initialized before and have a no-arg constructor, then it will be initialized with this constructor.
3. Field injection mocks will first be resolved by type, then, if there is several property of the same type, by the match of the field name and the mock name. Note: If you have fields 
with the same type (or same erasure), it�s better to name all @Mock annotated fields with the matching fields, otherwise Mockito might get confused and injection won�t happen. If 
@InjectMocks instance wasn�t initialized before and have a no-arg constructor, then it will be initialized with this constructor.

In the first test method the first thing we do is call the MockitoAnnotations.initMocks() method. It initializes objects annotated with @Mock for given test class.
 */

package _01;

import java.util.Calendar;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import _08_ArgumentCaptor.IReportGenerator;
import _08_ArgumentCaptor.ReportEntity;
import _08_ArgumentCaptor.ReportGeneratorService;

public class _08_ArgumentCaptor {
	@InjectMocks private ReportGeneratorService classUnderTest;
	@Mock private IReportGenerator reportGenerator;
	@Captor private ArgumentCaptor<ReportEntity> reportEntityCaptor;

	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testGenerateReport(){
		Calendar startDate = Calendar.getInstance();
		startDate.set(2016, 11, 25);
		Calendar endDate = Calendar.getInstance();
		endDate.set(9999, 12, 31);
		String reportContent = "Report Content";
		classUnderTest.generateReport(startDate.getTime(), endDate.getTime(), reportContent.getBytes());
				
		Mockito.verify(reportGenerator).generateReport(reportEntityCaptor.capture());
		ReportEntity entity = reportEntityCaptor.getValue();
		Assert.assertEquals(entity.getStartDate().getDate(), 25);
		Assert.assertEquals(entity.getEndDate().getDate(), 31);
		Assert.assertEquals(new String(entity.getContent()), "Report Content");
	}
}
