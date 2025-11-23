package just.somebody.gigtern.controllers.dtos

import just.somebody.gigtern.domain.entities.EmployerEntity
import just.somebody.gigtern.domain.entities.StudentEntity
import just.somebody.gigtern.domain.entities.UserEntity
import just.somebody.gigtern.domain.enums.Role
import just.somebody.gigtern.domain.enums.VerificationStatus
import java.time.LocalDateTime

/**
 * Consolidated DTO for retrieving the authenticated user's full profile (GET /api/v1/users/me).
 * This structure accommodates both Student and Employer roles.
 */
data class UserProfileResponseDTO(
	// Base User Fields
	val id: Long?,
	val name: String,
	val email: String,
	val oneLiner: String?,
	val role: Role,
	val verificationStatus: VerificationStatus,
	val createdAt: LocalDateTime,

	// Student Specific Fields (Present only if role is STUDENT)
	val universityName: String? = null,
	val skills: List<String>? = null,

	// Employer Specific Fields (Present only if role is EMPLOYER)
	val companyContact: String? = null,
)

/**
 * Extension function to convert consolidated entities into the DTO.
 */
fun UserEntity.toProfileResponseDTO(
	studentProfile: StudentEntity? = null,
	employerProfile: EmployerEntity? = null
): UserProfileResponseDTO {
	return UserProfileResponseDTO(
		id = this.id,
		name = this.name,
		email = this.email,
		oneLiner = this.oneLiner,
		role = this.role,
		verificationStatus = this.verificationStatus,
		createdAt = this.createdAt,

		// Student fields (populated only if StudentEntity is provided)
		universityName = studentProfile?.university,
		skills = studentProfile?.skills,

		// Employer fields (populated only if EmployerEntity is provided)
		companyContact = employerProfile?.pointOfContact
	)
}