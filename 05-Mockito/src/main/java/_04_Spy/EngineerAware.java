package _04_Spy;

public interface EngineerAware {
	Engineer getDesignation();
	
	enum Engineer {
		DEV,QA
	}
}