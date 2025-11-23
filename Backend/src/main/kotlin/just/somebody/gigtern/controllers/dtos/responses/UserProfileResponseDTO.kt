package just.somebody.gigtern.controllers.dtos.responses

import just.somebody.gigtern.domain.enums.Role
import just.somebody.gigtern.domain.enums.VerificationStatus
import java.time.LocalDateTime

// - - - get user's full profile
data class UserProfileResponseDTO(
	// - - - Base User Fields
	val id                  : Long?,
	val name                : String,
	val email               : String,
	val oneLiner            : String?,
	val role                : Role,
	val verificationStatus  : VerificationStatus,
	val createdAt           : LocalDateTime,

	// - - - Student Specific Fields (Present only if role is STUDENT)
	val universityName  : String?       = null,
	val skills          : List<String>? = null,

	// - - - Employer Specific Fields (Present only if role is EMPLOYER)
	val companyContact: String? = null)