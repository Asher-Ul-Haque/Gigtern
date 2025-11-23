package just.somebody.gigtern.service

import just.somebody.gigtern.controllers.dtos.GigRequestDTO
import just.somebody.gigtern.controllers.dtos.GigResponseDTO
import just.somebody.gigtern.domain.entities.EmployerEntity
import just.somebody.gigtern.domain.entities.UserEntity
import just.somebody.gigtern.domain.enums.Role
import just.somebody.gigtern.domain.repositories.EmployerRepository
import just.somebody.gigtern.domain.repositories.GigRepository
import just.somebody.gigtern.utils.Logger
import just.somebody.gigtern.utils.exceptions.AuthorizationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class GigService(
	private val GIG_REPO      : GigRepository,
	private val EMPLOYER_REPO : EmployerRepository)
{

	private fun getEmployerByUser(USER: UserEntity): EmployerEntity
	{
		// - - - Check Role
		if (USER.role != Role.EMPLOYER)
		{
			Logger.LOG_ERROR("[Gig Service] User ${USER.id} attempted to post gig without EMPLOYER role.")
			throw AuthorizationException("Only Employers can post gigs.")
		}

		// - - - Fetch Employer Profile
		return EMPLOYER_REPO.findByUser(USER) ?: throw Exception("Employer profile not found for user ID ${USER.id}.")
	}


	@Transactional
	fun createGig(REQUEST : GigRequestDTO, AUTH_USER: UserEntity): GigResponseDTO
	{
		// - - - Ensure the user is an Employer and fetch their profile
		val employer = getEmployerByUser(AUTH_USER)

		// - - - Convert DTO to Entity and link the Employer
		val newGig = REQUEST.toEntity(employer)

		// - - - Save the new gig
		val savedGig = GIG_REPO.save(newGig)

		Logger.LOG_INFO("[Gig Service] Gig created successfully: ${savedGig.id} by Employer ${employer.id}.")

		return savedGig.toDTO()
	}

	// - - - get all gigs
	fun getAllGigs(): List<GigResponseDTO>
	{
		return GIG_REPO.findAll().map { it.toDTO() }
	}


	fun getMyGigs(AUTHENTICATED_USER: UserEntity): List<GigResponseDTO>
	{
		// - - - Ensure the user is an Employer and fetch their profile
		val employer = getEmployerByUser(AUTHENTICATED_USER)
		// - - - Fetch gigs using the Employer ID
		return GIG_REPO.findAllByEmployerId(employer.id).map{ it.toDTO() }
	}

	// V2: Add getGigById, updateGigStatus, etc., here.
}