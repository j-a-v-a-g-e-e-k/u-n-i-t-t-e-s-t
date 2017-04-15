import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;

public class ExampleThatLogs {

    private static final Logger LOG = (Logger) LoggerFactory.getLogger(ExampleThatLogs.class);

    public String concat(String a, String b) {
        LOG.info(a+b);
        return a+b;
    }

	private static Logger logger = (Logger) LoggerFactory.getLogger(ExampleThatLogs.class);

    @SuppressWarnings("unchecked")
	@Test
    public void testSomething() {
    ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
    final Appender mockAppender = mock(Appender.class);
    when(mockAppender.getName()).thenReturn("MOCK");
    root.addAppender(mockAppender);

    ExampleThatLogs classUnderTest = new ExampleThatLogs();
    classUnderTest.concat("bimal", "jain");

    verify(mockAppender).doAppend(argThat(new ArgumentMatcher() {
      @Override
      public boolean matches(final Object argument) {
        return ((LoggingEvent)argument).getFormattedMessage().contains("bimaljain");
      }
    }));
    
    verify(mockAppender).doAppend(argThat(new ArgumentMatcher() {
        @Override
        public boolean matches(final Object argument) {
          return ((LoggingEvent)argument).getLevel().equals(Level.INFO);
        }
      }));
  }
}
