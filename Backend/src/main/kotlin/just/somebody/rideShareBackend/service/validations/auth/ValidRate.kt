package just.somebody.rideShareBackend.service.validations.auth

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [RateValidator::class])
annotation class ValidRate(
	val message : String                      = "Rate must be within 0 and 100",
	val groups  : Array<KClass<*>>            = [],
	val payload : Array<KClass<out Payload>>  = [])


class RateValidator : ConstraintValidator<ValidRate, Float>
{
	override fun isValid(RATE: Float?, CONTEXT: ConstraintValidatorContext?): Boolean
	{
		return RATE != null && RATE in 0.0f..100.0f
	}
}
