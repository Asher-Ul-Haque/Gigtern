package just.somebody.gigtern.controllers.dtos.requests

import jakarta.validation.constraints.NotEmpty
import just.somebody.gigtern.domain.enums.Role
import just.somebody.gigtern.controllers.validations.auth.ValidEmail
import just.somebody.gigtern.controllers.validations.auth.ValidPassword

data class AuthRequestDTO(
	@ValidEmail     val email     : String,
	@ValidPassword  val password  : String,
	@field:NotEmpty val name      : String,
									val role      : Role)