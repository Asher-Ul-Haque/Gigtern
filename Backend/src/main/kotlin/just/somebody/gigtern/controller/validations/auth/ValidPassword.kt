package just.somebody.gigtern.controller.validations.auth

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [PasswordValidator::class])
annotation class ValidPassword(
	val message : String                      = "Password must contain at least one uppercase letter, one lowercase letter, and one digit and be between 8 and 20 characters",
	val groups  : Array<KClass<*>>            = [],
	val payload : Array<KClass<out Payload>>  = [])

class PasswordValidator : ConstraintValidator<ValidPassword, String>
{
	private val minSize   = 8
	private val maxSize   = 20
	private val regex     = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$".toRegex()

	override fun isValid(PASSWORD: String?, CONTEXT: ConstraintValidatorContext?): Boolean
	{
		if (PASSWORD == null)                           return false;
		if (PASSWORD.length in minSize..maxSize)  return true;
		return false;
	}
}
