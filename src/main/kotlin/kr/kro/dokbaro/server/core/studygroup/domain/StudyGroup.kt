package kr.kro.dokbaro.server.core.studygroup.domain

import kr.kro.dokbaro.server.common.constant.Constants

class StudyGroup(
	val name: String,
	val introduction: String,
	val profileImageUrl: String? = null,
	val studyMembers: MutableSet<StudyMember> = mutableSetOf(),
	val inviteCode: InviteCode,
	val id: Long = Constants.UNSAVED_ID,
) {
	constructor(
		name: String,
		introduction: String,
		profileImageUrl: String,
		creatorId: Long,
		inviteCode: InviteCode,
	) : this(
		name,
		introduction,
		profileImageUrl,
		mutableSetOf(
			StudyMember(
				creatorId,
				StudyMemberRole.LEADER,
			),
		),
		inviteCode,
	)

	fun join(participantId: Long) {
		studyMembers.add(StudyMember(participantId, StudyMemberRole.MEMBER))
	}
}