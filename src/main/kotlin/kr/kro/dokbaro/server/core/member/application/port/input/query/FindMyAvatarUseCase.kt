package kr.kro.dokbaro.server.core.member.application.port.input.query

import kr.kro.dokbaro.server.core.member.query.MyAvatar
import java.util.UUID

fun interface FindMyAvatarUseCase {
	fun findMyAvatar(certificationId: UUID): MyAvatar
}