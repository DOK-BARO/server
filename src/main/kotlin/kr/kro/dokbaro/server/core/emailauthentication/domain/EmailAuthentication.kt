package kr.kro.dokbaro.server.core.emailauthentication.domain

import kr.kro.dokbaro.server.common.constant.Constants

class EmailAuthentication(
	val id: Long = Constants.UNSAVED_ID,
	val address: String,
	var code: String,
	var authenticated: Boolean = false,
	var used: Boolean = false,
) {
	fun use() {
		if (!authenticated) {
			throw UnauthenticatedEmailException(address)
		}

		used = true
	}

	fun matchCode(code: String): Boolean {
		if (this.code == code) {
			authenticated = true
		}

		return authenticated
	}

	fun changeCode(code: String) {
		this.code = code
	}
}