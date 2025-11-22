package just.somebody.rideShareBackend.service.validations.auth

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import org.hibernate.validator.constraints.Length
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [PhoneValidator::class])
annotation class ValidPhone(
	val message : String                      = "Phone Number must be 10 digits",
	val groups  : Array<KClass<*>>            = [],
	val payload : Array<KClass<out Payload>>  = [])

class PhoneValidator : ConstraintValidator<ValidPhone, Long>
{
	override fun isValid(PHONE: Long?, CONTEXT: ConstraintValidatorContext?): Boolean
	{
		return PHONE == null || PHONE.toString().length == 10
	}
}
