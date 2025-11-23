package just.somebody.gigtern.service

import just.somebody.gigtern.controllers.dtos.AuthRequestDTO
import just.somebody.gigtern.controllers.dtos.AuthResponseDTO
import just.somebody.gigtern.controllers.dtos.GigRequestDTO
import just.somebody.gigtern.controllers.dtos.GigResponseDTO
import just.somebody.gigtern.domain.entities.EmployerEntity
import just.somebody.gigtern.domain.entities.GigEntity
import just.somebody.gigtern.domain.entities.UserEntity

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

fun GigRequestDTO.toEntity(employer: EmployerEntity): GigEntity
{
	return GigEntity(
		employer = employer,
		title                 = this.title,
		description           = this.description,
		paymentType           = this.paymentType,
		payRate               = this.payRate,
		durationEstimateHours = this.durationEstimateHours,
		skillsRequired        = this.skillsRequired,
	)
}

fun GigEntity.toDTO(): GigResponseDTO
{
	return GigResponseDTO(
		id                    = this.id,
		employerId            = this.employer.id,
		title                 = this.title,
		description           = this.description,
		paymentType           = this.paymentType,
		payRate               = this.payRate,
		skillsRequired        = this.skillsRequired,
		postedDate            = this.postedDate,
		employerCompanyName   = this.employer.user.name,
		durationEstimateHours = this.durationEstimateHours,
		status                = this.status)
}