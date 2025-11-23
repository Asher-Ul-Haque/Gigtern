package just.somebody.gigtern.controllers.validations.auth

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [EmailValidator::class])
annotation class ValidEmail(
	val message : String                      = "Email must be alphanumeric and end with @iiitd.ac.in",
	val groups  : Array<KClass<*>>            = [],
	val payload : Array<KClass<out Payload>>  = []
)

class EmailValidator : ConstraintValidator<ValidEmail, String>
{
	private val regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$".toRegex()

	override fun isValid(EMAIL: String?, CONTEXT: ConstraintValidatorContext?): Boolean
	{
		if (EMAIL == null) return false
		return regex.matches(EMAIL)
	}
}
