package just.somebody.gigtern.controllers.dtos.responses

import just.somebody.gigtern.domain.enums.Role

data class AuthResponseDTO(
	val jwt     : String,
	val userId  : Long,
	val role    : Role,
	val message : String?)