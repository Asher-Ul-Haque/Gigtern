package just.somebody.rideShareBackend.service.dtos.auth

import just.somebody.rideShareBackend.domain.entities.UserEntity
import just.somebody.rideShareBackend.domain.enums.UserGender
import just.somebody.rideShareBackend.domain.enums.UserRole
import just.somebody.rideShareBackend.service.validations.auth.ValidEmail
import just.somebody.rideShareBackend.service.validations.auth.ValidName
import just.somebody.rideShareBackend.service.validations.auth.ValidPhone
import just.somebody.rideShareBackend.service.validations.auth.ValidRollNo

data class SignupRequestDTO(
	@field:ValidRollNo  val rollNo            : Int,
	@field:ValidName    val name              : String,
	@field:ValidEmail   val email             : String,
	@field:ValidPhone   val phoneNumber       : Long,
											val gender            : UserGender,
											val role              : UserRole)

public fun SignupRequestDTO.toEntity() : UserEntity
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
