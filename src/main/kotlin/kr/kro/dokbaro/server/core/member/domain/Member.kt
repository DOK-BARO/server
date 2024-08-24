package kr.kro.dokbaro.server.core.member.domain

import java.util.UUID

data class Member(
	val name: String?,
	val nickname: String?,
	val emails: Emails?,
	val profileImage: String?,
	val certificationId: UUID,
	val roles: Set<Role> = setOf(Role.GUEST),
	val id: Long = UNSAVED_MEMBER_ID,
) {
	companion object {
		private const val UNSAVED_MEMBER_ID = 0L
	}
}