package just.somebody.gigtern.service

import just.somebody.gigtern.controllers.dtos.requests.ApplicationRequestDTO
import just.somebody.gigtern.controllers.dtos.requests.ApplicationResponseDTO
import just.somebody.gigtern.controllers.dtos.requests.AuthRequestDTO
import just.somebody.gigtern.controllers.dtos.responses.AuthResponseDTO
import just.somebody.gigtern.controllers.dtos.requests.GigRequestDTO
import just.somebody.gigtern.controllers.dtos.responses.GigResponseDTO
import just.somebody.gigtern.controllers.dtos.responses.UserProfileResponseDTO
import just.somebody.gigtern.domain.entities.ApplicationEntity
import just.somebody.gigtern.domain.entities.EmployerEntity
import just.somebody.gigtern.domain.entities.GigEntity
import just.somebody.gigtern.domain.entities.StudentEntity
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

fun ApplicationRequestDTO.toEntity(GIG : GigEntity, STUDENT: StudentEntity) : ApplicationEntity
{
	return ApplicationEntity(
		gig         = GIG,
		student     = STUDENT,
		coverLetter = this.coverLetter)
}

fun ApplicationEntity.toResponseDTO() : ApplicationResponseDTO
{
	return ApplicationResponseDTO(
		id          = this.id,
		gigId       = this.gig.id,
		studentId   = this.student.id,
		coverLetter = this.coverLetter ?: "No cover letter provided.",
		status      = this.status,
		appliedDate = this.appliedDate,)
}

fun UserEntity.toProfileResponseDTO(
	STUDENT  : StudentEntity?   = null,
	EMPLOYER : EmployerEntity?  = null
) : UserProfileResponseDTO
{
	return UserProfileResponseDTO(
		id                  = this.id,
		name                = this.name,
		email               = this.email,
		oneLiner            = this.oneLiner,
		role                = this.role,
		verificationStatus  = this.verificationStatus,
		createdAt           = this.createdAt,

		// - - - Student fields (populated only if StudentEntity is provided)
		universityName  = STUDENT?.university,
		skills          = STUDENT?.skills,

		// - - - Employer fields (populated only if EmployerEntity is provided)
		companyContact = EMPLOYER?.pointOfContact
	)
}