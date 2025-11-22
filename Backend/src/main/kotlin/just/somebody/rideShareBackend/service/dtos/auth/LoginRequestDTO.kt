package just.somebody.rideShareBackend.service.dtos.auth

import just.somebody.rideShareBackend.service.validations.auth.ValidRollNo

data class LoginRequestDTO(@field:ValidRollNo val rollNo: Int)