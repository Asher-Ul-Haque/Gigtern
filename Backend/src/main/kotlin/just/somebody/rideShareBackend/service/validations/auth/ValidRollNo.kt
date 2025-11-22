package just.somebody.rideShareBackend.service.validations.auth

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import just.somebody.rideShareBackend.utils.Logger
import java.time.Year
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [RollNoValidator::class])
annotation class ValidRollNo(
	val message: String                       = "Roll number must be a valid 7-digit IIITD roll number from the last 5 years.",
	val groups : Array<KClass<*>>             = [],
	val payload: Array<KClass<out Payload>>   = [])

class RollNoValidator : ConstraintValidator<ValidRollNo, Int>
{
	// - - - check which years are allowed
	private val currentYear = Year.now().value
	private val validYears  = (currentYear - 4..currentYear).toSet()

	override fun isValid(ROLL_NO: Int, CONTEXT: ConstraintValidatorContext?): Boolean
	{
		val rollNoStr = ROLL_NO.toString()

		// - - - Check Length (Must be 7 digits)
		if (rollNoStr.length != 7)
		{
			Logger.LOG_ERROR("[Roll No Validator] : Roll number must be 7 digits, violation: $ROLL_NO")
			return false
		}

		// - - - Check Year Constraint (First 4 digits)
		val yearPart = rollNoStr.take(4).toIntOrNull()
		if (yearPart == null)
		{
			Logger.LOG_ERROR("[Roll no Validator] : Roll number must have a 4 digit year to start, violation: $ROLL_NO")
			return false
		}

		// - - - Check if the year is within the valid set
		if (yearPart !in validYears)
		{
			Logger.LOG_ERROR("[Roll no Validator] : Roll number must be in valid years : [NOW - 4, NOW], violation: $ROLL_NO")
			return false
		}

		// - - - If both length and year are valid, it passes
		return true
	}
}