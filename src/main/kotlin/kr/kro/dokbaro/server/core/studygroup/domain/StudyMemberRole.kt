package kr.kro.dokbaro.server.core.studygroup.domain

enum class StudyMemberRole(
	val authorities: Set<StudyMemberRoleAuthority> = setOf(),
) {
	LEADER(
		setOf(
			StudyMemberRoleAuthority.CREATE_QUIZ,
			StudyMemberRoleAuthority.SOLVE_QUIZ,
			StudyMemberRoleAuthority.INVITE_MEMBER,
			StudyMemberRoleAuthority.APPROVE_MEMBER,
			StudyMemberRoleAuthority.REMOVE_MEMBER,
			StudyMemberRoleAuthority.CHANGE_STUDY_INFO,
			StudyMemberRoleAuthority.CHANGE_LEADER,
			StudyMemberRoleAuthority.DELETE_STUDY_GROUP,
		),
	),
	MEMBER(
		setOf(
			StudyMemberRoleAuthority.CREATE_QUIZ,
			StudyMemberRoleAuthority.SOLVE_QUIZ,
			StudyMemberRoleAuthority.INVITE_MEMBER,
		),
	),
}