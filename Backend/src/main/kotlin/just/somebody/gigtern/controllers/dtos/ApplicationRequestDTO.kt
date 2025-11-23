package just.somebody.gigtern.controllers.dtos

import jakarta.validation.constraints.NotBlank
import just.somebody.gigtern.domain.entities.ApplicationEntity
import just.somebody.gigtern.domain.entities.GigEntity
import just.somebody.gigtern.domain.entities.StudentEntity

data class ApplicationRequestDTO(
	@field:NotBlank(message = "Cover letter is required for an application")
	val coverLetter: String,
) {
	/**
	 * Converts the DTO into a new ApplicationEntity, linking it to the provided Gig and Student.
	 */
	fun toEntity(gig: GigEntity, student: StudentEntity): ApplicationEntity {
		return ApplicationEntity(
			gig = gig,
			student = student,
			coverLetter = this.coverLetter
			// id, status, appliedDate, createdAt, updatedAt are handled by entity defaults/JPA
		)
	}
}