package kr.kro.dokbaro.server.core.token.application.port.out

import kr.kro.dokbaro.server.core.token.domain.refresh.RefreshToken

fun interface LoadRefreshTokenPort {
	fun loadByToken(token: String): RefreshToken?
}