package _07_PowerMock_Private;

import java.util.Date;

public class PrivateMethodExample {
	public String getDetails() {
	    return "Mock private method example: " + iAmPrivate();
	  }

	  private String iAmPrivate() {
	    return new Date().toString();
	  }
}
