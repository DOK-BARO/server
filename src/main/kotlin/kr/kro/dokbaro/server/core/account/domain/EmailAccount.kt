package kr.kro.dokbaro.server.core.account.domain

import kr.kro.dokbaro.server.common.constant.Constants
import org.springframework.security.crypto.password.PasswordEncoder

data class EmailAccount(
	val id: Long = Constants.UNSAVED_ID,
	var email: String,
	var password: String,
	val memberId: Long,
) {
	companion object {
		fun of(
			rawPassword: String,
			email: String,
			memberId: Long,
			encoder: PasswordEncoder,
		) = EmailAccount(
			email = email,
			password = encoder.encode(rawPassword),
			memberId = memberId,
		)
	}

	fun changePassword(
		newPassword: String,
		encoder: PasswordEncoder,
	) {
		password = encoder.encode(newPassword)
	}

	fun match(
		rawPassword: String,
		encoder: PasswordEncoder,
	): Boolean = encoder.matches(rawPassword, password)

	fun changeEmail(newEmail: String) {
		this.email = newEmail
	}
}