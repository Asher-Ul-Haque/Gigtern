package just.somebody.gigtern.controllers.dtos

import just.somebody.gigtern.domain.enums.Role
import java.util.UUID

data class AuthResponseDTO(
	val jwt     : String,
	val userId  : UUID,
	val role    : Role,
	val message : String?)


