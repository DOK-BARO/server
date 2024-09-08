package kr.kro.dokbaro.server.core.studygroup.domain

class StudyGroup(
	val name: String,
	val introduction: String,
	val profileImageUrl: String,
	val studyMembers: MutableSet<StudyMember> = mutableSetOf(),
	val id: Long = UNSAVED_STUDY_GROUP_ID,
) {
	companion object {
		private const val UNSAVED_STUDY_GROUP_ID = 1L
	}

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