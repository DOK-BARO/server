package kr.kro.dokbaro.server.core.studygroup.domain

enum class StudyMemberRoleAuthority {
	CREATE_QUIZ,
	SOLVE_QUIZ,
	INVITE_MEMBER,
	APPROVE_MEMBER,
	REMOVE_MEMBER,
	CHANGE_STUDY_INFO,
	CHANGE_LEADER,
	DELETE_STUDY_GROUP,
}