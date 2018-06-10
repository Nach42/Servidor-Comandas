package edu.uclm.esi.disoft.comandas.etiquetas;

import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Target(FIELD)
public @interface JSONable {

	String campo() default "";

	String nombre() default "";

}
