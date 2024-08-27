package kr.kro.dokbaro.server.core.member.domain

data class Email(
	val address: String,
	val verified: Boolean = false,
) {
	fun verify(): Email = Email(address, true)
}