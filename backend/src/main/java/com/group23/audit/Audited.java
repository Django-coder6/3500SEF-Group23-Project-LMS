package com.group23.audit;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks one successful, collection-point-scoped service mutation for auditing.
 *
 * <p>The target and collection point expressions are evaluated with Spring EL
 * after the method returns. {@code #result} is the returned object, and method
 * arguments are available by parameter name (or {@code #a0}, {@code #p0} when
 * parameter names are unavailable).</p>
 *
 * <p>Example for the future handover service:</p>
 *
 * <pre>{@code
 * @Audited(
 *     action = "handover.confirm",
 *     targetType = "parcel",
 *     targetIdExpression = "#result.parcelId",
 *     collectionPointIdExpression = "#result.collectionPointId"
 * )
 * public Handover confirm(PickupConfirmation request) { ... }
 * }</pre>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Audited {

    String action();

    String targetType();

    String targetIdExpression() default "#result.id";

    String collectionPointIdExpression() default "#result.collectionPointId";
}
