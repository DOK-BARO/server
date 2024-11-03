package kr.kro.dokbaro.server.core.token.application.port.out

import java.util.UUID

fun interface DeleteRefreshTokenPort {
	fun deleteBy(certificateId: UUID)
}