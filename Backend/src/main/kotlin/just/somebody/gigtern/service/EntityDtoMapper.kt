package just.somebody.gigtern.service

import just.somebody.gigtern.controllers.dtos.AuthRequestDTO
import just.somebody.gigtern.controllers.dtos.AuthResponseDTO
import just.somebody.gigtern.domain.entities.UserEntity
import java.time.LocalDateTime

fun AuthRequestDTO.toEntity(HASHED_PASSWORD : String) : UserEntity
{
	return UserEntity(
		name          = this.name,
		email         = this.email,
		passwordHash  = HASHED_PASSWORD,
		role          = this.role)
}

fun UserEntity.toResponseDTO(JWT: String, MESSAGE : String? = null) : AuthResponseDTO
{
	return AuthResponseDTO(
		jwt     = JWT,
		userId  = this.id,
		role    = this.role,
		message = MESSAGE)
}