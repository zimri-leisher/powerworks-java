package powerworks.event;

import static java.lang.annotation.ElementType.METHOD;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * For use with EventListener classes. Remember to do
 * {@link EventManager#registerEventListener(EventListener)} on the class
 * declaring this method and be sure that the arguments match (Event e).
 */
@Target(METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventHandler {

    EventPriority priority() default EventPriority.MED;
}
