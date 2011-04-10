package everfeeds.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by
 * User: boris
 * Date: 10.4.2011
 *
 * пока его не стоит трогать во всех местах, где он есть
 * работу свою он(временно) не выполяет.
 * Написан для того, чтоб не забыть
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Reconnectable {}
