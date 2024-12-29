package kr.kro.dokbaro.server.core.member.query

import kr.kro.dokbaro.server.core.member.domain.AccountType
import java.util.UUID

data class MyAvatar(
	val id: Long,
	val certificationId: UUID,
	val nickname: String,
	val email: String?,
	val profileImage: String?,
	val role: Collection<String>,
	val accountType: AccountType,
)