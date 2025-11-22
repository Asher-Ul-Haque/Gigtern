package just.somebody.gigtern.controllers.validations.auth

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ReputationValidator::class])
annotation class ValidReputation(
	val message : String                      = "Reputation must be within 0 and 5",
	val groups  : Array<KClass<*>>            = [],
	val payload : Array<KClass<out Payload>>  = [])

class ReputationValidator : ConstraintValidator<ValidRate, Float>
{
	override fun isValid(REPUATION: Float?, CONTEXT: ConstraintValidatorContext?): Boolean
	{
		return REPUATION != null && REPUATION in 1.0f..5.0f
	}
}

