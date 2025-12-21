package just.somebody.gigtern.controller.dtos.requests

import just.somebody.gigtern.controller.validations.auth.ValidEmail
import just.somebody.gigtern.controller.validations.auth.ValidPassword

data class LoginRequest(
	@ValidEmail     val email   : String,
	@ValidPassword  val password: String)

