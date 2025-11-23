package just.somebody.gigtern.controllers.dtos.requests

import jakarta.validation.constraints.NotBlank

data class ApplicationRequestDTO(
	@field:NotBlank(message = "Cover letter is required for an application")
	val coverLetter: String)