package just.somebody.gigtern.controllers.dtos

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import just.somebody.gigtern.domain.enums.PaymentType

data class GigRequestDTO(
	@field:NotBlank(message = "Title is required")
	val title                 : String,

	@field:NotBlank(message = "Description is required")
	val description           : String,
	val paymentType           : PaymentType,

	@field:Min(value = 1, message = "Pay rate must be greater than zero")
	val payRate               : Float,

	val durationEstimateHours : Int? = null,

	@field:NotEmpty
	val skillsRequired        : List<String> = emptyList()
)