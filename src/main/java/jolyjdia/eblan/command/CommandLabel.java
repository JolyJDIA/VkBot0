package jolyjdia.eblan.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CommandLabel {
    String[] alias();
    String usage() default "";
    String desc() default "";
    int minArg() default 0;
    int maxArg() default -1;
    boolean async() default false;
}
