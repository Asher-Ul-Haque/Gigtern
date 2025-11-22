package just.somebody.rideShareBackend.service.validations.ride

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import just.somebody.rideShareBackend.utils.Logger
import java.time.LocalDateTime
import java.time.Year
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [DepartureTimeValidator::class])
annotation class ValidDepartureTime(
	val message: String                       = "Departure Time must be within 12 hours of now",
	val groups : Array<KClass<*>>             = [],
	val payload: Array<KClass<out Payload>>   = [])

class DepartureTimeValidator : ConstraintValidator<ValidDepartureTime, LocalDateTime>
{
	override fun isValid(TIME: LocalDateTime, CONTEXT: ConstraintValidatorContext?): Boolean
	{
		// - - - check whether within 12 hours
		return LocalDateTime.now().plusHours(12) > TIME
	}
}