package kr.kro.dokbaro.server.core.studygroup.domain

import kr.kro.dokbaro.server.common.constant.Constants

class StudyGroup(
	val id: Long = Constants.UNSAVED_ID,
	val name: String,
	val introduction: String? = null,
	val profileImageUrl: String? = null,
	val studyMembers: MutableSet<StudyMember> = mutableSetOf(),
	val inviteCode: InviteCode,
) {
	companion object {
		fun of(
			name: String,
			introduction: String?,
			profileImageUrl: String?,
			creatorId: Long,
			inviteCode: InviteCode,
		) = StudyGroup(
			name = name,
			introduction = introduction,
			profileImageUrl = profileImageUrl,
			studyMembers =
				mutableSetOf(
					StudyMember(
						creatorId,
						StudyMemberRole.LEADER,
					),
				),
			inviteCode = inviteCode,
		)
	}

	fun join(participantId: Long) {
		studyMembers.add(
			StudyMember(
				memberId = participantId,
				role = StudyMemberRole.MEMBER,
			),
		)
	}
}