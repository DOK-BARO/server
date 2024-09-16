package kr.kro.dokbaro.server.core.studygroup.domain

import kr.kro.dokbaro.server.common.constant.Constants

class StudyGroup(
	val name: String,
	val introduction: String,
	val profileImageUrl: String,
	val studyMembers: MutableSet<StudyMember> = mutableSetOf(),
	val id: Long = Constants.UNSAVED_ID,
) {
	constructor(
		name: String,
		introduction: String,
		profileImageUrl: String,
		creatorId: Long,
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
	)
}