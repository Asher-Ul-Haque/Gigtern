package just.somebody.gigtern.controllers.dtos.responses

import just.somebody.gigtern.domain.enums.GigStatus
import just.somebody.gigtern.domain.enums.PaymentType
import java.time.LocalDateTime

data class GigResponseDTO(
	val id                    : Long,
	val employerId            : Long?,
	val title                 : String,
	val description           : String,
	val paymentType           : PaymentType,
	val payRate               : Float,
	val status                : GigStatus,
	val skillsRequired        : List<String>,
	val postedDate            : LocalDateTime,
	val employerCompanyName   : String,
	val durationEstimateHours : Int? = null)
