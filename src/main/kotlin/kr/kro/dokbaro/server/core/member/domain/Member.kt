package kr.kro.dokbaro.server.core.member.domain

import kr.kro.dokbaro.server.common.constant.Constants
import java.util.UUID

data class Member(
	val id: Long = Constants.UNSAVED_ID,
	var nickname: String,
	var email: Email? = null,
	var profileImage: String?,
	val certificationId: UUID,
	val roles: Set<Role> = setOf(Role.GUEST),
	val accountType: AccountType,
	var withdraw: Boolean = false,
) {
	fun modify(
		nickName: String?,
		email: Email?,
		profileImage: String?,
	) {
		this.nickname = nickName ?: this.nickname
		this.email = email ?: this.email
		this.profileImage = profileImage ?: this.profileImage
	}

	fun withdraw() {
		withdraw = true
	}
}