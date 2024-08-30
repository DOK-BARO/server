package kr.kro.dokbaro.server.core.member.domain

data class Email(
	val address: String,
) {
	init {
		if (!isValidEmail(address)) {
			throw InvalidEmailFormatException(address)
		}
	}

	private fun isValidEmail(email: String): Boolean {
		val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
		
		return email.matches(emailRegex)
	}
}