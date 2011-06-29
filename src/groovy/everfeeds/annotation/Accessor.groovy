package everfeeds.annotation

import everfeeds.thrift.util.Type
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
/**
 * @author Dmitry Kurinskiy
 * @since 29.06.11 14:41
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface Accessor {
  Type value()
}