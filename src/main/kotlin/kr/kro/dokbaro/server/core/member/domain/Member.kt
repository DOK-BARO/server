package kr.kro.dokbaro.server.core.member.domain

import kr.kro.dokbaro.server.common.constant.Constants
import java.util.UUID

data class Member(
	var nickName: String,
	var email: Email,
	var profileImage: String?,
	val certificationId: UUID,
	val roles: Set<Role> = setOf(Role.GUEST),
	val id: Long = Constants.UNSAVED_ID,
) {
	fun modify(
		nickName: String?,
		email: Email?,
		profileImage: String?,
	) {
		this.nickName = nickName ?: this.nickName
		this.email = email ?: this.email
		this.profileImage = profileImage ?: this.profileImage
	}
}