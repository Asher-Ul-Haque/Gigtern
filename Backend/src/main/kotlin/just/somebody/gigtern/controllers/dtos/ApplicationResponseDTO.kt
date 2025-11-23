package just.somebody.gigtern.controllers.dtos

import just.somebody.gigtern.domain.entities.ApplicationEntity
import just.somebody.gigtern.domain.enums.ApplicationStatus
import java.time.LocalDateTime

data class ApplicationResponseDTO(
	val id: Long?,
	val gigId: Long?,
	val studentId: Long?,
	val coverLetter: String,
	val status: ApplicationStatus,
	val appliedDate: LocalDateTime,
)

/**
 * Extension function to convert ApplicationEntity to ApplicationResponseDTO.
 */
fun ApplicationEntity.toResponseDTO(): ApplicationResponseDTO {
	return ApplicationResponseDTO(
		id = this.id,
		gigId = this.gig.id,
		studentId = this.student.id,
		coverLetter = this.coverLetter ?: "No cover letter provided.",
		status = this.status,
		appliedDate = this.appliedDate,
	)
}