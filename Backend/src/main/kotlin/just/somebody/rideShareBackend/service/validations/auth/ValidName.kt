package just.somebody.rideShareBackend.service.validations.auth

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [NameValidator::class])
annotation class ValidName(
	val message : String                      = "Email must be alphanumeric and end with @iiitd.ac.in",
	val groups  : Array<KClass<*>>            = [],
	val payload : Array<KClass<out Payload>>  = [])

class NameValidator : ConstraintValidator<ValidName, String>
{
	override fun isValid(NAME: String?, CONTEXT: ConstraintValidatorContext?): Boolean
	{
		return NAME != null && NAME.length in 5..50
	}
}

