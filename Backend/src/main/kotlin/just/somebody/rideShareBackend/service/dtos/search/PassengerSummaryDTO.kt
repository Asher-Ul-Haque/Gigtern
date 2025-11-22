package just.somebody.rideShareBackend.service.dtos.search

import just.somebody.rideShareBackend.domain.enums.UserGender

data class PassengerSummaryDTO(
	val rollNo: Int,
	val name  : String,
	val gender: UserGender)