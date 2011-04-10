package everfeeds.annotations;

import java.util.*;
import javax.annotation.processing.*;
import javax.lang.model.element.*;


/**
 * Created by
 * User: boris
 * Date: 10.4.2011
 */
@SupportedAnnotationTypes(value= {"everfeeds.annotations.Reconnectable"})
public class ReconnectableProcessor extends AbstractProcessor {


  @Override
  public boolean process(Set<? extends TypeElement> typeElements, RoundEnvironment roundEnvironment) {
    //todo: implement me
    return true;
  }
}
