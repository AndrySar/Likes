import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by moiseev on 10.10.17.
 */

@Target({METHOD, TYPE})
@Retention(RUNTIME)
public @interface Repeating {
    int count() default 1000;
    boolean stopOnFailure() default true;
}
