package kr.kro.dokbaro.server.core.account.domain

import kr.kro.dokbaro.server.common.constant.Constants
import kr.kro.dokbaro.server.core.account.domain.exception.PasswordNotMatchException
import org.springframework.security.crypto.password.PasswordEncoder

data class AccountPassword(
	val id: Long = Constants.UNSAVED_ID,
	var password: String,
	val memberId: Long,
) {
	companion object {
		fun of(
			rawPassword: String,
			memberId: Long,
			encoder: PasswordEncoder,
		) = AccountPassword(
			password = encoder.encode(rawPassword),
			memberId = memberId,
		)
	}

	fun changePassword(
		oldPassword: String,
		newPassword: String,
		encoder: PasswordEncoder,
	) {
		if (!match(oldPassword, encoder)) {
			throw PasswordNotMatchException()
		}

		password = encoder.encode(newPassword)
	}

	fun match(
		rawPassword: String,
		encoder: PasswordEncoder,
	): Boolean = encoder.matches(rawPassword, password)
}