package just.somebody.gigtern.controller.dtos.requests

import just.somebody.gigtern.controller.validations.auth.ValidEmail
import just.somebody.gigtern.controller.validations.auth.ValidName
import just.somebody.gigtern.controller.validations.auth.ValidPassword

data class RegisterRequest(
	@ValidName      val name    : String,
	@ValidEmail     val email   : String,
	@ValidPassword  val password: String)
