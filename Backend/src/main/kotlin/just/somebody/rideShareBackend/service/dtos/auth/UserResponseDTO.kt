package just.somebody.rideShareBackend.service.dtos.auth

import just.somebody.rideShareBackend.domain.entities.UserEntity
import just.somebody.rideShareBackend.domain.enums.UserGender
import just.somebody.rideShareBackend.domain.enums.UserRole
import just.somebody.rideShareBackend.service.validations.auth.ValidEmail
import just.somebody.rideShareBackend.service.validations.auth.ValidName
import just.somebody.rideShareBackend.service.validations.auth.ValidPhone
import just.somebody.rideShareBackend.service.validations.auth.ValidReputation
import just.somebody.rideShareBackend.service.validations.auth.ValidRollNo

data class UserResponseDTO(
	@field:ValidRollNo      val rollNo            : Int,
	@field:ValidName        val name              : String,
	@field:ValidEmail       val email             : String,
	@field:ValidPhone       val phoneNumber       : Long,
	@field:ValidReputation  val reputation        : Float,
													val gender            : UserGender,
													val role              : UserRole,
													val jwt               : String
)

public fun UserResponseDTO.toEntity() : UserEntity
{
	return UserEntity(
		rollNo            = this.rollNo,
		name              = this.name,
		email             = this.email,
		phoneNumber       = this.phoneNumber,
		gender            = this.gender,
		role              = this.role,
		cancellationRate  = 0.0f,
		onTimeRate        = 100.0f,
		reputationScore   = 3.5f)
}

public fun UserEntity.toResponseDTO(JWT: String) : UserResponseDTO
{
	return UserResponseDTO(
		rollNo            = this.rollNo,
		name              = this.name,
		email             = this.email,
		phoneNumber       = this.phoneNumber,
		gender            = this.gender,
		role              = this.role,
		reputation        = this.reputationScore,
		jwt               = JWT)
}

