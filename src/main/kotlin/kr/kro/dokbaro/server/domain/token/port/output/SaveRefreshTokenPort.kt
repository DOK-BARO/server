package kr.kro.dokbaro.server.domain.token.port.output

interface SaveRefreshTokenPort {
	fun save(token: String)
}