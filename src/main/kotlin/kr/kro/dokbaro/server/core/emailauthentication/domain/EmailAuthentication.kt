package kr.kro.dokbaro.server.core.emailauthentication.domain

class EmailAuthentication(
	val address: String,
	var code: String,
	var authenticated: Boolean = false,
	var used: Boolean = false,
	val id: Long = UNSAVED_EMAIL_AUTHENTICATION_ID,
) {
	companion object {
		private const val UNSAVED_EMAIL_AUTHENTICATION_ID = 0L
	}

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