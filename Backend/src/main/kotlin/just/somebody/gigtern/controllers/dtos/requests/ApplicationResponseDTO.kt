package just.somebody.gigtern.controllers.dtos.requests

import just.somebody.gigtern.domain.enums.ApplicationStatus
import java.time.LocalDateTime

data class ApplicationResponseDTO(
	val id          : Long,
	val gigId       : Long,
	val studentId   : Long,
	val coverLetter : String,
	val status      : ApplicationStatus,
	val appliedDate : LocalDateTime)