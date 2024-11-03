package kr.kro.dokbaro.server.core.token.application.port.out

import kr.kro.dokbaro.server.core.token.domain.refresh.RefreshToken

fun interface InsertRefreshTokenPort {
	fun insert(token: RefreshToken)
}