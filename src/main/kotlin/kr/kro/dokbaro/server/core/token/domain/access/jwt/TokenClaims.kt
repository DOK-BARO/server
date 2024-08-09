package kr.kro.dokbaro.server.core.token.domain.access.jwt

data class TokenClaims(
	val id: String,
	val role: Collection<String>,
) {
	companion object {
		@Suppress("UNCHECKED_CAST")
		fun from(attributes: Map<String, Any>): TokenClaims =
			TokenClaims(
				attributes["id"] as String,
				attributes["role"] as ArrayList<String>,
			)
	}
}