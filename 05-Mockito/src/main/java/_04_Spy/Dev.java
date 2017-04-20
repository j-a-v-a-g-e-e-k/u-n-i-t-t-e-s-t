package _04_Spy;

public class Dev implements EngineerAware {
	@Override
	public Engineer getDesignation() {
		return Engineer.DEV;
	}		
}
