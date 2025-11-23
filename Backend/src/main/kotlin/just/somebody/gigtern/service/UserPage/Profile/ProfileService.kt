package just.somebody.gigtern.service

import just.somebody.gigtern.controllers.dtos.AuthRequestDTO
import just.somebody.gigtern.controllers.dtos.AuthResponseDTO
import just.somebody.gigtern.security.JwtProvider
import just.somebody.gigtern.service.exceptions.UserNotFoundException
import just.somebody.gigtern.utils.Logger
import just.somebody.gigtern.domain.entities.StudentEntity
import just.somebody.gigtern.domain.enums.Role
import just.somebody.gigtern.domain.repositories.StudentRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service


@Service

class ProfileService(private val STUDENT_REPOSITORY: StudentRepository,
                     private val JWT_PROVIDER : JwtProvider,
                     private val ENCODER : PasswordEncoder)
{
    fun addSocials(REQUEST : AuthRequestDTO) : AuthResponseDTO
    {
        val student : StudentEntity? = STUDENT_REPOSITORY.findByEmail(REQUEST.email)
        if(student == null)
        {
            Logger.LOG_ERROR("[Auth Service] : User not found for email : ${REQUEST.email}")
            throw UserNotFoundException("User not found for email : $REQUEST.email")
        }
        val jwt = JWT_PROVIDER.generateSocials(student.id, Role.STUDENT, student.linkedin_url)
        return student.toResponseDTO(JWT = jwt)
    }
}
