package kr.kro.dokbaro.server.domain.token

data class TokenClaims(
	val id: String,
	val role: Collection<String>,
) {
	companion object {
		fun attributes() =
			listOf(
				ClaimsAttribute("id", String::class.java),
				ClaimsAttribute("role", ArrayList::class.java),
			)

		@Suppress("UNCHECKED_CAST")
		fun from(attributes: Map<String, Any>): TokenClaims =
			TokenClaims(
				attributes["id"] as String,
				attributes["role"] as ArrayList<String>,
			)
	}
}